package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 30.10.14.
 */
public interface MessageProducer extends Closeable {

    /**
     * Send message with a string payload
     * @param message
     * @param options
     * @return MQMessage
     */
    MQMessage send(String message, MQPutMessageOptions options) throws IOException, MQException;

    /**
     * Send message with a byte stream payload.
     * @param stream
     * @param options for example MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT
     * @return MQMessage
     */
    MQMessage send(InputStream stream, MQPutMessageOptions options) throws IOException, MQException;

    /**
     * Send message with a byte steam payload using default put parameters.
     * @param fileStream
     * @return MQMessage
     * @throws IOException
     */
    MQMessage send(InputStream fileStream) throws IOException, MQException;

    /**
     * Send message with a text payload on a board.
     * @param text
     * @return MQMessage
     * @throws IOException
     * @throws MQException
     */
    MQMessage send(String text) throws IOException, MQException;
}
