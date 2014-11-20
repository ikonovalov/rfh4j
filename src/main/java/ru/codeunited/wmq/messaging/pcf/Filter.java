package ru.codeunited.wmq.messaging.pcf;

import static com.ibm.mq.constants.CMQCFC.MQCMD_INQUIRE_Q_NAMES;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 20.11.14.
 */
public enum Filter {
    QUEUE(MQCMD_INQUIRE_Q_NAMES);

    private int object;

    Filter(int object) {
        this.object = object;
    }

    public int object() {
        return object;
    }
}
