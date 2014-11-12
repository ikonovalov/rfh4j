package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class MessageTools {

    private static final int DEFAULT_CHARACTER_SET = 1208;

    private static final int STREAM_BUFFER_SZ = 2048;

    private static Logger LOG = Logger.getLogger(MessageTools.class.getName());

    /**
     * Create MQMessage with specified characterSet.
     * @param charset for example 1208 (UTF-8)
     * @return MQMessage
     */
    public static MQMessage createMessage(int charset) {
        final MQMessage message = new MQMessage();
        message.characterSet = charset;
        return message;
    }

    /**
     * Set MQMessage format to STR (8)
     * @param message for setup
     * @return input MQMessage, not copy.
     */
    public static MQMessage stringFormat(MQMessage message) {
        message.format = MQFMT_STRING;
        return message;
    }

    /**
     * Create MQMessage with characterSet = 1208;
     * @return MQMessage
     */
    public static MQMessage createUTFMessage() {
        return createMessage(DEFAULT_CHARACTER_SET);
    }

    /**
     * Write string to MQMessage
     * @param str - string message.
     * @param message - MQQueue object
     * @return MQMessage with written str
     * @throws IOException
     */
    public static MQMessage writeStringToMessage(String str, MQMessage message) throws IOException {
        message.writeString(str);
        return message;
    }

    public MQMessage writeUTFToMessage(String utf, MQMessage message) throws IOException {
        message.writeUTF(utf);
        return message;
    }

    public static MQMessage writeStreamToMessage(InputStream stream, MQMessage message) throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_SZ];
        int readCount;
        long totalBytes = 0; // total message bytes counter
        while ((readCount = stream.read(buffer)) != -1) {
            message.write(buffer, 0, readCount);
            totalBytes += readCount;
        }

        LOG.fine("File with size " + totalBytes + "b stored in a message.");
        return message;
    }

}
