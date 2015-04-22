package ru.codeunited.wmq.messaging.impl;

import ru.codeunited.wmq.messaging.ConnectionOptions;
import ru.codeunited.wmq.messaging.MQLink;
import ru.codeunited.wmq.messaging.QueueManager;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 11.04.15.
 */
final class MQLinkImpl implements MQLink {

    private final ConnectionOptions options;

    private final QueueManager manager;

    MQLinkImpl(ConnectionOptions options, QueueManager manager) {
        this.options = options;
        this.manager = manager;
    }

    @Override
    public ConnectionOptions getOptions() {
        return options;
    }

    @Override
    public QueueManager getManager() {
        return manager;
    }

    @Override
    public void close() throws IOException {
        manager.close();
    }

    @Override
    public boolean isConnected() {
        return manager.isConnected();
    }
}
