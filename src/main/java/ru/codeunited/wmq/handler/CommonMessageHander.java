package ru.codeunited.wmq.handler;

import ru.codeunited.wmq.ExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public abstract class CommonMessageHander<T> implements MessageHandler<T> {

    private final ExecutionContext context;

    protected CommonMessageHander(ExecutionContext context) {
        this.context = context;
    }

    public ExecutionContext getContext() {
        return context;
    }
}
