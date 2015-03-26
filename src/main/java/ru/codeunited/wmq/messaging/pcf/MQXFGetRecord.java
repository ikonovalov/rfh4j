package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.03.15.
 */
public interface MQXFGetRecord extends MQXFMessageMoveRecord {

    Integer getGetOptions();

    Integer getBufferLength();

}
