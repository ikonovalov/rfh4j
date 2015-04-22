package ru.codeunited.wmq.handler;

import javax.inject.Singleton;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
@Singleton
public class BypassHandler implements MessageHandler<MessageEvent> {

    @Override
    public MessageEvent onMessage(MessageEvent messageEvent) {
        return messageEvent;
    }
}
