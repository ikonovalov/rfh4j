package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.03.15.
 */
public interface MQXFPutRecord extends MQXFMessageMoveRecord {

    Integer getPutOptions();

    Integer getRecsPresent();

    Integer getKnownDestCount();

    Integer getUnknownDestCount();

    Integer getInvalidDestCount();

}
