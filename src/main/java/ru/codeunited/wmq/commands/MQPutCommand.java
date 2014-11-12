package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.messaging.MessageProducer;
import ru.codeunited.wmq.messaging.MessageProducerImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import static ru.codeunited.wmq.messaging.MessageTools.bytesToHex;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQPutCommand extends QueueCommand {

    private static final char FILE_PAYLOAD = 'p';

    private static final char TEXT_PAYLOAD = 't';

    @Override
    public void work() throws CommandGeneralException, MissedParameterException {
        final ConsoleWriter console = getConsoleWriter();
        final ExecutionContext ctx = getExecutionContext();
        try {

            final MessageProducer messageProducer = new MessageProducerImpl(getDestinationQueueName(), getQueueManager());
            byte[] messageId;
            // handle payload parameters
            if (ctx.hasOption(FILE_PAYLOAD)) { // file payload
                try (final FileInputStream fileStream = new FileInputStream(ctx.getOption(FILE_PAYLOAD))) {
                    messageId = messageProducer.send(fileStream);
                }
            } else if (ctx.hasOption(TEXT_PAYLOAD)) { // just text message
                messageId = messageProducer.send(ctx.getOption(TEXT_PAYLOAD));
            } else {
                throw new MissedParameterException(FILE_PAYLOAD, TEXT_PAYLOAD);
            }

            console.table("PUT", getQueueManager().getName(), getDestinationQueueName(), bytesToHex(messageId));
        } catch (IOException | MQException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }
}
