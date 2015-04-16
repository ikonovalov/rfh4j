package ru.codeunited.wmq.format;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.ibm.mq.headers.MQHeader;
import com.ibm.mq.headers.MQRFH2;
import org.apache.commons.lang3.StringUtils;
import ru.codeunited.wmq.messaging.HeaderUtilService;
import ru.codeunited.wmq.messaging.pcf.*;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

import static com.ibm.mq.constants.MQConstants.*;



/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
@Singleton
public class MQFMTAdminActivityTraceFormatterDepFin extends MQActivityTraceFormatter<String> {

    private static final int BUFFER_2Kb = 2048;

    private static final int MAX_BODY_LENGTH = 256;

    MQFMTAdminActivityTraceFormatterDepFin() {
        super();
    }

    private final static ActivityRecordFilter OPERATION_FILTER = new ActivityRecordXFOperationFilter(
            MQXFOperation.MQXF_GET,
            MQXFOperation.MQXF_PUT,
            MQXFOperation.MQXF_PUT1
    );


    @Override
    public String format(final ActivityTraceCommand activityCommand) {
        final StringBuffer buffer = new StringBuffer(BUFFER_2Kb);

        boolean allowOutput = false;

        List<ActivityTraceRecord> records = activityCommand.getRecords();

        for (ActivityTraceRecord record : records) {
            if (!record.isSuccess() || !OPERATION_FILTER.allowed(record)) // skip failed
                continue;
            if (record.getOperation().anyOf(MQXFOperation.MQXF_GET, MQXFOperation.MQXF_PUT)) {
                MQXFMessageMoveRecord moveRecord = (MQXFMessageMoveRecord) record;
                buffer.append(moveRecord.getPutDateTimeISO()).append(';');

                boolean xmitExchange = moveRecord.isTransmissionMessage();

                buffer.append( /* append message id */
                        xmitExchange ?
                                moveRecord.getXMITMessageId() :
                                moveRecord.getMessageId()
                ).append(';');

                buffer.append( /* append queue name */
                        xmitExchange ?
                                String.format("%s;%s;",
                                        extractQueueName(moveRecord),
                                        moveRecord.getXMITRemoteQueueName()
                                ) :
                                String.format("%s;;",
                                        extractQueueName(moveRecord)
                                )
                );

                buffer.append(moveRecord.getOperation().name()).append(';'); /* append operation name */

                buffer.append( /* append queuemanager name */
                        xmitExchange ?
                                String.format("%s;%s;",
                                        extractQManagerName(moveRecord),
                                        moveRecord.getXMITRemoteQueueMananger()
                                ) :
                                String.format("%s;;",
                                        extractQManagerName(moveRecord)
                                )
                );

                buffer.append(moveRecord.getMessageLength()).append(';');
                buffer.append(activityCommand.getApplicationName()).append(';');
                buffer.append(activityCommand.getApplicationType()).append(';');
                buffer.append(activityCommand.getUserId()).append(";");

                // print captured data (it has four slots: headerdata x3, bodydata x1)
                TraceData traceData = moveRecord.getData();
                final String format = moveRecord.getFormat();
                final Optional<List<MQHeader>> listOfHeadersOpt = traceData.getHeaders();
                final Optional<Object> bodyOpt = traceData.getBody();

                final String[] capturedOutBlock = new String[4];
                Arrays.fill(capturedOutBlock, "");

                switch (format) {
                    case MQFMT_RF_HEADER_2:
                        if (listOfHeadersOpt.isPresent()) {
                            MQRFH2 mqrfh2 = (MQRFH2) listOfHeadersOpt.get().get(0);
                            capturedOutBlock[0] = HeaderUtilService.getStringFromRHF2FolderSafe(mqrfh2, "usr", "id");
                            capturedOutBlock[1] = HeaderUtilService.getStringFromRHF2FolderSafe(mqrfh2, "usr", "type");
                            capturedOutBlock[2] = HeaderUtilService.getStringFromRHF2FolderSafe(mqrfh2, "usr", "status");

                            if (bodyOpt.isPresent()) {
                                if (MQFMT_STRING.equals(mqrfh2.getFormat())) {
                                    String realBody = trimBodyToMaxSize(bodyOpt);
                                    capturedOutBlock[3] = realBody;
                                } else {
                                    capturedOutBlock[3] = "[bytes]";
                                }
                            }
                        }
                        break;
                    case MQFMT_NONE:
                        if (bodyOpt.isPresent()) {
                            capturedOutBlock[3] = "[bytes]";
                        }
                        break;
                    case MQFMT_STRING:
                        if (bodyOpt.isPresent()) {
                            String realBody = trimBodyToMaxSize(bodyOpt);
                            capturedOutBlock[3] = realBody;
                        }
                        break;
                    default:
                }
                buffer.append(Joiner.on(";").join(capturedOutBlock));
                // end of printing captured data

                buffer.append('\n');
            }

            allowOutput = true;
        }

        if (!allowOutput) { // drop buffer in it contains nothing interesting.
            buffer.setLength(0);
        }
        return buffer.toString().trim();
    }

    protected static String trimBodyToMaxSize(Optional<Object> bodyOpt) {
        String realBody = (String) bodyOpt.get();
        if (realBody.length() > MAX_BODY_LENGTH) {
            realBody = realBody.substring(0, MAX_BODY_LENGTH) + "...";
        }
        return realBody;
    }

    protected static String extractQueueName(MQXFMessageMoveRecord moveRecord) {
        return coalesce(moveRecord.getObjectName(),moveRecord.getResolvedQueueName(), moveRecord.getResolvedLocalQueueName());
    }

    protected static String extractQManagerName(MQXFMessageMoveRecord moveRecord) {
        return coalesce(moveRecord.getObjectQueueManagerName(), moveRecord.getResolvedQueueManagerName(), moveRecord.getResolvedLocalQueueManagerName());
    }

    protected static String coalesce(String... choice) {
        if (choice != null)
            for (String c : choice) {
                if (StringUtils.isNotBlank(c)) {
                    return c;
                }
            }
        return "";
    }


}
