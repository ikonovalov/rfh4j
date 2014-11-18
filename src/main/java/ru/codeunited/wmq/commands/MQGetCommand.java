package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import java.io.IOException;

import static ru.codeunited.wmq.messaging.MessageTools.bytesToHex;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public class MQGetCommand extends QueueCommand {

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException {
        final ConsoleWriter console = getConsoleWriter();
        final ExecutionContext ctx = getExecutionContext();
        final String sourceQueueName = getSourceQueueName();
        try {
            final MessageConsumer messageConsumer = new MessageConsumerImpl(sourceQueueName, getQueueManager());
            if (ctx.hasOption('s')) {
                try {
                    final MQMessage message = messageConsumer.get();

                    console.table("GET", getQueueManager().getName(), sourceQueueName, bytesToHex(message.messageId));
                    console.write(message);
                } catch (NoMessageAvailableException e) {
                    console.table("GET", getQueueManager().getName(), sourceQueueName, "[EMPTY]");
                }
            }
        } catch (MQException | IOException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }
}
