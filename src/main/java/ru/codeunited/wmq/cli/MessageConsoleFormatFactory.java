package ru.codeunited.wmq.cli;

import com.ibm.mq.MQMessage;

import static com.ibm.mq.constants.CMQC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
class MessageConsoleFormatFactory {

    static MessageConsoleFormatter formatterFor(MQMessage message) {
        final String format = message.format;
        switch (format) {
            case MQFMT_STRING:
                return new MQFMTStringFormatter();
            case MQFMT_ADMIN:
                return new MQFTMAdminActivityFormatter();
            default:
                return new MQFMTStringFormatter();
        }
    }

}
