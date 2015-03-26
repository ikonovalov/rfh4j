package ru.codeunited.wmq.messaging.pcf;

import com.ibm.mq.pcf.PCFMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class PCFMessageWrapper extends PCFContentWrapper {

    protected final PCFMessage pcfMessage;

    protected PCFMessageWrapper(PCFMessage pcfMessage) {
        super(pcfMessage);
        this.pcfMessage = pcfMessage;
        check();
    }

    protected void check() {

    }

}
