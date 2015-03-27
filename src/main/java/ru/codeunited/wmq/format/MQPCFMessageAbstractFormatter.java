package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.pcf.PCFMessage;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.02.15.
 */
public abstract class MQPCFMessageAbstractFormatter<T> extends MQFMTContextAwareFormatter<T> {

    private PCFMessage pcfMessage;

    /**
     * Used in a format() method with PCFMessage.
     * @param message
     * @return
     */
    public abstract T formatPCFMessage(PCFMessage message);

    /**
     * This is optimisation measure. In general factory already has conversion MQMessage to PCFMessage.
     * It prevents double conversion in a format method. But in is not necessary.
     * @param pcfMessage
     */
    void presetMessage(PCFMessage pcfMessage) {
        this.pcfMessage = pcfMessage;
    }

    @Override
    public final T format(MQMessage message) throws IOException, MQException {
        if (pcfMessage == null) {
            message.seek(0); /* going back or CC=2 and RC=6114 */
            pcfMessage = new PCFMessage(message);
        }
        return formatPCFMessage(pcfMessage);
    }
}
