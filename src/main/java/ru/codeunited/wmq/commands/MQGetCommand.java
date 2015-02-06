package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import static ru.codeunited.wmq.cli.CLIFactory.*;
import ru.codeunited.wmq.cli.ConsoleTable;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableColumnName;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import java.io.File;
import java.io.IOException;

import static ru.codeunited.wmq.messaging.MessageTools.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public class MQGetCommand extends QueueCommand {

    public static final String GET_OPERATION_NAME = "GET";

    private static final TableColumnName[] TABLE_HEADER = {
            TableColumnName.INDEX,
            TableColumnName.ACTION,
            TableColumnName.QMANAGER,
            TableColumnName.QUEUE,
            TableColumnName.MESSAGE_ID,
            TableColumnName.CORREL_ID,
            TableColumnName.OUTPUT
    };

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
        if (ctx.hasOption(OPT_STREAM) && ctx.hasOption("all")) {
            raiseIncompatibeException(String.format("Options --%1$s and --all can't run together. Use --%2$s instead --%1$s.", OPT_STREAM, OPT_PAYLOAD));
        }
    }

    private ConsoleTable createTable(ConsoleWriter console) {
        return console.createTable(TABLE_HEADER);
    }

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException {
        final ConsoleWriter console = getConsoleWriter();
        final ExecutionContext ctx = getExecutionContext();
        final String sourceQueueName = getSourceQueueName();

        try {
            final MessageConsumer messageConsumer = new MessageConsumerImpl(sourceQueueName, getQueueManager());
            boolean queueHasMessages = false;
            try {
                int limit = getMessagesCountLimit(1); // default is only one message per command
                int messageCouter = 0;
                while (isListenerMode() || limit-->0) {
                    final MQMessage message = shouldWait() ? messageConsumer.get(waitTime()) : messageConsumer.get();
                    queueHasMessages = true;
                    final ConsoleTable table = createTable(console);
                    table.append(String.valueOf(messageCouter), GET_OPERATION_NAME, getQueueManager().getName(), sourceQueueName, bytesToHex(message.messageId), bytesToHex(message.correlationId));

                    // print to std output (console)
                    if (ctx.hasOption(OPT_STREAM)) { // standard output to std.out
                        table.appendToLastRow("<stream>").flash();
                        console.write(message);
                    } else  if (ctx.hasOption(OPT_PAYLOAD)) { /* print to a file */
                        File destination = new File(ctx.getOption(OPT_PAYLOAD, fileNameForMessage(message)));

                        // if payload specified as folder, then we need to append file name
                        if (destination.exists() && destination.isDirectory()) {
                            destination = new File(destination.getAbsoluteFile() + File.separator + fileNameForMessage(message));
                        }
                        writeMessageBodyToFile(message, destination);
                        table.appendToLastRow(destination.getAbsolutePath());
                    }
                    table.flash();
                    messageCouter++;
                }

            } catch (NoMessageAvailableException e) {
                if (!queueHasMessages) { // prevent output extra information if queue has messages.
                    createTable(console)
                            .append(String.valueOf(0), GET_OPERATION_NAME, getQueueManager().getName(), sourceQueueName, "[EMPTY QUEUE]")
                            .flash();
                }
            }
        } catch (MQException | IOException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
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
     * true - If passed --wait parameter.
     * true - if MQGet in the listener mode. (with negative limit)
     *
     * @return true if context has 'wait' option or 'limit' has negative value..
     */
    protected boolean shouldWait() {
        final ExecutionContext context = getExecutionContext();
        return isListenerMode() || context.hasOption("wait");
    }

    protected boolean isListenerMode() {
        final ExecutionContext context = getExecutionContext();
        return context.hasOption("limit") && Integer.valueOf(context.getOption("limit")) < 0;
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
}
