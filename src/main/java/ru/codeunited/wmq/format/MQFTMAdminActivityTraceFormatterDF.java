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

        // scan for MQGACF_ACTIVITY_TRACE
        /*Enumeration<PCFParameter> parametersL1 = pcfMessage.getParameters();
        while (parametersL1.hasMoreElements()) {
            final PCFParameter parameter = parametersL1.nextElement();


            // process activity trace elements (MQGACF_ACTIVITY_TRACE is always grouped as MQCFGR)
            MQCFGR trace = (MQCFGR) parameter; // => MQGACF_ACTIVITY_TRACE

            // timestamp;msgId;queueName;operation(put/get);QMGRNAme;size;<Строка заголовков - вида name=value>;
            boolean xmitExchange = "MQXMIT".equals(decodedParameter(trace, MQCACH_FORMAT_NAME));
            final String putDateTime = String.format("%s %s", decodedParameter(trace, MQCACF_PUT_DATE), decodedParameter(trace, MQCACF_PUT_TIME));
            try {
                buffer.append(TIME_REFORMATED.format(TIME_FORMAT.parse(putDateTime)));
            } catch (ParseException e) {
                buffer.append(putDateTime);
            } finally {
                buffer.append(';');
            }
            buffer.append( *//* append message id *//*
                    xmitExchange ?
                            decodedParameter(trace, MQBACF_XQH_MSG_ID) :
                            decodedParameter(trace, MQBACF_MSG_ID))
                    .append(';');

            buffer.append( *//* append queue name *//*
                    xmitExchange ?
                            String.format("%s;%s;",
                                    coalesce(trace, MQCACF_OBJECT_NAME, MQCACF_RESOLVED_Q_NAME, MQCACF_RESOLVED_LOCAL_Q_NAME),
                                    decodedParameter(trace, MQCACF_XQH_REMOTE_Q_NAME)
                            ) :
                            String.format("%s;;",
                                    coalesce(trace, MQCACF_OBJECT_NAME, MQCACF_RESOLVED_LOCAL_Q_NAME, MQCACF_RESOLVED_LOCAL_Q_NAME)
                            )
            );

            buffer.append(decodeValue(mqiacfOperation)).append(';');

            buffer.append( *//* append queuemanager name *//*
                    xmitExchange ?
                            String.format("%s;%s;",
                                    coalesce(trace, MQCACF_OBJECT_Q_MGR_NAME, MQCACF_RESOLVED_Q_MGR, MQCACF_RESOLVED_LOCAL_Q_MGR),
                                    decodedParameter(trace, MQCACF_XQH_REMOTE_Q_MGR)
                            ) :
                            String.format("%s;;",
                                    coalesce(trace, MQCACF_OBJECT_Q_MGR_NAME, MQCACF_RESOLVED_Q_MGR, MQCACF_RESOLVED_LOCAL_Q_MGR)
                            )
            );

            buffer.append(decodedParameter(trace, MQIACF_MSG_LENGTH)).append(';');
            buffer.append(decodedParameter(pcfMessage, MQIA_APPL_TYPE)).append(';');
            buffer.append(decodedParameter(pcfMessage, MQCACF_APPL_NAME)).append(';');
            buffer.append(decodedParameter(pcfMessage, MQCACF_USER_IDENTIFIER));
            //buffer.append(decodedParameter(trace, MQBACF_MESSAGE_DATA)).append(';');

            buffer.append('\n');
            allowOutput = true;

        }*/
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
