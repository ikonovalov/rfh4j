package ru.codeunited.wmq.commands;

import com.google.inject.Key;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableColumnName;
import ru.codeunited.wmq.handler.*;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.impl.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;
import ru.codeunited.wmq.messaging.pcf.MQXFOperation;

import java.io.IOException;
import java.util.logging.Logger;

import static ru.codeunited.wmq.RFHConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public class MQGetCommand extends QueueCommand {

    private static final Logger LOG = Logger.getLogger(MQGetCommand.class.getName());

    /**
     * Check input context options and raise IncompatibleOptionsException if something wrong.
     * @throws IncompatibleOptionsException
     */
    @Override
    protected void validateOptions() throws IncompatibleOptionsException, MissedParameterException {
        final ExecutionContext ctx = getExecutionContext();
        if (ctx.hasntOption(OPT_STREAM, OPT_PAYLOAD)) {
            raiseMissedParameters(new String[]{OPT_STREAM, OPT_PAYLOAD});
        }
        if (ctx.hasOption(OPT_STREAM) && ctx.hasOption(OPT_ALL)) {
            raiseIncompatibeException(String.format("Options --%1$s and --%3$s can't run together. Use --%2$s instead --%1$s.", OPT_STREAM, OPT_PAYLOAD, OPT_ALL));
        }
    }

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException, NestedHandlerException {
        final ConsoleWriter console = getConsoleWriter();
        final String sourceQueueName = getSourceQueueName();

        try(final MessageConsumer messageConsumer = new MessageConsumerImpl(sourceQueueName, getExecutionContext().getLink())) {
            boolean queueHasMessages = false;
            try {
                int limit = getMessagesCountLimit(1); // default is only one message per command
                int messageCouter = 0;
                long startTime = System.currentTimeMillis();
                while (isListenerMode() || limit-->0) {
                    // in listener mode shouldWait = true, waitTime() = -1 (infinity)
                    final MQMessage message = shouldWait() ? messageConsumer.get(waitTime()) : messageConsumer.get();
                    queueHasMessages = true;
                    handleMessage(messageCouter, message);
                    messageCouter++;
                }
                LOG.fine(">> total " + messageCouter + " in " + (System.currentTimeMillis() - startTime) + "ms");

            } catch (NoMessageAvailableException e) {
                if (!queueHasMessages) { // prevent output extra information if queue has messages.
                    handleNoMessage(console, sourceQueueName);
                }
            }
        } catch (MQException | IOException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }

    void handleNoMessage(ConsoleWriter console, String sourceQueueName) {
        TableColumnName[] header = {
                TableColumnName.INDEX,
                TableColumnName.ACTION,
                TableColumnName.QMANAGER,
                TableColumnName.QUEUE,
                TableColumnName.MESSAGE_ID,
                TableColumnName.CORREL_ID,
                TableColumnName.OUTPUT
        };

        console.createTable(header)
                .append(String.valueOf(0), MQXFOperation.MQXF_GET.name(), getExecutionContext().getLink().getOptions().getQueueManagerName(), sourceQueueName, "[EMPTY QUEUE]")
                .make();
    }

    void handleMessage(final int messageIndex, final MQMessage message) throws MQException, IOException, MissedParameterException, NestedHandlerException {

        // create event
        final EventSource eventSource = new EventSource(getSourceQueueName());
        final MessageEvent event = new MessageEvent(eventSource);
        event.setMessageIndex(messageIndex);
        event.setMessage(message);
        event.setOperation(MQXFOperation.MQXF_GET);

        final HandlerLookupService lookupService = new HandlerLookupService(executionContext);

        // lookup handler
        if (executionContext.hasOption(OPT_HANDLER)) {
            try {
                MessageHandler handler = lookupService.lookup(Class.forName(executionContext.getOption(OPT_HANDLER)));
                handler.onMessage(event);
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }
        } else if (executionContext.hasOption(OPT_STREAM)) { // standard output to std.out
            MessageHandler handler = injectorProvider.get().getInstance(Key.get(MessageHandler.class, PrintStream.class));
            handler.onMessage(event);

        } else if (executionContext.hasOption(OPT_PAYLOAD)) { /* print to a file */
            MessageHandler handler = injectorProvider.get().getInstance(Key.get(MessageHandler.class, BodyToFile.class));
            handler.onMessage(event);
        }
    }

}
