package ru.codeunited.wmq.handler;

import com.ibm.mq.MQException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public interface MessageHandler<R> {

    R onMessage(MessageEvent messageEvent) throws NestedHandlerException;

}
