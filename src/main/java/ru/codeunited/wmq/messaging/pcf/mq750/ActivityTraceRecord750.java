package ru.codeunited.wmq.messaging.pcf.mq750;

import com.ibm.mq.pcf.MQCFGR;
import com.ibm.mq.pcf.PCFParameter;
import ru.codeunited.wmq.messaging.pcf.ActivityTraceRecord;
import ru.codeunited.wmq.messaging.pcf.MQXFOperations;
import ru.codeunited.wmq.messaging.pcf.PCFGroupParameterWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * Basic class for a activity recodes MQCFGR-based.
 * http://www-01.ibm.com/support/knowledgecenter/SSFKSJ_7.5.0/com.ibm.mq.mon.doc/q037630_.htm
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class ActivityTraceRecord750 extends PCFGroupParameterWrapper implements ActivityTraceRecord {

    private static final SimpleDateFormat ISO_DATETIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    static ActivityTraceRecord create(PCFParameter parameter) {
        return create((MQCFGR) parameter);
    }

    static ActivityTraceRecord create(MQCFGR parameter) {
        Integer operation = (Integer) parameter.getParameter(MQIACF_OPERATION_ID).getValue();
        MQXFOperations operationEnum = MQXFOperations.lookup(operation);
        switch (operationEnum) {
            case MQXF_PUT:
                return new MQXFPutRecord750(parameter);
            case MQXF_GET:
                return new MQXFGetRecord750(parameter);
            default:
                return new ActivityTraceRecord750(parameter);
        }
    }

    protected ActivityTraceRecord750(MQCFGR parameter) {
        super(parameter);
    }

    @Override
    public MQXFOperations getOperation() {
        return MQXFOperations.lookup(decodedParameterAsInt(MQIACF_OPERATION_ID));
    }

    @Override
    public Integer getOperationAsInt() {
        return decodedParameterAsInt(MQIACF_OPERATION_ID);
    }

    @Override
    public Integer getThreadId() {
        return decodedParameterAsInt(MQIACF_THREAD_ID);
    }

    @Override
    public Date getOperationDate() {
        return createDateTime(MQCACF_OPERATION_DATE, MQCACF_OPERATION_TIME);
    }

    @Override
    public String getOperationDateISO() {
        return ISO_DATETIME.format(getOperationDate());
    }

    @Override
    public String getCompCode() {
        return decodedParameter(MQIACF_COMP_CODE);
    }

    @Override
    public Integer getCompCodeAsInt() {
        return decodedParameterAsInt(MQIACF_COMP_CODE);
    }

    @Override
    public boolean isSuccess() {
        return ((Integer) MQCC_OK).equals(decodedParameterAsInt(MQIACF_COMP_CODE));
    }

    @Override
    public Integer getReasonCode() {
        return decodedParameterAsInt(MQIACF_REASON_CODE);
    }
}
