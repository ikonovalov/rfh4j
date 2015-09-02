package ru.codeunited.wmq;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.codeunited.wmq.bus.DeadMessageListener;
import ru.codeunited.wmq.bus.MainBus;
import ru.codeunited.wmq.cli.ConsoleWriter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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

    @Provides @Singleton @MainBus
    EventBus mainEventBus() {
        Executor executor = Executors.newFixedThreadPool(10, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
        EventBus eventBus = new AsyncEventBus(RFH4J.class.getName(), executor);
        eventBus.register(new DeadMessageListener());
        return eventBus;
    }

}
