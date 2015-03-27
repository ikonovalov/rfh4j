package ru.codeunited.wmq.messaging.pcf.mq800;

import com.ibm.mq.pcf.MQCFGR;
import com.ibm.mq.pcf.PCFParameter;
import ru.codeunited.wmq.messaging.pcf.ActivityTraceRecord;
import ru.codeunited.wmq.messaging.pcf.MQXFOperations;
import ru.codeunited.wmq.messaging.pcf.mq750.ActivityTraceRecord750;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * http://www-01.ibm.com/support/knowledgecenter/SSFKSJ_8.0.0/com.ibm.mq.mon.doc/q037630_.htm
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class ActivityTraceRecord800 extends ActivityTraceRecord750 {

    static ActivityTraceRecord create(PCFParameter parameter) {
        return create((MQCFGR) parameter);
    }

    static ActivityTraceRecord create(MQCFGR parameter) {
        Integer operation = (Integer) parameter.getParameter(MQIACF_OPERATION_ID).getValue();
        MQXFOperations operationEnum = MQXFOperations.lookup(operation);
        switch (operationEnum) {
            case MQXF_PUT:
                return new MQXFPutRecord800(parameter);
            case MQXF_GET:
                return new MQXFGetRecord800(parameter);
            default:
                return new ActivityTraceRecord800(parameter);
        }
    }

    protected ActivityTraceRecord800(MQCFGR parameter) {
        super(parameter);
    }
}
