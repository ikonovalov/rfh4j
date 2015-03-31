package ru.codeunited.wmq.messaging.pcf.mq800;

import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFParameter;
import ru.codeunited.wmq.messaging.pcf.ActivityTraceRecord;
import ru.codeunited.wmq.messaging.pcf.WrongTypeException;
import ru.codeunited.wmq.messaging.pcf.mq750.ActivityTraceCommand750;
import ru.codeunited.wmq.messaging.pcf.mq750.ActivityTraceRecord750;

import java.util.ArrayList;
import java.util.List;

import static com.ibm.mq.constants.CMQC.MQIA_COMMAND_LEVEL;
import static com.ibm.mq.constants.CMQCFC.MQCMD_ACTIVITY_TRACE;
import static com.ibm.mq.constants.CMQCFC.MQGACF_ACTIVITY_TRACE;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class ActivityTraceCommand800 extends ActivityTraceCommand750 {

    private static final String MQ_800 = "800";

    public ActivityTraceCommand800(PCFMessage pcfMessage) {
        super(pcfMessage);
    }

    @Override
    protected void check() {
        if (pcfMessage.getCommand() != MQCMD_ACTIVITY_TRACE)
            throw new WrongTypeException("Can't handled as MQCMD_ACTIVITY_TRACE. Actual command is " + pcfMessage.getCommand());
        final String commandLevel = decodedParameter(MQIA_COMMAND_LEVEL);
        if (!MQ_800.equals(commandLevel)) {
            throw new WrongTypeException("This command has different level. Required 800 but got " + commandLevel);
        }
    }

    @Override
    public List<ActivityTraceRecord> getRecords() {
        final List<PCFParameter> filteredParams = getParamaters(MQGACF_ACTIVITY_TRACE);
        final List<ActivityTraceRecord> traceRecords = new ArrayList<>(filteredParams.size());
        for (PCFParameter par : filteredParams) {
            traceRecords.add(ActivityTraceRecord800.create(par));
        }
        return traceRecords;
    }
}
