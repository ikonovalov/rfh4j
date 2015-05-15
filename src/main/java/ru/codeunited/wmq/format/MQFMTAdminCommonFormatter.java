package ru.codeunited.wmq.format;

import com.ibm.mq.MQMessage;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.MQCFBS;
import com.ibm.mq.pcf.MQCFGR;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFParameter;

import javax.inject.Singleton;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;

import static com.ibm.mq.constants.MQConstants.*;
import static ru.codeunited.wmq.messaging.MessageTools.bytesToHex;
import static ru.codeunited.wmq.messaging.pcf.PCFUtilService.decodeValue;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
@Singleton
public class MQFMTAdminCommonFormatter extends MQPCFMessageAbstractFormatter<String> {

    private static final int DEFAULT_OUTPUT_BUFFER_SZ = 128;

    public MQFMTAdminCommonFormatter() {
        super();
    }

    private void boarder(final StringBuffer buffer) {
        buffer.append("<--------------MQFTM_ADMIN------------------------>").append('\n');
    }

    @SuppressWarnings("unchecked")
    @Override
    public String format(PCFMessage pcfMessage, MQMessage mqMessage) {

        int paramCount = pcfMessage.getParameterCount();

        final StringBuffer buffer = new StringBuffer(DEFAULT_OUTPUT_BUFFER_SZ * paramCount);

        // print MQFTM_ADMIN
        boarder(buffer);

        buffer.append(String.format("Command: %d\n", pcfMessage.getCommand()));
        buffer.append(String.format("Parameters count: %d\n", paramCount));
        buffer.append(String.format("Correlation ID: %s\n", bytesToHex(mqMessage.correlationId)));
        buffer.append(String.format("Sequence number %s\n", decodeValue(pcfMessage.getParameter(MQIACF_SEQUENCE_NUMBER))));

        final Enumeration<PCFParameter> parametersEnum = pcfMessage.getParameters();
        final StringBuffer parametersBuffer = formatParameters(parametersEnum, 1);
        buffer.append(parametersBuffer);

        return buffer.toString();
    }

    private String depthStringOffset(int depth) {
        char[] offset = new char[depth];
        Arrays.fill(offset, '\t');
        return new String(offset);
    }

    private StringBuffer formatParameters(Enumeration<PCFParameter> parameters, int depth) {
        final String offset = depthStringOffset(depth);
        final StringBuffer buffer = new StringBuffer(DEFAULT_OUTPUT_BUFFER_SZ);
        int pIndex = 0;
        while(parameters.hasMoreElements()) {
            PCFParameter pcfParameter = parameters.nextElement(); // MQGACF_ACTIVITY_TRACE,  "MQI Operation"
            final int paramCode = pcfParameter.getParameter();
            switch (paramCode) { // resolve grouped parameters
                case MQGACF_ACTIVITY_TRACE: // activity trace
                    final MQCFGR parameterGroup = (MQCFGR) pcfParameter;
                    pIndex = formatSingleParameter(offset, buffer, pIndex, parameterGroup);
                    final StringBuffer parameterGroupBuffer = formatParameters(
                            (Enumeration<PCFParameter>) parameterGroup.getParameters(),
                            depth + 1
                    );
                    buffer.append(parameterGroupBuffer);
                    break;
                default:
                    pIndex = formatSingleParameter(offset, buffer, pIndex, pcfParameter);

            }
        }
        return buffer;
    }

    private int formatSingleParameter(String offset, StringBuffer buffer, int pIndex, PCFParameter pcfParameter) {
        final String pName = pcfParameter.getParameterName();
        final String pStringValue = decodeValue(pcfParameter);
        buffer.append(String.format("%sP_%s:%d [%d][%s][%s]\n",
                offset, pcfParameter.getClass().getSimpleName(), pIndex++, pcfParameter.getParameter(), pName, pStringValue
        ));
        return pIndex;
    }
}
