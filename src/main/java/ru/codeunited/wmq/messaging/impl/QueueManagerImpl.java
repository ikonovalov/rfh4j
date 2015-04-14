package ru.codeunited.wmq.messaging.impl;

import com.google.common.base.Objects;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.messaging.QueueManager;

import java.io.IOException;

import static com.ibm.mq.constants.MQConstants.*;

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
        if (manager != null) {
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

    protected String getAttribute(int code, int length) {
        try {
            return manager.getAttributeString(code, length);
        } catch (MQException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDLQName() {
        return getAttribute(MQCA_DEAD_LETTER_Q_NAME, MQ_Q_NAME_LENGTH);
    }

    @Override
    public String getDescription() {
        return getAttribute(MQCA_Q_MGR_DESC, MQ_Q_MGR_DESC_LENGTH);
    }

    @Override
    public String getIdentefier() {
        return getAttribute(MQCA_Q_MGR_IDENTIFIER, MQ_Q_MGR_IDENTIFIER_LENGTH);
    }

    @Override
    public String getName() {
        return getAttribute(MQCA_Q_MGR_NAME, MQ_Q_MGR_NAME_LENGTH);
    }
}
