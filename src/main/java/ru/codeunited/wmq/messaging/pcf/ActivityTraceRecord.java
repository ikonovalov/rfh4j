package ru.codeunited.wmq.messaging.pcf;

import java.util.Date;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public interface ActivityTraceRecord {

    MQXFOperation getOperation();

    Integer getOperationAsInt();

    Integer getThreadId();

    Date getOperationDate();

    String getOperationDateISO();

    String getCompCode();

    Integer getCompCodeAsInt();

    boolean isSuccess();

    Integer getReasonCode();
}
