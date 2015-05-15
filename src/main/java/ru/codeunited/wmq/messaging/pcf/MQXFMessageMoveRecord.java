package ru.codeunited.wmq.messaging.pcf;

import java.util.Date;

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

    /**
     * Returns glued PUT_DATE and PUT_TIME if exists and HighResolutionTime if not.
     * @return
     */
    Date getPutDateTime();

    String getPutDateTimeISO();

    /**
     * Can be null if TraceData=0 in a mqat.ini
     * @param <T>
     * @return
     */
    <T> T getDataRaw();

    TraceData getData();

    /**
     * The length of message data (in bytes) that is traced for this connection.
     * @return size of captured body.
     */
    Integer getTraceDataLength();

    boolean isTransmissionMessage();

    String getXMITMessageId();

    String getXMITCorrelId();

    String getXMITPutDate();

    String getXMITPutTime();

    String getXMITRemoteQueueName();

    String getXMITRemoteQueueMananger();
}
