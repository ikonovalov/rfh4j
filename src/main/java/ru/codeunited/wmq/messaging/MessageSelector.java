package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public interface MessageSelector {

    void setup(MQGetMessageOptions messageOptions, MQMessage message);
}
