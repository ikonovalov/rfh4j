package ru.codeunited.wmq.mock;

import com.ibm.mq.MQMessage;

import static com.ibm.mq.constants.MQConstants.*;
import static org.mockito.Mockito.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 31.03.15.
 */
public class MQMessageMock {

    public static MQMessage createMQFMTAdminMessage() {
        MQMessage message = mock(MQMessage.class);
        message.format = MQFMT_ADMIN;
        return message;
    }

    public static MQMessage createMQFMTNoneMessage() {
        MQMessage message = mock(MQMessage.class);
        message.format = MQFMT_NONE;
        return message;
    }

    public static MQMessage createMQFMTStringMessage() {
        MQMessage message = mock(MQMessage.class);
        message.format = MQFMT_STRING;
        return message;
    }

    public static MQMessage createMQFMTDLHMessage() {
        MQMessage message = mock(MQMessage.class);
        message.format = MQFMT_DEAD_LETTER_HEADER;
        return message;
    }
}
