package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.03.15.
 */
public interface MQXFPutRecord extends ActivityTraceRecord {

    Long getHighResolutionTime();

    Integer getHObject();

    Integer getPutOptions();

    Integer getMessageLength();

    Integer getRecsPresent();

    Integer getKnownDestCount();

    Integer getUnknownDestCount();

    Integer getInvalidDestCount();

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
