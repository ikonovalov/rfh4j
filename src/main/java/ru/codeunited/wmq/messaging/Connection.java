package ru.codeunited.wmq.messaging;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public interface Connection {

    ConnectionOptions getOptions();

    QueueManager getManager();

}
