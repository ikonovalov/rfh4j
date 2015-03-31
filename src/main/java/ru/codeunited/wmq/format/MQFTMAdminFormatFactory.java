package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.pcf.PCFMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.handler.MessageEvent;

import java.io.IOException;
import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 08.02.15.
 */
class MQFTMAdminFormatFactory implements FormatterFactory {

    private final ExecutionContext context;

    MQFTMAdminFormatFactory(ExecutionContext context) {
        this.context = context;
    }

    @Override
    public MQPCFMessageAbstractFormatter formatterFor(MQMessage message) throws MQException, IOException {
        final PCFMessage pcfMessage = new PCFMessage(message);
        final int commandCode = pcfMessage.getCommand();
        final MQPCFMessageAbstractFormatter formatter;
        switch (commandCode) {
            case MQCMD_ACTIVITY_TRACE:
                formatter = new MQFTMAdminActivityTraceFormatter();
                break;
            default:
                formatter = new MQFTMAdminCommonFormatter();
        }
        formatter.attach(context);
        return formatter;
    }

}
