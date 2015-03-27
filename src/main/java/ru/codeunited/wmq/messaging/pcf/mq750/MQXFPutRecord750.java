package ru.codeunited.wmq.messaging.pcf.mq750;

import com.ibm.mq.pcf.MQCFGR;
import ru.codeunited.wmq.messaging.pcf.MQXFPutRecord;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class MQXFPutRecord750 extends MQXFMessageMoveRecord750 implements MQXFPutRecord {

    protected MQXFPutRecord750(MQCFGR parameter) {
        super(parameter);
    }

    @Override
    public Integer getPutOptions() {
        return decodedParameterAsInt(MQIACF_PUT_OPTIONS);
    }

    @Override
    public Integer getRecsPresent() {
        return decodedParameterAsInt(MQIACF_RECS_PRESENT);
    }

    @Override
    public Integer getKnownDestCount() {
        return decodedParameterAsInt(MQIACF_KNOWN_DEST_COUNT);
    }

    @Override
    public Integer getUnknownDestCount() {
        return decodedParameterAsInt(MQIACF_UNKNOWN_DEST_COUNT);
    }

    @Override
    public Integer getInvalidDestCount() {
        return decodedParameterAsInt(MQIACF_INVALID_DEST_COUNT);
    }
}
