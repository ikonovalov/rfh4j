package ru.codeunited.wmq.messaging.impl;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.messaging.QueueManager;

import java.io.IOException;
import java.util.Objects;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 11.04.15.
 */
class QueueManagerImpl implements QueueManager {

    private MQQueueManager manager;

    QueueManagerImpl(MQQueueManager manager) {
        this.manager = manager;
    }

    @Override
    public MQQueueManager get() {
        return manager;
    }

    @Override
    public boolean isConnected() {
        if (Objects.nonNull(manager)) {
            return manager.isConnected();
        } else {
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            manager.disconnect();
        } catch (MQException e) {
            throw new IOException(e);
        }
    }
}
