package ru.codeunited.wmq.format;

import ru.codeunited.wmq.ExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.02.15.
 */
public abstract class MQFMTContextAwareFormatter<T> implements MessageFormatter<T> {

    protected ExecutionContext context;

    public void attach(ExecutionContext context) {
        this.context = context;
    }

    protected MQFMTContextAwareFormatter() {
        super();
    }
}
