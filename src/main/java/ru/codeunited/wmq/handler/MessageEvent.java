package ru.codeunited.wmq.handler;

import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.messaging.pcf.MQXFOperation;

import static ru.codeunited.wmq.messaging.MessageTools.bytesToHex;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class MessageEvent {

    private int messageIndex = 0;

    private MQMessage message;

    private MQXFOperation operation = MQXFOperation.MQXF_UNKNOWN;

    private final EventSource eventSource;

    public MessageEvent(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    public EventSource getEventSource() {
        return eventSource;
    }

    public byte[] getMessageId() {
        return message.messageId;
    }

    public String getHexMessageId() {
        return bytesToHex(getMessageId());
    }

    public byte[] getCorrelationId(){
        return message.correlationId;
    }

    public String getHexCorrelationId() {
        return bytesToHex(getCorrelationId());
    }

    public int getMessageIndex() {
        return messageIndex;
    }

    public String getMessageFormat() {
        return message.format;
    }

    public void setMessageIndex(int messageIndex) {
        this.messageIndex = messageIndex;
    }

    public MQMessage getMessage() {
        return message;
    }

    public void setMessage(MQMessage message) {
        checkAlreadySet(this.message, "MQMessage already set");
        this.message = message;
    }

    public void setOperation(MQXFOperation operation) {
        this.operation = operation;
    }

    public MQXFOperation getOperation() {
        return operation;
    }

    private void checkAlreadySet(Object object, String raiseWithMessage) {
        if (object != null)
            throw new IllegalStateException(raiseWithMessage);
    }
}
