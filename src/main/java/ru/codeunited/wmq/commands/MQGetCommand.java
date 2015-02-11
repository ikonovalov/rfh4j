package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import static ru.codeunited.wmq.cli.CLIFactory.*;
import ru.codeunited.wmq.cli.ConsoleTable;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.MessageConsoleFormatFactory;
import ru.codeunited.wmq.cli.TableColumnName;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import java.io.File;
import java.io.IOException;
import static com.ibm.mq.constants.MQConstants.*;
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
                    // in listener mode shouldWait = true, waitTime() = -1 (infinity)
                    final MQMessage message = shouldWait() ? messageConsumer.get(waitTime()) : messageConsumer.get();
                    queueHasMessages = true;
                    handleMessage(messageCouter, message, console);
                    messageCouter++;
                }

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

    void handleNoMessage(ConsoleWriter console, String sourceQueueName) throws MQException {
        console.createTable(TABLE_HEADER)
                .append(String.valueOf(0), GET_OPERATION_NAME, getQueueManager().getName(), sourceQueueName, "[EMPTY QUEUE]")
                .flash();
    }

    void handleMessage(final int messageIndex, final MQMessage message, final ConsoleWriter console) throws MQException, IOException, MissedParameterException {
        final ExecutionContext ctx = getExecutionContext();
        // print to std output (console)
        if (ctx.hasOption(OPT_STREAM)) { // standard output to std.out
            handleAsStream(messageIndex, message, console);
        } else  if (ctx.hasOption(OPT_PAYLOAD)) { /* print to a file */
            handleAsPayload(messageIndex, message, console);
        }

    }

    private void handleAsPayload(int messageIndex, MQMessage message, ConsoleWriter console) throws IOException, MQException, MissedParameterException {
        final ExecutionContext ctx = getExecutionContext();
        final ConsoleTable table = console.createTable(TABLE_HEADER);
        table.append(String.valueOf(messageIndex), GET_OPERATION_NAME, getQueueManager().getName(), getSourceQueueName(), bytesToHex(message.messageId), bytesToHex(message.correlationId));

        File destination = new File(ctx.getOption(OPT_PAYLOAD, fileNameForMessage(message)));

        // if payload specified as folder, then we need to append file name
        if (destination.exists() && destination.isDirectory()) {
            destination = new File(destination.getAbsoluteFile() + File.separator + fileNameForMessage(message));
        }
        writeMessageBodyToFile(message, destination);
        table.appendToLastRow(destination.getAbsolutePath());
        table.flash();
    }

    private void handleAsStream(int messageIndex, MQMessage message, ConsoleWriter console) throws IOException, MQException, MissedParameterException {
        final String messageFormat = message.format;
        switch (messageFormat) {
            case MQFMT_STRING:
                final ConsoleTable table = console.createTable(TABLE_HEADER);
                table.append(String.valueOf(messageIndex), GET_OPERATION_NAME, getQueueManager().getName(), getSourceQueueName(), bytesToHex(message.messageId), bytesToHex(message.correlationId));
                table.appendToLastRow("<stream>").flash();
                table.flash();
            case MQFMT_ADMIN:
            default:
                console.write(message);
        }
    }
}
