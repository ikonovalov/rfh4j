package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQPutMessageOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 30.10.14.
 */
public interface MessageProducer {

    /**
     * Send message with a string payload
     * @param message
     * @param options
     * @return
     */
    byte[] send(String message, MQPutMessageOptions options) throws IOException, MQException;

    /**
     * Send message with a byte stream payload.
     * @param stream
     * @param options for example MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT
     * @return byte[] of messageId
     */
    byte[] send(InputStream stream, MQPutMessageOptions options) throws IOException;

    /**
     * Send message with a byte steam payload using default put parameters.
     * @param fileStream
     * @return byte[] of a messageId.
     * @throws IOException
     */
    byte[] send(FileInputStream fileStream) throws IOException;

    /**
     * Send message with a text payload on a board.
     * @param text
     * @return byte[] of a messageId.
     * @throws IOException
     * @throws MQException
     */
    byte[] send(String text) throws IOException, MQException;
}
