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

    Date getPutDateTime();

    /**
     * Get PUT_DATE and PUT_TIME glued together with ISO compatible format like 2015-05-12T14:16:50.
     * If PUT_DATE and PUT_TIME is empty it returns zeros like 0000-00-00T00:00:00.
     * @return
     */
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
