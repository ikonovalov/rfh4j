package ru.codeunited.wmq.format;

import ru.codeunited.wmq.ExecutionContext;

import javax.inject.Inject;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.02.15.
 */
public abstract class MQFMTContextAwareFormatter<T> implements MessageFormatter<T> {

    protected ExecutionContext context;

    @Inject
    public void attach(ExecutionContext context) {
        if (this.context != null) {
            throw new IllegalStateException("Context already set");
        }
        this.context = context;
    }

    protected MQFMTContextAwareFormatter() {
        super();
    }
}
