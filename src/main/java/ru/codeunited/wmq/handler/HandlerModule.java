package ru.codeunited.wmq.handler;

import com.google.inject.AbstractModule;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.04.15.
 */
public class HandlerModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(MessageHandler.class).annotatedWith(PrintStream.class).to(PrintStreamHandler.class);
        bind(MessageHandler.class).annotatedWith(BodyToFile.class).to(BodyToFileHandler.class);
        bind(MessageHandler.class).annotatedWith(Bypass.class).to(BypassHandler.class);

    }
}
