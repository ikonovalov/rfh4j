package ru.codeunited.wmq.format;

import com.ibm.mq.MQMessage;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
@Singleton
public class MQFMTStringFormatter extends MQFMTContextAwareFormatter<String> {

    private static final String BORDER = "<--------------MQFMT_STRING-------------------->";

    protected MQFMTStringFormatter() {
        super();
    }

    @Override
    public String format(final MQMessage message) throws IOException {
        message.seek(0); // going to beginning of message
        final int size = message.getDataLength(); // all remaining data size
        final StringBuffer buffer = new StringBuffer(size);
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
