package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 27.03.15.
 */
public interface MQXFMessageMoveRecord extends ActivityTraceRecord {

    Long getHighResolutionTime();

    Integer getHObject();

    Integer getMessageLength();

    String getBodyAsString();

    String getObjectType();

    String getObjectName();

    String getObjectQueueManagerName();

    String getResolvedQueueName();

    String getResolvedQueueManagerName();

    String getResolvedLocalQueueName();

    String getResolvedLocalQueueManagerName();

    String getResolvedType();

    Integer getReport();

    Integer getMessageTypeAsInt();

    String getMessageType();

    Long getExpire();

    String getFormat();

    Integer getPriority();

    Integer getPersistence();

    Boolean isPersistence();

    String getMessageId();

    String getCorrelId();

    String getReplyToQ();

    String getReplyToQManager();

    Integer getCCSID();

    Integer getEncoding();

    String getPutDate();

    String getPutTime();

    boolean isTransmissionMessage();
}
