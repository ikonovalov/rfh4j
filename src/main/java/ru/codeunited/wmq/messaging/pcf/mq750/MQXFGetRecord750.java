package ru.codeunited.wmq.messaging.pcf.mq750;

import com.ibm.mq.pcf.MQCFGR;
import ru.codeunited.wmq.messaging.pcf.MQXFGetRecord;

import static com.ibm.mq.constants.CMQCFC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class MQXFGetRecord750 extends MQXFMessageMoveRecord750 implements MQXFGetRecord {


    public MQXFGetRecord750(MQCFGR parameter) {
        super(parameter);
    }

    @Override
    public Integer getGetOptions() {
        return decodedParameterAsInt(MQIACF_GET_OPTIONS);
    }

    @Override
    public Integer getBufferLength() {
        return decodedParameterAsInt(MQIACF_BUFFER_LENGTH);
    }




}
