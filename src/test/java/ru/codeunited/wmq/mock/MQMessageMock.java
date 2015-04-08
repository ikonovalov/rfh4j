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

}
