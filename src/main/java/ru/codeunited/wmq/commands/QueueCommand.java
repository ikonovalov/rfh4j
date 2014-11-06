package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.ExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
public abstract class QueueCommand extends AbstractCommand {

    public MQQueueManager getQueueManager() {
        return executionContext.getQueueManager();
    }

    /**
     * Create destination queue specified in --dstq parameter.
     * @return String - destination queue name.
     * @throws MQException
     */
    public String getDestinationQueueName() throws MQException, MissedParameterException {
        final ExecutionContext ctx = getExecutionContext();
        if (ctx.hasOption("dstq")) {
            final String queueName = ctx.getOption("dstq");
            if (queueName.length() > 0) {
                return queueName;
            } else {
                throw new MissedParameterException("dstq").withMessage("Parameter dstq has wrong argument [" + queueName +"]");
            }
        } else {
            throw new MissedParameterException("dstq");
        }
    }

}
