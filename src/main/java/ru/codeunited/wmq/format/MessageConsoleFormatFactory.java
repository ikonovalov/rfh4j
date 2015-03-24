package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;

import java.io.IOException;

import static com.ibm.mq.constants.CMQC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public class MessageConsoleFormatFactory {

    private ExecutionContext context;

    public MessageConsoleFormatFactory(ExecutionContext context) {
        this.context = context;
    }

    public MessageConsoleFormatter formatterFor(MQMessage message) throws MQException, IOException {
        final String format = message.format;
        MessageConsoleFormatter formatter;
        switch (format) {
            case MQFMT_STRING:
                formatter = new MQFMTStringFormatter(message);
                break;
            case MQFMT_ADMIN:
                formatter = new MQFTMAdminFormatFactory(context).formatterFor(message);
                break;
            default:
                formatter = new MQFMTStringFormatter(message);
        }
        formatter.attach(context);
        return formatter;
    }

}
