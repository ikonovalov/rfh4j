package ru.codeunited.wmq.format;

import com.ibm.mq.MQMessage;
import com.ibm.mq.pcf.PCFMessage;
import org.apache.commons.lang3.StringUtils;
import ru.codeunited.wmq.messaging.pcf.*;

import java.text.SimpleDateFormat;
import java.util.List;



/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public class MQFTMAdminActivityTraceFormatterDF extends MQPCFMessageAbstractFormatter<String> {

    private static final int BUFFER_2Kb = 2048;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyyMMdd HHmmss");

    private static final SimpleDateFormat TIME_REFORMATED = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    MQFTMAdminActivityTraceFormatterDF() {
        super();
    }

    private final static ActivityRecordFilter OPERATION_FILTER = new ActivityRecordXFOperationFilter(
            MQXFOperation.MQXF_GET,
            MQXFOperation.MQXF_PUT,
            MQXFOperation.MQXF_PUT1,
            MQXFOperation.MQXF_OPEN
    );


    @Override
    public String format(PCFMessage pcfMessage, MQMessage mqMessage) {
        final StringBuffer buffer = new StringBuffer(BUFFER_2Kb);

        ActivityTraceCommand activityCommand = PCFUtilService.activityCommandFor(pcfMessage, mqMessage);

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
                //buffer.append(decodedParameter(trace, MQBACF_MESSAGE_DATA)).append(';');

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
