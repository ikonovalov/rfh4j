package ru.codeunited.wmq.commands;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
public class ParameterException extends Exception {

    private char name;

    private String longName;

    public ParameterException(String message) {
        super();
        if (message == null || message.length() == 0)
            throw new IllegalArgumentException("Option longName can't be null or void");
        longName = message;
    }

    public ParameterException(char p) {
        super();
        name = p;
    }

    @Override
    public String getMessage() {
        return "Option " + name + " " + (longName != null ? "--" + longName : "") + " are missed";
    }

    public char getSingleCharName() {
        return name;
    }

    public String getLongName() {
        return longName;
    }
}
