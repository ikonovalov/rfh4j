package ru.codeunited.wmq.format;

import com.google.common.base.Optional;
import com.ibm.mq.headers.MQHeader;
import com.ibm.mq.headers.MQRFH2;
import org.apache.commons.lang3.StringUtils;
import ru.codeunited.wmq.messaging.pcf.*;

import java.util.List;

import static com.ibm.mq.constants.MQConstants.*;



/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public class MQFTMAdminActivityTraceFormatterDepFin extends MQActivityTraceFormatter<String> {

    private static final int BUFFER_2Kb = 2048;

    MQFTMAdminActivityTraceFormatterDepFin() {
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
                buffer.append(activityCommand.getUserId());

                // print captured data (it has two slots: headerdata, bodydata)
                TraceData traceData = moveRecord.getData();
                final String format = moveRecord.getFormat();
                final Optional<List<MQHeader>> listOfHeadersOpt = traceData.getHeaders();
                final Optional<Object> bodyOpt = traceData.getBody();
                switch (format) {
                    case MQFMT_RF_HEADER_2:
                        if (listOfHeadersOpt.isPresent()) {
                            MQRFH2 mqrfh2 = (MQRFH2) listOfHeadersOpt.get().get(0);

                            if (bodyOpt.isPresent()) {

                            }
                        }
                        break;
                    case MQFMT_NONE:
                        if (bodyOpt.isPresent()) {
                            buffer.append(";[bytes]");
                        }
                        break;
                    case MQFMT_STRING:
                        if (bodyOpt.isPresent()) {

                        }
                        break;
                    default:
                }
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
