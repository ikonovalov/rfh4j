package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import static com.ibm.mq.constants.CMQC.MQOO_OUTPUT;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
public abstract class QueueCommand extends AbstractCommand {

    public MQQueueManager getQueueManager() {
        return executionContext.getQueueManager();
    }

    private MQQueue getQueue(String name, int options) throws MQException {
        return getQueueManager().accessQueue(name, options);
    }

    /**
     * Create destination queue specified in --dstq parameter.
     * @return
     * @throws MQException
     */
    public MQQueue getDestinationQueue() throws MQException, MissedParameterException {
        if (hasOption("dstq")) {
            final String queueName = getOption("dstq");
            if (queueName.length() > 0) {
                return getQueue(queueName, MQOO_OUTPUT);
            } else {
                throw new MissedParameterException("dstq").withMessage("Parameter dstq has wrong argument [" + queueName +"]");
            }
        } else {
            throw new MissedParameterException("dstq");
        }
    }

}
