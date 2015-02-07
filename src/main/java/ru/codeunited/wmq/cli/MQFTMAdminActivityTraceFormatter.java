package ru.codeunited.wmq.cli;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public class MQFTMAdminActivityTraceFormatter implements MessageConsoleFormatter {

    private final PCFMessage message;

    MQFTMAdminActivityTraceFormatter(PCFMessage pcfMessage) {
        this.message = pcfMessage;
    }

    static interface Filter {
        boolean allowed(PCFParameter code);
    }

    static class OperationFilter implements Filter {

        private final Set<Integer> WHITE_LIST = new HashSet<>();
        {
            WHITE_LIST.add(MQXF_PUT);
            WHITE_LIST.add(MQXF_PUT1);
            WHITE_LIST.add(MQXF_GET);
            WHITE_LIST.add(MQXF_CMIT);
            WHITE_LIST.add(MQXF_BACK);
        }

        OperationFilter() {

        }

        @Override
        public boolean allowed(PCFParameter checkIt) {
            return WHITE_LIST.contains(checkIt.getValue());
        }

    }

    private Filter operationFilter = new OperationFilter();


    @Override
    public String format(MQMessage message) throws IOException, MQException {
        final StringBuffer buffer = new StringBuffer();

        // print MQFTM_ADMIN
        final PCFMessage pcfMessage = this.message;
        if (pcfMessage.getCommand() != MQCMD_ACTIVITY_TRACE)
            return String.format("Can't handled with %s", MQFTMAdminActivityTraceFormatter.class.getName());

        buffer.append(String.format("[%1$s %2$s] QM:[%3$s]",
                valueOf(pcfMessage, MQCAMO_START_DATE),
                valueOf(pcfMessage, MQCAMO_START_TIME),
                valueOf(pcfMessage, MQCA_Q_MGR_NAME)
        ));

        // scan for MQGACF_ACTIVITY_TRACE
        Enumeration<PCFParameter> parametersL1 = pcfMessage.getParameters();
        while (parametersL1.hasMoreElements()) {
            final PCFParameter parameter = parametersL1.nextElement();

            // skip non activity trace records
            if (parameter.getParameter() != MQGACF_ACTIVITY_TRACE)
                continue;

            // process activity trace elements (ACTIVITY_TRACE is always grouped as MQCFGR)
            MQCFGR trace = (MQCFGR) parameter;

            final PCFParameter operation = trace.getParameter(MQIACF_OPERATION_ID);
            if (operationFilter.allowed(operation)) {
                String operationName = decodeValue(operation);
                buffer.append(String.format(" op:[%s]", operationName));
                if (Integer.valueOf(MQXF_PUT).equals(operation.getValue())) {
                    buffer.append(String.format(" obj:[%s]", valueOf(trace, MQCACF_OBJECT_NAME)));
                    buffer.append(String.format(" msgid:[%s]", valueOf(trace, MQBACF_MSG_ID)));
                    buffer.append(String.format(" corid:[%s]", valueOf(trace, MQBACF_CORREL_ID)));
                    buffer.append(String.format(" len:[%s]", valueOf(trace, MQIACF_MSG_LENGTH)));
                    buffer.append(String.format(" dat:[%s]", valueOf(trace, MQBACF_MESSAGE_DATA)));
                }
                buffer.append(';');
            }

        }

        /*final Enumeration<PCFParameter> parametersEnum = pcfMessage.getParameters();
        final StringBuffer parametersBuffer = formatParameters(parametersEnum, 1);
        buffer.append(parametersBuffer);*/

        return buffer.toString();
    }

    private String valueOf(PCFContent message, int paramCode) {
        String value = decodeValue(message.getParameter(paramCode));
        if (value != null)
            value = value.trim();
        return value;
    }

    private String decodeValue(PCFParameter pcfParameter) {
        final int code = pcfParameter.getParameter();
        final Object value = pcfParameter.getValue();
        switch (code) { //http://www-01.ibm.com/support/knowledgecenter/SSFKSJ_7.5.0/com.ibm.mq.ref.dev.doc/q090210_.htm
            case MQIACF_OPERATION_ID:
                return MQConstants.lookup(value, "MQXF_.*");
            case MQIACF_COMP_CODE:
                return MQConstants.lookup(value, "MQCC_.*");
            case MQIA_PLATFORM:
                return MQConstants.lookup(value, "MQPL_.*");
            case MQIA_APPL_TYPE:
                return MQConstants.lookup(value, "MQAT_.*");
            case MQBACF_MESSAGE_DATA:
                return new String(((MQCFBS) pcfParameter).getString(), Charset.forName("UTF-8"));
            default:
                return pcfParameter.getStringValue();
        }

    }
}
