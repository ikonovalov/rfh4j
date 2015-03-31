package ru.codeunited.wmq.format;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 27.03.15.
 */
public class CustomFormatterException extends RuntimeException {

    public CustomFormatterException() {
    }

    public CustomFormatterException(String message) {
        super(message);
    }

    public CustomFormatterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomFormatterException(Throwable cause) {
        super(cause);
    }
}
