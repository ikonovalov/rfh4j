package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public interface MessageSelector {

    /**
     * Setup MQGET options and query message header (message or correlation id for instance).
     * @param messageOptions
     * @param message
     */
    void setup(MQGetMessageOptions messageOptions, MQMessage message);
}
