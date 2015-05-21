package ru.codeunited.wmq.messaging.pcf;

import java.util.Date;
import java.util.List;

/**
 * Application activity trace message.
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public interface ActivityTraceCommand {

    /**
     * Initialized with the ConnectionId of the application.
     * @return
     */
    String getCorrelationId();

    Date getStartDate();

    String getStartDateRaw();

    Date getEndDate();

    String getEndDateRaw();

    String getQueueManager();

    String getHost();

    Integer getCommandLevel();

    Integer getSequenceNumber();

    String getApplicationName();

    String getApplicationType();

    Integer getProcessId();

    String getUserId();

    Integer getAPICallerType();

    Integer getAPIEnvironment();

    String getChannel();

    String getChannelType();

    String connectionName();

    /**
     * Get current activity trace level enabled into MQ
     * @return
     */
    ActivityTraceLevel getTraceDetail();

    String getPlatform();

    List<ActivityTraceRecord> getRecords();
}
