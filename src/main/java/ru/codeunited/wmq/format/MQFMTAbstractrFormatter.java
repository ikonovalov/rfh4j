package ru.codeunited.wmq.format;

import com.ibm.mq.MQMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.02.15.
 */
public abstract class MQFMTAbstractrFormatter<T> implements MessageConsoleFormatter<T> {

    protected final MQMessage message;


    protected MQFMTAbstractrFormatter(MQMessage message) {
        this.message = message;
    }
}
