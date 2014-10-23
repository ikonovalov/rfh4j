package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

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
    public MQQueueManager connectQueueManager() throws MQException;

}
