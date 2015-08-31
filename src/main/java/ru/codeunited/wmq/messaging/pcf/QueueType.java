package ru.codeunited.wmq.messaging.pcf;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 31.08.15.
 */
public enum QueueType {
    ALL(MQQT_ALL),
    ALIAS(MQQT_ALIAS),
    MODEL(MQQT_MODEL),
    CLUSTER(MQQT_CLUSTER),
    LOCAL(MQQT_LOCAL),
    REMOTE(MQQT_REMOTE);

    private final int code;

    QueueType(int code) {
        this.code = code;
    }


    public int code() {
        return code;
    }
}
