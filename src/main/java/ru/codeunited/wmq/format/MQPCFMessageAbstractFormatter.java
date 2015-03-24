package ru.codeunited.wmq.format;

import com.ibm.mq.pcf.PCFMessage;
import ru.codeunited.wmq.ExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.02.15.
 */
public abstract class MQPCFMessageAbstractFormatter<T> implements MessageConsoleFormatter<T> {

    protected ExecutionContext context;

    public void attach(ExecutionContext context) {
        this.context = context;
    }

    protected final PCFMessage pcfMessage;

    protected MQPCFMessageAbstractFormatter(PCFMessage pcfMessage) {
        this.pcfMessage = pcfMessage;
    }
}
