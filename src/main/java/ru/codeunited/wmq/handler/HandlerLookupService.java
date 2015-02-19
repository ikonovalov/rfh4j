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

    private final ConsoleWriter console;


    public HandlerLookupService(ExecutionContext context, ConsoleWriter console) {
        this.context = context;
        this.console = console;
    }

    public MessageHandler lookup(Class clazz) {
        return null;
    }
}
