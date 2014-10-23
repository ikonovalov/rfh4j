package ru.codeunited.wmq.commands;

/**
 * Workflow:
 * READY -> SKIPPED | EXECUTING -> SUCCESS | FAILED
 **/
public enum ReturnCode {


    /**
     * code 0
     */
    SUCCESS(0,"SUCCESS"),

    /**
     * code 1
     */
    SKIPPED(1, "SKIPPED"),

    /**
     * code 5
     */
    EXECUTING(5,"EXECUTING"),

    /**
     * code 10
     */
    READY(10, "READY"),

    /**
     * code -1 (negative)
     */
    FAILED(-1, "FAILED");

    private int code;

    private String shortDescription;

    ReturnCode(int code, String shortDescription) {
        this.code = code;
        this.shortDescription = shortDescription;
    }

    public int code() {
        return code;
    }

    public String shortDescription() {
        return shortDescription;
    }
}
