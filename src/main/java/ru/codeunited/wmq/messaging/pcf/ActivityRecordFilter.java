package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 30.03.15.
 */
public interface ActivityRecordFilter {
    boolean allowed(ActivityTraceRecord record);
}
