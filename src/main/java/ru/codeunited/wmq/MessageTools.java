package ru.codeunited.wmq;

import com.ibm.mq.MQMessage;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class MessageTools {

    public static MQMessage createMessage(int charset) {
        final MQMessage message = new MQMessage();
        message.characterSet = charset;
        return message;
    }

    public static MQMessage stringFormat(MQMessage message) {
        message.format = MQFMT_STRING;
        return message;
    }

    public static MQMessage createUTFMessage() {
        return createMessage(1208);
    }

}
