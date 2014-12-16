package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
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

    /**
     * Check input context options and raise IncompatibleOptionsException if something wrong.
     * @throws IncompatibleOptionsException
     */
    protected void validateOptions() throws IncompatibleOptionsException, MissedParameterException {
        ExecutionContext ctx = getExecutionContext();
        if (ctx.hasOption("stream") && ctx.hasOption("all")) {
            throw new IncompatibleOptionsException("Options --stream and --all can't run together. Use --payload instead --stream.");
        }
    }

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException {
        validateOptions();
        final ConsoleWriter console = getConsoleWriter();
        final ConsoleTable table = console.createTable(
                        TableColumnName.ACTION, TableColumnName.QMANAGER, TableColumnName.QUEUE, TableColumnName.MESSAGE_ID, TableColumnName.CORREL_ID, TableColumnName.OUTPUT);

        final ExecutionContext ctx = getExecutionContext();
        final String sourceQueueName = getSourceQueueName();

        try {
            final MessageConsumer messageConsumer = new MessageConsumerImpl(sourceQueueName, getQueueManager());
            try {
                final MQMessage message = shouldWait() ? messageConsumer.get(waitTime()) : messageConsumer.get();

                table.append(GET_OPERATION_NAME, getQueueManager().getName(), sourceQueueName, bytesToHex(message.messageId), bytesToHex(message.correlationId));

                // print to std output (console)
                if (ctx.hasOption("stream")) { // standard output to std.out
                    table.appendToLastRow("<stream>").flash();
                    console.write(message);
                }
                
                // print to a file (can used with conjunction with --stream)
                if (ctx.hasOption("payload")) {
                    File destination = new File(ctx.getOption("payload", fileNameForMessage(message)));

                    // if payload specified as folder, then we need to append file name
                    if (destination.exists() && destination.isDirectory()) {
                        destination = new File(destination.getAbsoluteFile() + File.separator + fileNameForMessage(message));
                    }
                    writeMessageBodyToFile(message, destination);
                    table.appendToLastRow(destination.getAbsolutePath()).flash();
                }
            } catch (NoMessageAvailableException e) {
                table.append(GET_OPERATION_NAME, getQueueManager().getName(), sourceQueueName, "[EMPTY QUEUE]").flash();
            }
        } catch (MQException | IOException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }
}
