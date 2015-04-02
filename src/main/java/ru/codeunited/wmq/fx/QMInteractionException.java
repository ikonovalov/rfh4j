package ru.codeunited.wmq.fx;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class QMInteractionException extends Exception {
    public QMInteractionException(String message) {
        super(message);
    }

    public QMInteractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public QMInteractionException(Throwable cause) {
        super(cause);
    }

    public QMInteractionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
