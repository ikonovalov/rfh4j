package ru.codeunited.wmq.fx.controller;

import ru.codeunited.wmq.fx.FXExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 03.04.15.
 */
public abstract class ContextAwareController {

    protected final FXExecutionContext context;

    protected ContextAwareController() {
        this.context = FXExecutionContext.getInstance();
    }
}
