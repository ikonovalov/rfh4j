package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQQueueManager;

import java.io.Closeable;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public interface QueueManager extends Closeable {

    MQQueueManager get();

    boolean isConnected();

}
