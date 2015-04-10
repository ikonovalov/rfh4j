package ru.codeunited.wmq.messaging;

import java.util.Properties;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class ConnectionOptions {

    private final String queueManagerName;

    private Properties options;

    public ConnectionOptions(String queueManagerName) {
        this.queueManagerName = queueManagerName;
    }

    public String getQueueManagerName() {
        return queueManagerName;
    }

    public Properties getOptions() {
        return options;
    }

    public ConnectionOptions withOptions(Properties options) {
        this.options = options;
        return this;
    }
}
