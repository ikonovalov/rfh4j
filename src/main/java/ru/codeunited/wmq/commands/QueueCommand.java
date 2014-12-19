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
    public String getDestinationQueueName() throws MissedParameterException {
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

    public String getSourceQueueName() throws MissedParameterException {
        final ExecutionContext ctx = getExecutionContext();
        if (ctx.hasOption("srcq")) {
            final String queueName = ctx.getOption("srcq");
            if (queueName.length() > 0) {
                return queueName;
            } else {
                throw new MissedParameterException("srcq").withMessage("Parameter srcq has wrong argument [" + queueName +"]");
            }
        } else {
            throw new MissedParameterException("srcq");
        }
    }
}
