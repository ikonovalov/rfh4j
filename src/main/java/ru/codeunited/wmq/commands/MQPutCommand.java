package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.messaging.MessageProducer;
import ru.codeunited.wmq.messaging.MessageProducerImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQPutCommand extends QueueCommand {

    @Override
    public void work() throws CommandGeneralException, MissedParameterException {
        final ConsoleWriter console = getConsoleWriter();
        final ExecutionContext ctx = getExecutionContext();
        try {

            final MessageProducer messageProducer = new MessageProducerImpl(getDestinationQueueName(), getQueueManager());
            byte[] messageId;
            // handle payload parameters
            if (ctx.hasOption('p')) { // file payload
                try (final FileInputStream fileStream = new FileInputStream(ctx.getOption('p'))) {
                    messageId = messageProducer.send(fileStream);
                }
            } else if (ctx.hasOption('t')) { // just text message
                messageId = messageProducer.send(ctx.getOption('t'));
            } else {
                throw new MissedParameterException('p','t');
            }

            console.table(getQueueManager().getName(), getDestinationQueueName(), "PUT", UUID.nameUUIDFromBytes(messageId).toString());
        } catch (IOException | MQException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }

    @Override
    public boolean resolve() {
        return true;
    }
}
