package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableColumnName;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.MessageTools;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import java.io.File;
import java.io.IOException;

import static ru.codeunited.wmq.messaging.MessageTools.bytesToHex;
import static ru.codeunited.wmq.messaging.MessageTools.fileNameForMessage;
import static ru.codeunited.wmq.messaging.MessageTools.messageIdAsString;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public class MQGetCommand extends QueueCommand {

    public static final String GET_OPERATION_NAME = "GET";

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException {
        final ConsoleWriter console = getConsoleWriter();
        final ExecutionContext ctx = getExecutionContext();
        final String sourceQueueName = getSourceQueueName();
        try {
            final MessageConsumer messageConsumer = new MessageConsumerImpl(sourceQueueName, getQueueManager());
            try {
                final MQMessage message = shouldWait() ? messageConsumer.get(waitTime()) : messageConsumer.get();
                console.head(TableColumnName.ACTION, TableColumnName.QMANAGER, TableColumnName.QUEUE, TableColumnName.MESSAGE_ID, TableColumnName.OUTPUT);
                console.table(GET_OPERATION_NAME, getQueueManager().getName(), sourceQueueName, bytesToHex(message.messageId));

                // print to std output (console)
                if (ctx.hasOption("stream")) { // standard output to std.out
                    console
                            .tableAppendToLastRow("<stream>")
                            .printTable()
                            .write(message);
                }
                
                // print to a file (can used with conjunction with --stream)
                if (ctx.hasOption("payload")) {
                    File destination = new File(ctx.getOption("payload", fileNameForMessage(message)));

                    // if payload specified as folder, then we need to append file name
                    if (destination.exists() && destination.isDirectory()) {
                        destination = new File(destination.getAbsoluteFile() + File.separator + fileNameForMessage(message));
                    }
                    MessageTools.writeMessageBodyToFile(message, destination);
                    console
                            .tableAppendToLastRow(destination.getAbsolutePath())
                            .printTable();
                }
            } catch (NoMessageAvailableException e) {
                console.table(GET_OPERATION_NAME, getQueueManager().getName(), sourceQueueName, "[EMPTY QUEUE]");
            }
        } catch (MQException | IOException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }
}
