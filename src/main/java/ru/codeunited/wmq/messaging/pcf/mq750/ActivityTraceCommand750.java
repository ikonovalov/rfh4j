package ru.codeunited.wmq.messaging.pcf.mq750;

import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFParameter;
import ru.codeunited.wmq.messaging.MessageTools;
import ru.codeunited.wmq.messaging.pcf.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ibm.mq.constants.CMQC.*;
import static com.ibm.mq.constants.CMQCFC.*;

/**
 *
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class ActivityTraceCommand750 extends PCFMessageWrapper implements ActivityTraceCommand {

    private static final String MQ_750 = "750";

    public ActivityTraceCommand750(PCFMessage pcfMessage) {
        super(pcfMessage);
    }

    @Override
    protected void check() {
        if (pcfMessage.getCommand() != MQCMD_ACTIVITY_TRACE)
            throw new WrongTypeException("Can't be handled as MQCMD_ACTIVITY_TRACE. Actual command code is " + pcfMessage.getCommand());
        final String commandLevel = decodedParameter(MQIA_COMMAND_LEVEL);
        if (!MQ_750.equals(commandLevel)) {
            throw new WrongTypeException("This command has different level. Required 750 but got " + commandLevel);
        }
    }

    @Override
    public String getCorrelationId() {
        return MessageTools.bytesToHex(super.getCorrelationIdBytes());
    }

    @Override
    public Date getStartDate() {
        return createDateTime(MQCAMO_START_DATE, MQCAMO_START_TIME);
    }

    @Override
    public String getStartDateRaw() {
        return createDateTimeRaw(MQCAMO_START_DATE, MQCAMO_START_TIME);
    }

    @Override
    public Date getEndDate() {
        return createDateTime(MQCAMO_END_DATE, MQCAMO_END_TIME);
    }

    @Override
    public String getEndDateRaw() {
        return createDateTimeRaw(MQCAMO_END_DATE, MQCAMO_END_TIME);
    }

    @Override
    public String getQueueManager() {
        return decodedParameter(MQCA_Q_MGR_NAME);
    }

    @Override
    public String getHost() {
        return decodedParameter(MQCACF_HOST_NAME);
    }

    @Override
    public Integer getCommandLevel() {
        return Integer.valueOf(decodedParameter(MQIA_COMMAND_LEVEL));
    }

    @Override
    public Integer getSequenceNumber() {
        return decodedParameterAsInt(MQIACF_SEQUENCE_NUMBER);
    }

    @Override
    public String getApplicationName() {
        return decodedParameter(MQCACF_APPL_NAME);
    }

    @Override
    public String getApplicationType() {
        return decodedParameter(MQIA_APPL_TYPE);
    }

    @Override
    public Integer getProcessId() {
        return decodedParameterAsInt(MQIACF_PROCESS_ID);
    }

    @Override
    public String getUserId() {
        return decodedParameter(MQCACF_USER_IDENTIFIER);
    }

    @Override
    public Integer getAPICallerType() {
        return decodedParameterAsInt(MQIACF_API_CALLER_TYPE);
    }

    @Override
    public Integer getAPIEnvironment() {
        return decodedParameterAsInt(MQIACF_API_ENVIRONMENT);
    }

    @Override
    public String getChannel() {
        return decodedParameter(MQCACH_CHANNEL_NAME);
    }

    @Override
    public String getChannelType() {
        return decodedParameter(MQIACH_CHANNEL_TYPE);
    }

    @Override
    public String connectionName() {
        return decodedParameter(MQCACH_CONNECTION_NAME);
    }

    @Override
    public ActivityTraceLevel getTraceDetail() {
        Integer level = decodedParameterAsInt(MQIACF_TRACE_DETAIL);
        return ActivityTraceLevel.forCode(level);
    }

    @Override
    public String getPlatform() {
        return decodedParameter(MQIA_PLATFORM);
    }

    @Override
    public List<ActivityTraceRecord> getRecords() {
        final List<PCFParameter> filteredParams = getParamaters(MQGACF_ACTIVITY_TRACE);
        final List<ActivityTraceRecord> traceRecords = new ArrayList<>(filteredParams.size());
        for (PCFParameter par : filteredParams) {
            traceRecords.add(ActivityTraceRecord750.create(par));
        }
        return traceRecords;
    }
}
