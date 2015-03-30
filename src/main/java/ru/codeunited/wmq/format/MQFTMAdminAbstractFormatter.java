package ru.codeunited.wmq.format;

import com.ibm.mq.pcf.PCFMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.02.15.
 */
public abstract class MQFTMAdminAbstractFormatter<T> implements MessageConsoleFormatter<T> {

    protected final PCFMessage pcfMessage;

    protected MQFTMAdminAbstractFormatter(PCFMessage pcfMessage) {
        this.pcfMessage = pcfMessage;
    }
}
