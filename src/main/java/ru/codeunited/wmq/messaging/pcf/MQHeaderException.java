package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 15.04.15.
 */
public class MQHeaderException extends Exception {
    public MQHeaderException(String message) {
        super(message);
    }

    public MQHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQHeaderException(Throwable cause) {
        super(cause);
    }

    public MQHeaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
