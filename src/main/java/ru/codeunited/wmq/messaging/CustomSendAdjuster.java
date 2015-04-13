package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 13.04.15.
 */
public interface CustomSendAdjuster {

    void setup(MQMessage message) throws IOException, MQException;

    void setup(MQPutMessageOptions options);

}
