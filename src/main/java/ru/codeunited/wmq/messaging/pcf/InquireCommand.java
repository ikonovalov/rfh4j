package ru.codeunited.wmq.messaging.pcf;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * TODO Look at this http://www-01.ibm.com/support/knowledgecenter/SSFKSJ_7.5.0/com.ibm.mq.ref.adm.doc/q086870_.htm
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 20.11.14.
 */
public enum InquireCommand {
    QUEUE_DEF(MQCMD_INQUIRE_Q),
    QUEUE_NAMES(MQCMD_INQUIRE_Q_NAMES),
    QUEUE_STATUS(MQCMD_INQUIRE_Q_STATUS),
    QMGR(MQCMD_INQUIRE_Q_MGR),
    CHANNEL(MQCMD_INQUIRE_CHANNEL);

    private int commandCode;

    InquireCommand(int object) {
        this.commandCode = object;
    }

    public int commandCode() {
        return commandCode;
    }
}
