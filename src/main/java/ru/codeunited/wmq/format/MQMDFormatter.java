package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 03.02.15.
 */
public class MQMDFormatter extends MQFMTContextAwareFormatter implements MessageFormatter<String> {

    private static final String BOARDER = "<------------------MQMD--------------------------->";

    MQMDFormatter() {
        super();
    }

    @Override
    public String format(final MQMessage message) throws IOException, MQException {
        final StringBuffer buffer = new StringBuffer();
        boarder(buffer);
        buffer.append(String.format("Format: %s\n", message.format));
        boarder(buffer);
        return buffer.toString();
    }

    private void boarder(StringBuffer buffer) {
        buffer.append(BOARDER).append('\n');
    }
}
