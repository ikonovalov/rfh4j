package ru.codeunited.wmq.format;

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

    public static MessageConsoleFormatter formatterFor(MQMessage message, Class clazz) throws MQException, IOException {
        final String format = message.format;
        switch (format) {
            case MQFMT_STRING:
                return new MQFMTStringFormatter(message);
            case MQFMT_ADMIN:
                return new MQFTMAdminFormatFactory().formatterFor(message);
            default:
                return new MQFMTStringFormatter(message);
        }
    }

}
