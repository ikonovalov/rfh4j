package ru.codeunited.wmq.commands;

/**
 * Created by ikonovalov on 22.10.14.
 */
public class CommandGeneralException extends Exception {
    public CommandGeneralException() {
    }

    public CommandGeneralException(String message) {
        super(message);
    }

    public CommandGeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandGeneralException(Throwable cause) {
        super(cause);
    }

    public CommandGeneralException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
