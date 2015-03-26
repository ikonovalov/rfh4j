package ru.codeunited.wmq.messaging.pcf.mq800;

import com.ibm.mq.pcf.PCFMessage;
import ru.codeunited.wmq.messaging.pcf.WrongTypeException;
import ru.codeunited.wmq.messaging.pcf.mq750.ActivityTraceCommand750;

import static com.ibm.mq.constants.CMQC.MQIA_COMMAND_LEVEL;
import static com.ibm.mq.constants.CMQCFC.MQCMD_ACTIVITY_TRACE;

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
}
