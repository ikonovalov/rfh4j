package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;

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
    MQLink connectQueueManager(ConnectionOptions connectionOptions) throws MQException;

}
