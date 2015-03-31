package ru.codeunited.wmq.messaging.pcf;

import static com.ibm.mq.constants.MQConstants.*;
import com.ibm.mq.pcf.PCFMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class PCFMessageWrapper extends PCFContentWrapper {

    protected final PCFMessage pcfMessage;

    private String format = "";

    private byte[] correlationId = MQMI_NONE;

    protected PCFMessageWrapper(PCFMessage pcfMessage) {
        super(pcfMessage);
        this.pcfMessage = pcfMessage;
        check();
    }

    protected String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    protected byte[] getCorrelationIdBytes() {
        return correlationId;
    }

    public void setCorrelationIdBytes(byte[] correlationId) {
        this.correlationId = correlationId;
    }

    protected void check() {

    }

}
