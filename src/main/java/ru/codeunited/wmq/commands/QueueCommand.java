package ru.codeunited.wmq.commands;

import com.google.inject.Injector;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.messaging.MQLink;

import javax.inject.Inject;
import javax.inject.Provider;

import static ru.codeunited.wmq.RFHConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
public abstract class QueueCommand extends AbstractCommand {

    @Inject
    protected Provider<Injector> injectorProvider;

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
        if (getExecutionContext().getOption(OPT_WAIT) == null) {
            return -1;
        } else {
            return Integer.valueOf(getExecutionContext().getOption(OPT_WAIT));
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
        return isListenerMode() || context.hasOption(OPT_WAIT);
    }

    /**
     * Return true if limit has negative value;
     * @return
     */
    protected boolean isListenerMode() {
        final ExecutionContext context = getExecutionContext();
        return context.hasOption(OPT_LIMIT) && Integer.valueOf(context.getOption(OPT_LIMIT)) < 0;
    }


    /**
     * Return maximum message count limit/times or defaultValue.
     * @return int.
     */
    protected int getMessagesCountLimit(int defaultValue) {
        return Math.max(
                getMessageRepeatCount(defaultValue, OPT_LIMIT),
                getMessageRepeatCount(defaultValue, OPT_TIMES)
        );
    }

    private int getMessageRepeatCount(int defaultValue, String parameterName) {
        final ExecutionContext ctx = getExecutionContext();
        return ctx.hasOption(parameterName) ? Integer.valueOf(ctx.getOption(parameterName)) : defaultValue;
    }

    /**
     * Return value of limit parameter or 1 if not specified.
     * @return
     */
    protected int getMessagesCountLimit() {
        return getMessagesCountLimit(1);
    }
}
