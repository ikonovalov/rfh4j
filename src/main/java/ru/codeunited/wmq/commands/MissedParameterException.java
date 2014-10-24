package ru.codeunited.wmq.commands;

import java.util.Arrays;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
public class MissedParameterException extends Exception {

    // sorted char array
    private char[] name;

    // sorted string array
    private String[] longName;

    public MissedParameterException(String... message) {
        super();
        if (message == null || message.length == 0)
            throw new IllegalArgumentException("Option longName can't be null or empty");
        longName = message;
        Arrays.sort(longName);
    }

    public MissedParameterException(char... p) {
        super();
        name = p;
        Arrays.sort(name);
    }

    @Override
    public String getMessage() {
        return "Option " + name + " " + (longName != null ? "--" + longName : "") + " are missed";
    }

    public char[] getSingleCharName() {
        return name;
    }

    public String[] getLongName() {
        return longName;
    }
}
