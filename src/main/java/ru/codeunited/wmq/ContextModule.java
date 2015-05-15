package ru.codeunited.wmq;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.codeunited.wmq.cli.ConsoleWriter;

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

    @Provides @Singleton
    ConsoleWriter consoleWriter() {
        return new ConsoleWriter(System.out, System.err);
    }

}
