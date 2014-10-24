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
        // TODO bad output. Array toString needed!
        return "Option " + name + " " + (longName != null ? "--" + longName : "") + " are missed";
    }

    /**
     * Get array of missed single character parameters.
     * @return char[] or null if long named parameters used only.
     */
    public char[] getSingleCharName() {
        return name;
    }

    /**
     * Get array of missed long named parameters
     * @return String[] or null of used single character parameters.
     */
    public String[] getLongName() {
        return longName;
    }
}
