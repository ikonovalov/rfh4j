package ru.codeunited.wmq.commands;

import com.google.inject.Key;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.handler.*;
import ru.codeunited.wmq.messaging.MessageProducer;
import ru.codeunited.wmq.messaging.impl.MessageProducerImpl;
import ru.codeunited.wmq.messaging.pcf.MQXFOperation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static ru.codeunited.wmq.RFHConstants.OPT_PAYLOAD;
import static ru.codeunited.wmq.RFHConstants.OPT_STREAM;
import static ru.codeunited.wmq.RFHConstants.OPT_TEXT;

/**
 * Thread-safe.
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQPutCommand extends QueueCommand {

    @Override
    public void work() throws CommandGeneralException, MissedParameterException, NestedHandlerException {
        final ExecutionContext ctx = getExecutionContext();

        try {
            final MessageProducer messageProducer = new MessageProducerImpl(getDestinationQueueName(), getExecutionContext().getLink());

            int repeatTimes = getMessagesCountLimit();
            int sentIndex = 0;
            while (repeatTimes-->0) {
                MQMessage sentMessage;
                // handle payload parameters
                if (ctx.hasOption(OPT_PAYLOAD)) { // file payload
                    try (final FileInputStream fileStream = new FileInputStream(ctx.getOption(OPT_PAYLOAD))) {
                        sentMessage = messageProducer.send(fileStream);
                    }
                } else if (ctx.hasOption(OPT_TEXT)) { // just text message
                    sentMessage = messageProducer.send(ctx.getOption(OPT_TEXT));
                } else if (ctx.hasOption(OPT_STREAM)) {
                    try (final BufferedInputStream bufferedInputStream = new BufferedInputStream(System.in)) {
                        sentMessage = messageProducer.send(bufferedInputStream);
                    }
                } else {
                    throw new MissedParameterException(OPT_PAYLOAD, OPT_TEXT, OPT_STREAM);
                }

                // create event
                final EventSource eventSource = new EventSource(getDestinationQueueName());
                final MessageEvent event = new MessageEvent(eventSource);
                event.setMessageIndex(sentIndex);
                event.setMessage(sentMessage);
                event.setOperation(MQXFOperation.MQXF_PUT);

                // publish event
                MessageHandler handler = injectorProvider.get().getInstance(Key.get(MessageHandler.class, PrintStream.class));
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
