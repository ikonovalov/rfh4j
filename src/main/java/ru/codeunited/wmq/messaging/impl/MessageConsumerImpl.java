package ru.codeunited.wmq.messaging.impl;

import com.ibm.mq.*;
import ru.codeunited.wmq.messaging.MQLink;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.MessageSelector;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import java.io.IOException;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public class MessageConsumerImpl implements MessageConsumer {

    private final MQQueue queue;

    private final int DEFAULT_GET_OPTIONS = MQGMO_FAIL_IF_QUIESCING | MQGMO_NO_SYNCPOINT;

    /**
     * Create message consumer for a MQ queue.
     * @param queueName - name of a queue
     * @param link - connected queue manager.
     * @throws MQException - if something goes wrong.
     */
    public MessageConsumerImpl(String queueName, MQLink link) throws MQException {
        /** MQOO_INPUT_AS_Q_DEF -- open queue to get message
         *  using queue-define default.
         *  MQOO_FAIL_IF_QUIESCING -- access fail if queue manager is quiescing. **/
        this.queue = link.getManager().get().accessQueue(queueName, MQOO_FAIL_IF_QUIESCING | MQOO_INPUT_AS_Q_DEF);
    }

    private MQMessage get(MQMessage message, MQGetMessageOptions getMessageOptions) throws NoMessageAvailableException, MQException {
        try {
            queue.get(message, getMessageOptions);
        } catch (MQException mqe) {
            if (mqe.reasonCode == MQRC_NO_MSG_AVAILABLE)
                throw new NoMessageAvailableException(mqe);
            else
                throw mqe;
        }
        return message;
    }


    @Override
    public MQMessage get(MQGetMessageOptions getMessageOptions) throws NoMessageAvailableException, MQException {
        final MQMessage message = new MQMessage();
        return get(message, getMessageOptions);
    }

    @Override
    public MQMessage get() throws NoMessageAvailableException, MQException {
        final MQGetMessageOptions messageOptions = new MQGetMessageOptions();
        messageOptions.options = DEFAULT_GET_OPTIONS | MQGMO_NO_WAIT;
        //gmo.waitInterval = MQC.MQWI_UNLIMITED;
        return get(messageOptions);
    }

    @Override
    public MQMessage get(int waitInterval) throws NoMessageAvailableException, MQException {
        final MQGetMessageOptions messageOptions = new MQGetMessageOptions();
        messageOptions.options = DEFAULT_GET_OPTIONS | MQGMO_WAIT;
        if (waitInterval < 0)
            messageOptions.waitInterval = MQWI_UNLIMITED;
        else
            messageOptions.waitInterval = waitInterval;
        return get(messageOptions);
    }

    @Override
    public MQMessage select(MessageSelector selector) throws NoMessageAvailableException, MQException {
        final MQGetMessageOptions messageOptions = new MQGetMessageOptions();
        messageOptions.options = DEFAULT_GET_OPTIONS | MQGMO_NO_WAIT;
        final MQMessage message = new MQMessage();
        selector.setup(messageOptions, message);
        return get(message, messageOptions);
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
