package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.pcf.PCFMessage;

import java.io.EOFException;
import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.02.15.
 */
public abstract class MQPCFMessageAbstractFormatter<T> extends MQFMTContextAwareFormatter implements PCFMessageFormatter<T> {

    @Override
    public final T format(MQMessage message) throws IOException, MQException {
        resetMQMessagePosition(message); /* going back or CC=2 and RC=6114 */
        final PCFMessage pcfMessage = new PCFMessage(message);
        resetMQMessagePosition(message);
        return format(pcfMessage, message);
    }

    private static void resetMQMessagePosition(MQMessage message) throws EOFException {
        message.seek(0);
    }
}
