package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public interface MessageInspector {

    int depth() throws MQException;

}
