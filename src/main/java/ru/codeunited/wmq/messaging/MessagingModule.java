package ru.codeunited.wmq.messaging;

import com.google.inject.AbstractModule;
import ru.codeunited.wmq.messaging.impl.WMQDefaultConnectionFactory;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.04.15.
 */
public class MessagingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WMQConnectionFactory.class).to(WMQDefaultConnectionFactory.class);
    }
}
