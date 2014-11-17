package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import static com.ibm.mq.constants.CMQC.MQOO_BROWSE;
import static com.ibm.mq.constants.CMQC.MQOO_FAIL_IF_QUIESCING;
import static com.ibm.mq.constants.CMQC.MQOO_INQUIRE;

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
        this.queue = queueManager.accessQueue(queueName, MQOO_INQUIRE | MQOO_BROWSE | MQOO_FAIL_IF_QUIESCING);
    }


    @Override
    public int depth() throws MQException {
        return queue.getCurrentDepth();
    }
}
