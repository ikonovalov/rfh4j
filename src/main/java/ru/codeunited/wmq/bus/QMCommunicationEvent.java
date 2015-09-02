package ru.codeunited.wmq.bus;

import ru.codeunited.wmq.messaging.MQLink;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.05.15.
 */
public interface QMCommunicationEvent {

    MQLink getLink();

}
