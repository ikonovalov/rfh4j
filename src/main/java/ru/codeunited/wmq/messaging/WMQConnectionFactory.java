package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

import java.util.Properties;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public interface WMQConnectionFactory {

    /**
     * Connect to WebSphere queue manager.
     * @return
     * @throws MQException
     */
    public MQQueueManager connectQueueManager(String queueManagerName, Properties properties) throws MQException;

}
