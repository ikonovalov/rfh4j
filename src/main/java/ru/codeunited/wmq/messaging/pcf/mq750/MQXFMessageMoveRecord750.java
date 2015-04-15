package ru.codeunited.wmq.messaging.pcf.mq750;

import com.ibm.mq.pcf.MQCFGR;
import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.codeunited.wmq.messaging.pcf.MQXFMessageMoveRecord;
import ru.codeunited.wmq.messaging.pcf.TraceData;
import ru.codeunited.wmq.messaging.pcf.TraceDataImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.ibm.mq.constants.CMQC.MQFMT_XMIT_Q_HEADER;
import static com.ibm.mq.constants.CMQC.MQIA_CODED_CHAR_SET_ID;
import static com.ibm.mq.constants.CMQCFC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.03.15.
 */
public abstract class MQXFMessageMoveRecord750 extends ActivityTraceRecord750 implements MQXFMessageMoveRecord {

    private static final SimpleDateFormat TIME_FORMAT;

    private static final SimpleDateFormat TIME_REFORMATED;

    static {
        TIME_FORMAT = new SimpleDateFormat("yyyyMMdd HHmmssSS");
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));

        TIME_REFORMATED = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        TIME_REFORMATED.setTimeZone(TimeZone.getDefault());
    }

    protected MQXFMessageMoveRecord750(MQCFGR parameter) {
        super(parameter);
    }

    @Override
    public Integer getHObject() {
        return decodedParameterAsInt(MQIACF_REASON_CODE);
    }

    @Override
    public String getObjectType() {
        return decodedParameter(MQIACF_OBJECT_TYPE);
    }

    @Override
    public String getObjectName() {
        return decodedParameter(MQCACF_OBJECT_NAME);
    }

    @Override
    public String getObjectQueueManagerName() {
        return decodedParameter(MQCACF_OBJECT_Q_MGR_NAME);
    }

    @Override
    public String getResolvedQueueName() {
        return decodedParameter(MQCACF_RESOLVED_Q_NAME);
    }

    @Override
    public String getResolvedQueueManagerName() {
        return decodedParameter(MQCACF_RESOLVED_Q_MGR);
    }

    @Override
    public String getResolvedLocalQueueName() {
        return decodedParameter(MQCACF_RESOLVED_LOCAL_Q_NAME);
    }

    @Override
    public String getResolvedLocalQueueManagerName() {
        return decodedParameter(MQCACF_RESOLVED_LOCAL_Q_MGR);
    }

    @Override
    public String getResolvedType() {
        return decodedParameter(MQIACF_RESOLVED_TYPE);
    }

    @Override
    public Integer getReport() {
        return decodedParameterAsInt(MQIACF_MSG_TYPE);
    }

    @Override
    public Integer getMessageTypeAsInt() {
        return decodedParameterAsInt(MQIACF_MSG_TYPE);
    }

    @Override
    public String getMessageType() {
        return decodedParameter(MQIACF_MSG_TYPE);
    }

    @Override
    public Long getExpire() {
        return decodeParameterAsLong(MQIACF_EXPIRY);
    }

    @Override
    public String getFormat() {
        return (String) decodeParameterRaw(MQCACH_FORMAT_NAME);
    }

    @Override
    public Integer getPriority() {
        return decodedParameterAsInt(MQIACF_PRIORITY);
    }

    @Override
    public Integer getPersistence() {
        return decodedParameterAsInt(MQIACF_PERSISTENCE);
    }

    @Override
    public Boolean isPersistence() {
        return Integer.valueOf(1).equals(decodedParameterAsInt(MQIACF_PERSISTENCE));
    }

    @Override
    public String getMessageId() {
        return decodedParameter(MQBACF_MSG_ID);
    }

    @Override
    public String getCorrelId() {
        return decodedParameter(MQBACF_CORREL_ID);
    }

    @Override
    public String getReplyToQ() {
        return decodedParameter(MQCACF_REPLY_TO_Q);
    }

    @Override
    public String getReplyToQManager() {
        return decodedParameter(MQCACF_REPLY_TO_Q_MGR);
    }

    @Override
    public Integer getCCSID() {
        return decodedParameterAsInt(MQIA_CODED_CHAR_SET_ID);
    }

    @Override
    public Integer getEncoding() {
        return decodedParameterAsInt(MQIACF_ENCODING);
    }

    @Override
    public String getPutDate() {
        return decodedParameter(MQCACF_PUT_DATE);
    }

    @Override
    public String getPutTime() {
        return decodedParameter(MQCACF_PUT_TIME);
    }

    @Override
    public Date getPutDateTime() {
        final String gluedPutDateTime = getPutDate() + ' ' + getPutTime();
        try {
            return TIME_FORMAT.parse(gluedPutDateTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String getPutDateTimeISO() {
        return TIME_REFORMATED.format(getPutDateTime());
    }

    @Override
    public Long getHighResolutionTime() {
        return Long.valueOf(decodedParameter(MQIAMO64_HIGHRES_TIME));
    }

    @Override
    public Integer getMessageLength() {
        return decodedParameterAsInt(MQIACF_MSG_LENGTH);
    }

    @Override
    public String getBodyAsString() {
        return decodedParameter(MQBACF_MESSAGE_DATA);
    }

    @Override
    public <T> T getDataRaw() {
        return (T) decodeParameterRaw(MQBACF_MESSAGE_DATA);
    }

    @Override
    public Integer getTraceDataLength() {
        return decodedParameterAsInt(MQIACF_TRACE_DATA_LENGTH);
    }

    @Override
    public TraceData getData() {
        TraceData data = TraceDataImpl.create(this);
        return data;
    }

    @Override
    public boolean isTransmissionMessage() {
        return MQFMT_XMIT_Q_HEADER.equals(getFormat());
    }

    @Override
    public String getXMITMessageId() {
        return decodedParameter(MQBACF_XQH_MSG_ID);
    }

    @Override
    public String getXMITCorrelId() {
        return decodedParameter(MQBACF_XQH_CORREL_ID);
    }

    @Override
    public String getXMITPutDate() {
        return decodedParameter(MQCACF_XQH_PUT_DATE);
    }

    @Override
    public String getXMITPutTime() {
        return decodedParameter(MQCACF_XQH_PUT_TIME);
    }

    @Override
    public String getXMITRemoteQueueName() {
        return decodedParameter(MQCACF_XQH_REMOTE_Q_NAME);
    }

    @Override
    public String getXMITRemoteQueueMananger() {
        return decodedParameter(MQCACF_XQH_REMOTE_Q_MGR);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("messageId", getMessageId())
                .append("length", getMessageLength())
                .append("objectName", getObjectName())
                .appendSuper(super.toString())
                .toString();
    }
}
