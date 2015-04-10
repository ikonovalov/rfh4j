package ru.codeunited.wmq.messaging.impl;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.messaging.ConnectionOptions;
import ru.codeunited.wmq.messaging.WMQConnectionFactory;

/**
 * This is a not thread-safe implementation.
 * Created by ikonovalov on 22.10.14.
 */
public class WMQDefaultConnectionFactory implements WMQConnectionFactory {

    public WMQDefaultConnectionFactory() {

    }

    @Override
    public MQQueueManager connectQueueManager(ConnectionOptions connectionOptions) throws MQException {
        return new MQQueueManager(
                connectionOptions.getQueueManagerName(),
                connectionOptions.getOptions()
        );
    }
}
