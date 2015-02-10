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

    /**
     * Return 'wait' parameter value.
     * @return value or -1 if 'wait' passed without argument.
     */
    protected int waitTime() {
        if (getExecutionContext().getOption("wait") == null) {
            return -1;
        } else {
            return Integer.valueOf(getExecutionContext().getOption("wait"));
        }
    }

    /**
     * true - If passed --wait parameter.
     * true - if MQGet in the listener mode. (with negative limit)
     *
     * @return true if context has 'wait' option or 'limit' has negative value..
     */
    protected boolean shouldWait() {
        final ExecutionContext context = getExecutionContext();
        return isListenerMode() || context.hasOption("wait");
    }

    /**
     * Return true if limit has negative value;
     * @return
     */
    protected boolean isListenerMode() {
        final ExecutionContext context = getExecutionContext();
        return context.hasOption("limit") && Integer.valueOf(context.getOption("limit")) < 0;
    }


    /**
     * Return maximum message count limit or defaultValue.
     * @return int.
     */
    protected int getMessagesCountLimit(int defaultValue) {
        final ExecutionContext ctx = getExecutionContext();
        return ctx.hasOption("limit") ? Integer.valueOf(ctx.getOption("limit")) : defaultValue;
    }

    /**
     * Return value of limit parameter or 1 if not specified.
     * @return
     */
    protected int getMessagesCountLimit() {
        return getMessagesCountLimit(1);
    }
}
