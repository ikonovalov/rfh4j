package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

import java.util.Properties;

/**
 * This is a not thread-safe implementation.
 * Created by ikonovalov on 22.10.14.
 */
public class WMQDefaultConnectionFactory implements WMQConnectionFactory {

    private final Properties connectionProperties;

    private final String queueManagerName;

    private MQQueueManager queueManager;

    public WMQDefaultConnectionFactory(String queueManager, Properties properties) {
        this.connectionProperties = properties;
        this.queueManagerName = queueManager;
    }

    @Override
    public MQQueueManager connectQueueManager() throws MQException {
        queueManager = new MQQueueManager(queueManagerName, connectionProperties);
        return queueManager;
    }
}
