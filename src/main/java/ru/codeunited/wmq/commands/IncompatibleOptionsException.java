package ru.codeunited.wmq.commands;

import java.util.Arrays;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 29.11.14.
 */
public class IncompatibleOptionsException extends Exception {

    private String[] incompatibleParams;

    public IncompatibleOptionsException(String message, String...incompatibleParams) {
        super(message);
        this.incompatibleParams = incompatibleParams;
    }

    public IncompatibleOptionsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "IncompatibleOptionsException{" +
                super.getMessage() +
                " incompatibleParams=" + Arrays.toString(incompatibleParams) +
                '}';
    }
}
