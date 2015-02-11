package ru.codeunited.wmq.cli;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;

import java.io.IOException;

import static com.ibm.mq.constants.CMQC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public class MessageConsoleFormatFactory {

    public static MessageConsoleFormatter formatterFor(MQMessage message) throws MQException, IOException {
        final String format = message.format;
        switch (format) {
            case MQFMT_STRING:
                return new MQFMTStringFormatter();
            case MQFMT_ADMIN:
                return new MQFTMAdminFormatFactory().formatterFor(message);
            default:
                return new MQFMTStringFormatter();
        }
    }

}
