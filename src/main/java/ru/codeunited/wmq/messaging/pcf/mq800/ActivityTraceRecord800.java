package ru.codeunited.wmq.messaging.pcf.mq800;

import com.ibm.mq.pcf.MQCFGR;
import ru.codeunited.wmq.messaging.pcf.mq750.ActivityTraceRecord750;

/**
 * http://www-01.ibm.com/support/knowledgecenter/SSFKSJ_8.0.0/com.ibm.mq.mon.doc/q037630_.htm
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class ActivityTraceRecord800 extends ActivityTraceRecord750 {

    protected ActivityTraceRecord800(MQCFGR parameter) {
        super(parameter);
    }
}
