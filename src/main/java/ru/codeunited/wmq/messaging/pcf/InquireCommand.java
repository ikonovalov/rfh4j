package ru.codeunited.wmq.messaging.pcf;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 20.11.14.
 */
public enum InquireCommand {
    QUEUE(MQCMD_INQUIRE_Q_NAMES),
    QMGR(MQCMD_INQUIRE_Q_MGR),
    CHANNEL(MQCMD_INQUIRE_CHANNEL);

    private int object;

    InquireCommand(int object) {
        this.object = object;
    }

    public int object() {
        return object;
    }
}
