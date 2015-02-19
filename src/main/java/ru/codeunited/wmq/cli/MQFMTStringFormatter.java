package ru.codeunited.wmq.cli;

import com.ibm.mq.MQMessage;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public class MQFMTStringFormatter implements MessageConsoleFormatter {

    private static final String BORDER = "<--------------MQFMT_STRING-------------------->";

    @Override
    public String format(MQMessage message) throws IOException {
        message.seek(0); // going to begining of message
        final int size = message.getDataLength(); // all remaining data size
        final StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("Remain %d bytes", size));
        boarder(buffer);
        buffer.append(message.readStringOfByteLength(message.getDataLength()));
        boarder(buffer);

        return buffer.toString();
    }

    private void boarder(StringBuffer buffer) {
        buffer.append('\n').append(BORDER).append('\n');
    }
}
