package ru.codeunited.wmq.messaging.pcf;

import com.ibm.mq.pcf.PCFContent;
import com.ibm.mq.pcf.PCFParameter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class PCFContentWrapper {

    protected final PCFContent content;


    protected PCFContentWrapper(PCFContent content) {
        this.content = content;
    }

    protected String decodedParameter(int code) {
        return PCFUtilService.decodedParameter(content, code);
    }

    protected Integer decodedParameterAsInt(int code) {
        try { /* optimistic way */
            return (Integer) decodeParameterRaw(code);
        } catch (ClassCastException cce) {
            return Integer.valueOf(decodedParameter(code));
        }
    }

    protected Long decodeParameterAsLong(int code) {
        try {
            return (Long) decodeParameterRaw(code);
        } catch (ClassCastException cce) {
            return Long.valueOf(decodedParameter(code));
        }
    }

    /**
     * Returns paramter value as-is without casting and convertations.
     * @param code
     * @return value as-is or null of where is not such parameter.
     */
    protected Object decodeParameterRaw(int code) {
        PCFParameter pcfParameter = PCFUtilService.parameterOf(content, code);
        return pcfParameter == null ? pcfParameter : PCFUtilService.parameterOf(content, code).getValue();
    }

    protected List<PCFParameter> getParamaters() {
        final List<PCFParameter> res = new ArrayList<>();
        final Enumeration<PCFParameter> allParametersEnum = content.getParameters();
        while (allParametersEnum.hasMoreElements()) {
            res.add(allParametersEnum.nextElement());
        }
        return res;
    }

    /**
     * Return only parameter of specified type.
     * @param filterCodes - required filter.
     * @return list of PCFParameter for specified type.
     */
    protected List<PCFParameter> getParamaters(Integer ... filterCodes) {
        List<PCFParameter> res = new ArrayList<>();
        List<Integer> filterCodesList = Arrays.asList(filterCodes);
        final Enumeration<PCFParameter> allParametersEnum = content.getParameters();
        while (allParametersEnum.hasMoreElements()) {
            PCFParameter parameter = allParametersEnum.nextElement();
            int parameterCode = parameter.getParameter();
            // skip non activity trace records
            if (filterCodesList.contains(parameterCode)) {
                res.add(parameter);
            }
        }
        return res;
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Create java.util.Date from separated parameters
     * @param dateCode - date code (like position in the structure)
     * @param timeCode - time code
     * @return aligned date with time
     */
    protected Date createDateTime(int dateCode, int timeCode) {
        String stDateTime = createDateTimeRaw(dateCode, timeCode);
        try {
            return DATE_FORMAT.parse(stDateTime);
        } catch (ParseException e) {
            return null;
        }
    }

    protected String createDateTimeRaw(int dateCode, int timeCode) {
        return decodedParameter(dateCode) + ' ' + decodedParameter(timeCode);
    }
}
