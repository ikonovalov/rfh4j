package ru.codeunited.wmq.handler;

import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class HandlerLookupService {

    private final ExecutionContext context;


    public HandlerLookupService(ExecutionContext context) {
        this.context = context;
    }

    public MessageHandler lookup(Class clazz) {
        return null;
    }
}
