package ru.codeunited.wmq.commands;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.10.14.
 */
public class MQConnectionException extends RuntimeException {
    public MQConnectionException(String message) {
        super(message);
    }

    public MQConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQConnectionException(Throwable cause) {
        super(cause);
    }
}
