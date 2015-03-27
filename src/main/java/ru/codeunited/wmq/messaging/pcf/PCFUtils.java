package ru.codeunited.wmq.messaging.pcf;

import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.MQCFBS;
import com.ibm.mq.pcf.PCFContent;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFParameter;
import org.apache.commons.lang3.StringUtils;
import ru.codeunited.wmq.messaging.pcf.mq750.ActivityTraceCommand750;
import ru.codeunited.wmq.messaging.pcf.mq800.ActivityTraceCommand800;

import java.nio.charset.Charset;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public final class PCFUtils {

    private PCFUtils() {

    }

    /**
     * Returns parameter value as string and in special cases it can lookup some of predefined constants values;
     * @param pcfParameter
     * @return
     */
    public static String decodeValue(final PCFParameter pcfParameter) {
        if (pcfParameter == null)
            return "";
        final int code = pcfParameter.getParameter();
        final Object value = pcfParameter.getValue();
        switch (code) { //http://www-01.ibm.com/support/knowledgecenter/SSFKSJ_7.5.0/com.ibm.mq.ref.dev.doc/q090210_.htm
            case MQIACF_OPERATION_ID:
                return MQConstants.lookup(value, "MQXF_.*");
            case MQIACF_COMP_CODE:
                return MQConstants.lookupCompCode((Integer) value);
            case MQIACF_REASON_CODE:
                return MQConstants.lookupReasonCode((Integer) value);
            case MQIA_PLATFORM:
                return MQConstants.lookup(value, "MQPL_.*");
            case MQIA_APPL_TYPE:
                return MQConstants.lookup(value, "MQAT_.*");
            case MQIACH_CHANNEL_TYPE:
                return MQConstants.lookup(value, "MQCHT_.*");
            case MQBACF_MESSAGE_DATA:
                return new String(((MQCFBS) pcfParameter).getString(), Charset.forName("UTF-8"));
            case MQIACF_RESOLVED_TYPE:
                return MQConstants.lookup(value, "MQOT_.*");
            case MQIACF_OBJECT_TYPE:
                return MQConstants.lookup(value, "MQOT_.*");
            case MQIACF_MSG_TYPE:
                return MQConstants.lookup(value, "MQMT_.*");
            default:
                return pcfParameter.getStringValue();
        }
    }

    public static String coalesce(PCFContent content, int... codes) {
        if (codes == null || codes.length == 0) return null;
        for (int code : codes) {
            String midRes = decodedParameter(content, code);
            if (StringUtils.isNotBlank(midRes)) {
                return midRes;
            }
        }
        return "";
    }

    public static PCFParameter parameterOf(PCFContent content, int paramCode) {
        return content.getParameter(paramCode);
    }

    public static String decodedParameter(PCFContent content, int paramCode) {
        String value = decodeValue(parameterOf(content, paramCode));
        if (value != null)
            value = value.trim();
        return value;
    }

    public static ActivityTraceCommand activityCommandFor(PCFMessage message) {
        String commandLevel = decodedParameter(message, MQIA_COMMAND_LEVEL);
        switch(commandLevel) { /* this is WMQ version switcher */
            case "750":
                return new ActivityTraceCommand750(message);
            case "800":
                return new ActivityTraceCommand800(message);
            default:
                throw new WrongTypeException("Unsupported command level for an activity command. Available 750, 800 but got ");
        }
    }
}
