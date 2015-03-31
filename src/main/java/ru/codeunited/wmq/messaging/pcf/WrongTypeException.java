package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 25.03.15.
 */
public class WrongTypeException extends IllegalStateException {

    public WrongTypeException(String message) {
        super(message);
    }
}
