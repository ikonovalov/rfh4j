package ru.codeunited.wmq.messaging.impl;

import com.ibm.mq.*;
import ru.codeunited.wmq.messaging.MQLink;
import ru.codeunited.wmq.messaging.CustomSendAdjuster;
import ru.codeunited.wmq.messaging.MessageProducer;
import ru.codeunited.wmq.messaging.MessageTools;

import java.io.IOException;
import java.io.InputStream;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 30.10.14.
 */
public class MessageProducerImpl implements MessageProducer {

    private final MQQueue queue;

    private final MQPutMessageOptions defaultPutSpec = new MQPutMessageOptions();

    public MessageProducerImpl(String queueName, MQLink link) throws MQException {
        MQQueueManager queueManager = link.getManager().get();
        this.queue = queueManager.accessQueue(queueName, MQOO_OUTPUT | MQOO_FAIL_IF_QUIESCING);
        initialize();
    }

    protected void initialize() {
        defaultPutSpec.options = defaultPutSpec.options | MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT;
    }

    private MQMessage putWithOptions(MQMessage mqMessage, MQPutMessageOptions options) throws MQException {
        queue.put(mqMessage, options);
        return mqMessage;
    }

    @Override
    public MQMessage send(String messageText, MQPutMessageOptions options) throws IOException, MQException {
        final MQMessage message = MessageTools.createUTFMessage();
        MessageTools.writeStringToMessage(messageText, message);
        return putWithOptions(message, options);
    }

    @Override
    public MQMessage send(InputStream stream, MQPutMessageOptions options) throws IOException, MQException {
        final MQMessage message = MessageTools.createMessage(MessageTools.UTF8_CCSID);
        MessageTools.writeStreamToMessage(stream, message);
        return putWithOptions(message, options);
    }

    @Override
    public MQMessage send(InputStream fileStream) throws IOException, MQException {
        return send(fileStream, defaultPutSpec);
    }

    @Override
    public MQMessage send(String text) throws IOException, MQException {
        return send(text, defaultPutSpec);
    }

    @Override
    public MQMessage send(CustomSendAdjuster builder) throws IOException, MQException {
        final MQMessage message = MessageTools.createEmptyMessage();
        final MQPutMessageOptions pmo = new MQPutMessageOptions();
        builder.setup(message);
        builder.setup(pmo);
        putWithOptions(message, defaultPutSpec);
        return message;
    }

    @Override
    public void close() throws IOException {
        try {
            this.queue.close();
        } catch (MQException e) {
            throw new IOException(e);
        }
    }
}
