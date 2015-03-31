package ru.codeunited.wmq.messaging.pcf;

import com.ibm.mq.pcf.MQCFGR;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class PCFGroupParameterWrapper extends PCFContentWrapper {

    public PCFGroupParameterWrapper(MQCFGR parameter) {
        super(parameter);
    }
}
