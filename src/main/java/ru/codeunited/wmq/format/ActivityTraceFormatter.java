package ru.codeunited.wmq.format;

import ru.codeunited.wmq.messaging.pcf.ActivityTraceCommand;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 31.03.15.
 */
public interface ActivityTraceFormatter<T> {

    T format(ActivityTraceCommand traceCommand);

}
