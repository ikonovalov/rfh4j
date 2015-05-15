package ru.codeunited.wmq.messaging;

import java.io.Closeable;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public interface MQLink extends Closeable {

    ConnectionOptions getOptions();

    QueueManager getManager();

    boolean isConnected();

}
