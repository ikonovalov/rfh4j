package ru.codeunited.wmq.messaging.pcf;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter for allowed operations of MQXF.
 */
public class ActivityRecordXFOperationFilter implements ActivityRecordFilter {

    private final Set<MQXFOperation> WHITE_LIST;

    public ActivityRecordXFOperationFilter(MQXFOperation... whiteOperations) {
        final Set<MQXFOperation> whiteList = new HashSet<>();
        if (whiteOperations != null)
            Collections.addAll(whiteList, whiteOperations);
        WHITE_LIST = Collections.unmodifiableSet(whiteList);
    }

    @Override
    public boolean allowed(ActivityTraceRecord checkIt) {
        return WHITE_LIST.contains(checkIt.getOperation());
    }

}
