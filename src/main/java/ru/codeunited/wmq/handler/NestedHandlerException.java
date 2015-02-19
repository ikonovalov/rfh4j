package ru.codeunited.wmq.handler;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class NestedHandlerException extends Exception {

    NestedHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    NestedHandlerException(Throwable cause) {
        super(cause);
    }

    static NestedHandlerException nest(Exception e) {
        return new NestedHandlerException(e);
    }
}
