package ru.codeunited.wmq.messaging.pcf.mq750;

import com.ibm.mq.pcf.MQCFGR;

import static com.ibm.mq.constants.CMQC.MQFMT_XMIT_Q_HEADER;
import static com.ibm.mq.constants.CMQC.MQIA_CODED_CHAR_SET_ID;
import static com.ibm.mq.constants.CMQCFC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.03.15.
 */
public abstract class MQXFMessageMoveRecord750 extends ActivityTraceRecord750 {

    protected MQXFMessageMoveRecord750(MQCFGR parameter) {
        super(parameter);
    }

    public Integer getHObject() {
        return decodedParameterAsInt(MQIACF_REASON_CODE);
    }

    public String getObjectType() {
        return decodedParameter(MQIACF_OBJECT_TYPE);
    }

    public String getObjectName() {
        return decodedParameter(MQCACF_OBJECT_NAME);
    }

    public String getObjectQueueManagerName() {
        return decodedParameter(MQCACF_OBJECT_Q_MGR_NAME);
    }

    public String getResolvedQueueName() {
        return decodedParameter(MQCACF_RESOLVED_Q_NAME);
    }

    public String getResolvedQueueManagerName() {
        return decodedParameter(MQCACF_RESOLVED_Q_MGR);
    }

    public String getResolvedLocalQueueName() {
        return decodedParameter(MQCACF_RESOLVED_LOCAL_Q_NAME);
    }

    public String getResolvedLocalQueueManagerName() {
        return decodedParameter(MQCACF_RESOLVED_LOCAL_Q_MGR);
    }

    public String getResolvedType() {
        return decodedParameter(MQIACF_RESOLVED_TYPE);
    }

    public Integer getReport() {
        return decodedParameterAsInt(MQIACF_MSG_TYPE);
    }

    public Integer getMessageTypeAsInt() {
        return decodedParameterAsInt(MQIACF_MSG_TYPE);
    }

    public String getMessageType() {
        return decodedParameter(MQIACF_MSG_TYPE);
    }

    public Long getExpire() {
        return decodeParameterAsLong(MQIACF_EXPIRY);
    }

    public String getFormat() {
        return decodedParameter(MQCACH_FORMAT_NAME);
    }

    public Integer getPriority() {
        return decodedParameterAsInt(MQIACF_PRIORITY);
    }

    public Integer getPersistence() {
        return decodedParameterAsInt(MQIACF_PERSISTENCE);
    }

    public Boolean isPersistence() {
        return Integer.valueOf(1).equals(decodedParameterAsInt(MQIACF_PERSISTENCE));
    }

    public String getMessageId() {
        return decodedParameter(MQBACF_MSG_ID);
    }

    public String getCorrelId() {
        return decodedParameter(MQBACF_CORREL_ID);
    }

    public String getReplyToQ() {
        return decodedParameter(MQCACF_REPLY_TO_Q);
    }

    public String getReplyToQManager() {
        return decodedParameter(MQCACF_REPLY_TO_Q_MGR);
    }

    public Integer getCCSID() {
        return decodedParameterAsInt(MQIA_CODED_CHAR_SET_ID);
    }

    public Integer getEncoding() {
        return decodedParameterAsInt(MQIACF_ENCODING);
    }

    public String getPutDate() {
        return decodedParameter(MQCACF_PUT_DATE);
    }

    public String getPutTime() {
        return decodedParameter(MQCACF_PUT_TIME);
    }

    public Long getHighResolutionTime() {
        return Long.valueOf(decodedParameter(MQIAMO64_HIGHRES_TIME));
    }

    public Integer getMessageLength() {
        return decodedParameterAsInt(MQIACF_MSG_LENGTH);
    }

    public String getBodyAsString() {
        return decodedParameter(MQBACF_MESSAGE_DATA);
    }

    public boolean isTransmissionMessage() {
        return MQFMT_XMIT_Q_HEADER.equals(decodedParameter(MQCACH_FORMAT_NAME));
    }

    public String getXMITMessageId() {
        return decodedParameter(MQBACF_XQH_MSG_ID);
    }

    public String getXMITCorrelId() {
        return decodedParameter(MQBACF_XQH_CORREL_ID);
    }

    public String getXMITPutDate() {
        return decodedParameter(MQCACF_XQH_PUT_DATE);
    }

    public String getXMITPutTime() {
        return decodedParameter(MQCACF_XQH_PUT_TIME);
    }

    public String getXMITRemoteQueueName() {
        return decodedParameter(MQCACF_XQH_REMOTE_Q_NAME);
    }

    public String getXMITRemoteQueueMananger() {
        return decodedParameter(MQCACF_XQH_REMOTE_Q_MGR);
    }
}
