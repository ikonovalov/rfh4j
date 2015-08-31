package ru.codeunited.wmq.messaging.pcf;

import com.ibm.mq.pcf.PCFMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class PCFMessageWrapper extends PCFContentWrapper {

    protected final PCFMessage pcfMessage;

    public PCFMessageWrapper(PCFMessage pcfMessage) {
        super(pcfMessage);
        this.pcfMessage = pcfMessage;
        check();
    }

    /**
     * Implement this method only if you want to check incoming PCFMessage against you specific implementation.
     * Not all PCFMessages can be handled by your implementation.
     */
    protected void check() {

    }

}
