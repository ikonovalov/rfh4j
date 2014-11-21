package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import static com.ibm.mq.constants.CMQC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public class MessageInspectorImpl implements MessageInspector {

    private final MQQueue queue;

    public MessageInspectorImpl(String queueName, MQQueueManager queueManager) throws MQException {
        /** MQOO_INQUIRE -  inquire on a queue
         *  MQOO_FAIL_IF_QUIESCING -- access fail if queue manager is quiescing. **/
        this.queue = queueManager.accessQueue(queueName, MQOO_INQUIRE | MQOO_BROWSE | MQOO_FAIL_IF_QUIESCING); // MQOO_INPUT_SHARED?
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
