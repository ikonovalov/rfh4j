package ru.codeunited.wmq.cli;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.pcf.PCFMessage;

import java.io.IOException;
import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 08.02.15.
 */
public class MQFTMAdminFormatFactory {

    public MessageConsoleFormatter formatterFor(MQMessage message) throws MQException, IOException {
        final PCFMessage pcfMessage = new PCFMessage(message);
        final int commandCode = pcfMessage.getCommand();
        switch (commandCode) {
            case MQCMD_ACTIVITY_TRACE:
                return new MQFTMAdminActivityTraceFormatter(pcfMessage);
            default:
                return new MQFTMAdminCommonFormatter(pcfMessage);
        }
    }

}
