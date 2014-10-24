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
    public MQQueue getDestinationQueue() throws MQException, ParameterException {
        if (hasOption("dstq")) {
            final String queueName = getOption("dstq");
            final MQQueue queue = getQueue("MFC.APPLICATION_OUT", MQOO_OUTPUT);
            return queue;
        } else {
            throw new ParameterException("dstq");
        }
    }

}
