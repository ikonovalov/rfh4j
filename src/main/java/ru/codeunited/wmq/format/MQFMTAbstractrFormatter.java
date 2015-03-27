package ru.codeunited.wmq.format;

import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.02.15.
 */
public abstract class MQFMTAbstractrFormatter<T> implements MessageConsoleFormatter<T> {

    protected final MQMessage message;

    protected ExecutionContext context;

    public void attach(ExecutionContext context) {
        this.context = context;
    }

    protected MQFMTAbstractrFormatter(MQMessage message) {
        this.message = message;
    }
}
