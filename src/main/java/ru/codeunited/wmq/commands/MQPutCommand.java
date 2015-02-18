package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleTable;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableColumnName;
import ru.codeunited.wmq.messaging.MessageProducer;
import ru.codeunited.wmq.messaging.MessageProducerImpl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static ru.codeunited.wmq.RFHConstants.*;
import static ru.codeunited.wmq.messaging.MessageTools.bytesToHex;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQPutCommand extends QueueCommand {

    private static final char FILE_PAYLOAD = 'p';

    private static final char TEXT_PAYLOAD = 't';

    private static final char STREAM_PAYLOAD = 's';

    @Override
    public void work() throws CommandGeneralException, MissedParameterException {
        final ConsoleWriter console = getConsoleWriter();
        final ConsoleTable table = console.createTable(
                        TableColumnName.ACTION, TableColumnName.QMANAGER, TableColumnName.QUEUE, TableColumnName.MESSAGE_ID, TableColumnName.MSG_SIZE);
        final ExecutionContext ctx = getExecutionContext();
        try {
            final MessageProducer messageProducer = new MessageProducerImpl(getDestinationQueueName(), getQueueManager());
            MQMessage sentMessage;
            // handle payload parameters
            if (ctx.hasOption(OPT_PAYLOAD)){ // file payload
                try (final FileInputStream fileStream = new FileInputStream(ctx.getOption(OPT_PAYLOAD))) {
                    sentMessage = messageProducer.send(fileStream);
                }
            } else if (ctx.hasOption(TEXT_PAYLOAD)) { // just text message
                sentMessage = messageProducer.send(ctx.getOption(TEXT_PAYLOAD));
            } else if (ctx.hasOption(OPT_STREAM)) {
                try (final BufferedInputStream bufferedInputStream = new BufferedInputStream(System.in)) {
                    sentMessage = messageProducer.send(bufferedInputStream);
                }
            } else {
                throw new MissedParameterException(OPT_PAYLOAD, "text", OPT_STREAM);
            }

            table
                    .append("PUT", getQueueManager().getName(), getDestinationQueueName(), bytesToHex(sentMessage.messageId), sentMessage.getMessageLength() + "b")
                    .flash();

        } catch (IOException | MQException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }
}
