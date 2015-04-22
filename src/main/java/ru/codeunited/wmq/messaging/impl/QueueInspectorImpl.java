package ru.codeunited.wmq.messaging.impl;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import ru.codeunited.wmq.messaging.MQLink;
import ru.codeunited.wmq.messaging.QueueInspector;

import java.io.IOException;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public class QueueInspectorImpl implements QueueInspector {

    private final MQQueue queue;

    public QueueInspectorImpl(String queueName, MQLink link) throws MQException {
        /** MQOO_INQUIRE -  inquire on a queue
         *  MQOO_FAIL_IF_QUIESCING -- access fail if queue manager is quiescing. **/
        this.queue = link.getManager().get().accessQueue(queueName, MQOO_INQUIRE | MQOO_BROWSE | MQOO_FAIL_IF_QUIESCING); // MQOO_INPUT_SHARED?
    }

    public void close() throws IOException {
        try {
            queue.close();
        } catch (MQException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int depth() throws MQException {
        return queue.getCurrentDepth();
    }

    @Override
    public int maxDepth() throws MQException {
        return queue.getMaximumDepth();
    }

    @Override
    public int openInputCount() throws MQException {
        return queue.getOpenInputCount();
    }

    @Override
    public int opentOutputCount() throws MQException {
        return queue.getOpenOutputCount();
    }
}
