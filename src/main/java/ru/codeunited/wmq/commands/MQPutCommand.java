package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleTable;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableColumnName;
import ru.codeunited.wmq.handler.*;
import ru.codeunited.wmq.messaging.MQOperation;
import ru.codeunited.wmq.messaging.MessageProducer;
import ru.codeunited.wmq.messaging.MessageProducerImpl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static ru.codeunited.wmq.RFHConstants.OPT_PAYLOAD;
import static ru.codeunited.wmq.RFHConstants.OPT_STREAM;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQPutCommand extends QueueCommand {

    @Override
    public void work() throws CommandGeneralException, MissedParameterException, NestedHandlerException {
        final ExecutionContext ctx = getExecutionContext();

        try {
            final MessageProducer messageProducer = new MessageProducerImpl(getDestinationQueueName(), getQueueManager());

            int repeatTimes = getMessagesCountLimit();
            int sentIndex = 0;
            while (repeatTimes-->0) {
                MQMessage sentMessage;
                // handle payload parameters
                if (ctx.hasOption(OPT_PAYLOAD)) { // file payload
                    try (final FileInputStream fileStream = new FileInputStream(ctx.getOption(OPT_PAYLOAD))) {
                        sentMessage = messageProducer.send(fileStream);
                    }
                } else if (ctx.hasOption("text")) { // just text message
                    sentMessage = messageProducer.send(ctx.getOption("text"));
                } else if (ctx.hasOption(OPT_STREAM)) {
                    try (final BufferedInputStream bufferedInputStream = new BufferedInputStream(System.in)) {
                        sentMessage = messageProducer.send(bufferedInputStream);
                    }
                } else {
                    throw new MissedParameterException(OPT_PAYLOAD, "text", OPT_STREAM);
                }

                // create event
                final EventSource eventSource = new EventSource(getDestinationQueueName());
                final MessageEvent event = new MessageEvent(eventSource);
                event.setMessageIndex(sentIndex);
                event.setMessage(sentMessage);
                event.setOperation(MQOperation.MQPUT);

                // publish event
                MessageHandler handler = new PrintStreamHandler(ctx, getConsoleWriter());
                handler.onMessage(event);

                sentIndex++;
            }

        } catch (IOException | MQException e) {
            LOG.severe(e.getMessage());
            getConsoleWriter().errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }
}
