package ru.codeunited.wmq.handler;

import com.ibm.mq.MQMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class NopHandler implements MessageHandler<Void> {

    @Override
    public Void onMessage(MessageEvent messageEvent) {
        return null;
    }
}
