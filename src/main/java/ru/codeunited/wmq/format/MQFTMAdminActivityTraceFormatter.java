package ru.codeunited.wmq.format;

import com.ibm.mq.MQMessage;
import com.ibm.mq.pcf.PCFMessage;
import ru.codeunited.wmq.messaging.pcf.*;

import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public class MQFTMAdminActivityTraceFormatter extends MQPCFMessageAbstractFormatter<String> {

    private static final int BUFFER_2Kb = 2048;

    MQFTMAdminActivityTraceFormatter() {
        super();
    }

    private final static ActivityRecordFilter OPERATION_FILTER = new ActivityRecordXFOperationFilter(
            MQXFOperation.MQXF_GET,
            MQXFOperation.MQXF_PUT,
            MQXFOperation.MQXF_PUT1
    );


    @Override
    public String format(PCFMessage pcfMessage, MQMessage mqMessage) {

        final StringBuilder buffer = new StringBuilder(BUFFER_2Kb);

        ActivityTraceCommand activityCommand = PCFUtilService.activityCommandFor(pcfMessage, mqMessage);

        buffer.append(String.format("COR[%s][%d] TRR[%s -> %s] QM[%s] APP[%s %d] USR[%s] CHL[%s:%s]\n",
                activityCommand.getCorrelationId(),
                activityCommand.getSequenceNumber(),
                activityCommand.getStartDateRaw(), /* TRace Record - TRR time*/
                activityCommand.getStartDateRaw(),
                activityCommand.getQueueManager(),
                activityCommand.getApplicationName(),
                activityCommand.getProcessId(),
                activityCommand.getUserId(),
                activityCommand.getChannelType(),
                activityCommand.getChannel()
        ));

        boolean allowOutput = false;

        List<ActivityTraceRecord> records = activityCommand.getRecords();
        for (ActivityTraceRecord record : records) {

            if (!OPERATION_FILTER.allowed(record)) { /* skip uninteresting operations */
                continue;
            }

            buffer.append(String.format("\topr[%s] [%s] otm[%s] ",
                    record.getOperation().name(),
                    record.getCompCode(),
                    record.getOperationDateISO()

            ));
            if (record.getOperation().anyOf(MQXFOperation.MQXF_GET, MQXFOperation.MQXF_PUT)) {
                MQXFMessageMoveRecord moveRecord = (MQXFMessageMoveRecord) record;
                buffer.append(String.format("obj:[%s] mid:[%s] cid:[%s] len:[%s] dat:[%s]",
                        //coalesce(trace, MQCACF_OBJECT_NAME, MQCACF_RESOLVED_LOCAL_Q_NAME, MQCACF_RESOLVED_LOCAL_Q_NAME),
                        moveRecord.getResolvedLocalQueueName(),
                        moveRecord.getMessageId(),
                        moveRecord.getCorrelId(),
                        moveRecord.getMessageLength(),
                        moveRecord.getBodyAsString()
                ));
            }
            buffer.append('\n');
            allowOutput = true;
        }

        /*Enumeration<PCFParameter> parametersL1 = pcfMessage.getParameters();
        while (parametersL1.hasMoreElements()) {
            final PCFParameter parameter = parametersL1.nextElement();

            // process activity trace elements (MQGACF_ACTIVITY_TRACE is always grouped as MQCFGR)
            MQCFGR trace = (MQCFGR) parameter; // => MQGACF_ACTIVITY_TRACE

            final PCFParameter mqiacfOperation = parameterOf(trace, MQIACF_OPERATION_ID);
            if (parameterOf(trace, MQIACF_COMP_CODE).getValue().equals(MQCC_OK) // => skip failed operations
                    && OPERATION_FILTER.allowed(mqiacfOperation)) { // => skip not interesting operations

            }

        }*/
        if (!allowOutput) { // drop buffer in it contains nothing interesting.
            buffer.setLength(0);
        }

        return buffer.toString();
    }
}
