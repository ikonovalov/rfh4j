package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.messaging.MessageSelector;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public interface MessageConsumer {

    /**
     * Get MQ message with specified GET_MESSAGE_OPTIONS.
     * @param mqGMO - message options for GET operation.
     * @return dequeue MQMessage.
     */
    MQMessage get(MQGetMessageOptions mqGMO) throws NoMessageAvailableException, MQException;

    /**
     * Get MQ message with default options and NO_WAIT strategy.
     * @return dequeue MQMessage.
     */
    MQMessage get() throws NoMessageAvailableException, MQException;

    /**
     * Get MQ message and wait specified period of time of no message in queue right now.
     * @param waitInterval  The maximum time (in milliseconds) get(interval) call waits for a suitable message to arrive.
     * @return MQMessage  dequeue MQMessage.
     * @throws NoMessageAvailableException if no one message in a queue.
     * @throws MQException
     */
    MQMessage get(int waitInterval) throws NoMessageAvailableException, MQException;

    MQMessage select(MessageSelector selector) throws NoMessageAvailableException, MQException;

}
