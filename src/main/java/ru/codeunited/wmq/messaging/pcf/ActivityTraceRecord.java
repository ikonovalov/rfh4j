package ru.codeunited.wmq.messaging.pcf;

import java.util.Date;

/**
 * Represent individual operation trace records. Such as MQPUT, MQINQ and so on.
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public interface ActivityTraceRecord {

    /**
     * Returns operation type as enum.
     * @return
     */
    MQXFOperation getOperation();

    /**
     * Get operation code as-is.
     * @return
     */
    Integer getOperationAsInt();

    Integer getThreadId();

    Date getOperationDate();

    String getOperationDateISO();

    String getCompCode();

    Integer getCompCodeAsInt();

    /**
     * Returns true if record operation reason code is MQCC_OK.
     * @return
     */
    boolean isSuccess();

    Integer getReasonCode();
}
