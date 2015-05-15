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

    /**
     * Create new mocked MQMessage with specific format.
     * @param format
     * @return
     */
    public static MQMessage makeNew(String format) {
        MQMessage message = mock(MQMessage.class);
        message.format = format;
        return message;
    }

    /**
     * MQFMT_ADMIN
     * @return
     */
    public static MQMessage createMQFMTAdminMessage() {
        return  makeNew(MQFMT_ADMIN);
    }

    /**
     * MQFMT_NONE
     * @return
     */
    public static MQMessage createMQFMTNoneMessage() {
        return  makeNew(MQFMT_NONE);
    }

    /**
     * MQFMT_STRING
     * @return
     */
    public static MQMessage createMQFMTStringMessage() {
        return  makeNew(MQFMT_STRING);
    }

    /**
     * MQFMT_DEAD_LETTER_HEADER
     * @return
     */
    public static MQMessage createMQFMTDLHMessage() {
        return  makeNew(MQFMT_DEAD_LETTER_HEADER);
    }
}
