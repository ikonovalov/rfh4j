package ru.codeunited.wmq;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.codeunited.wmq.messaging.MQLink;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public class ContextModule extends AbstractModule {

    private final ExecutionContext context;

    public ContextModule(ExecutionContext context) {
        this.context = context;
    }

    @Override
    protected void configure() {

    }

    @Provides
    ExecutionContext context() {
        return context;
    }
}
