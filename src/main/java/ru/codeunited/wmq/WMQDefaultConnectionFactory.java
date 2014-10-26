package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

import java.util.Properties;

/**
 * This is a not thread-safe implementation.
 * Created by ikonovalov on 22.10.14.
 */
public class WMQDefaultConnectionFactory implements WMQConnectionFactory {

    public WMQDefaultConnectionFactory() {

    }

    @Override
    public MQQueueManager connectQueueManager(String queueManagerName, Properties properties) throws MQException {
        return new MQQueueManager(queueManagerName, properties);
    }
}
