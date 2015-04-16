package ru.codeunited.wmq.format;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 16.04.15.
 */
public class FormatterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageFormatter.class).annotatedWith(Names.named(MQFMTStringFormatter.class.getName())).to(MQFMTStringFormatter.class);
    }
}
