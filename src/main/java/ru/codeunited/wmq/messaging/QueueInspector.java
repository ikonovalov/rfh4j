package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public interface QueueInspector {

    int depth() throws MQException;

    int maxDepth() throws MQException;

    int openInputCount() throws MQException;

    int opentOutputCount() throws MQException;
}
