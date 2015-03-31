package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

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
