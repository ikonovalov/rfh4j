package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQMessage;
import com.ibm.mq.headers.CCSID;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import static com.ibm.mq.constants.CMQC.MQFMT_NONE;
import static com.ibm.mq.constants.MQConstants.MQFMT_STRING;
import static com.ibm.mq.constants.MQConstants.MQPER_PERSISTENT;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public final class MessageTools {

    public static final int UTF8_CCSID = 1208;

    private static final int STREAM_BUFFER_SZ = 2048;

    private static Logger LOG = Logger.getLogger(MessageTools.class.getName());

    private MessageTools() {
        super();
    }

    /**
     * Create MQMessage with specified characterSet and MQFMT_NONE
     * @param charset for example 1208 (UTF-8)
     * @return MQMessage
     */
    public static MQMessage createMessage(int charset) {
        final MQMessage message = createEmptyMessage();
        message.characterSet = charset;
        message.persistence = MQPER_PERSISTENT;
        message.format = MQFMT_NONE;
        return message;
    }

    public static MQMessage createEmptyMessage() {
        return new MQMessage();
    }

    /**
     * Create MQMessage with characterSet = 1208 and MQFMT_STRING
     * @return MQMessage
     */
    public static MQMessage createUTFMessage() {
        MQMessage mqMessage = createMessage(UTF8_CCSID);
        mqMessage.format = MQFMT_STRING;
        return mqMessage;
    }

    /**
     * Write string to MQMessage
     * @param str - string message.
     * @param message - MQQueue commandCode
     * @return MQMessage with written str
     * @throws IOException
     */
    public static void writeStringToMessage(String str, MQMessage message) throws IOException {
        message.writeString(str);
    }

    public static String fileNameForMessage(MQMessage message) {
        return bytesToHex(message.messageId) + ".message";
    }

    public static void writeStreamToMessage(InputStream stream, MQMessage message) throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_SZ];
        int readCount;
        long totalBytes = 0; // total message bytes counter
        while ((readCount = stream.read(buffer)) != -1) {
            message.write(buffer, 0, readCount);
            totalBytes += readCount;
        }
        LOG.fine("File with size " + totalBytes + "b stored in a message.");
    }

    public static byte[] readMessageBodyToBytes(MQMessage message) throws IOException {
        final byte[] buffer = new byte[message.getDataLength()];
        message.readFully(buffer);
        return buffer;
    }

    /**
     * Read full message body as string using message CCSID
     * @param message
     * @return
     * @throws IOException
     */
    public static String readMessageBodyToString(MQMessage message) throws IOException {
        byte[] asByte = readMessageBodyToBytes(message);
        String bodyAsString = new String(asByte, CCSID.getCodepage(message.characterSet));
        return bodyAsString;
    }

    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    // taken from http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java

    /**
     * Convert bytes to hex. Using uppercase characters.
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return "";
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
