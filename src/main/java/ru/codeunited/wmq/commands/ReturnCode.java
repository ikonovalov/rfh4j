package ru.codeunited.wmq.commands;

/**
 * Created by ikonovalov on 22.10.14.
 */
public enum ReturnCode {

    /**
     * Workflow:
     * READY -> SKIPPED | EXECUTING -> SUCCESS | FAILED
    **/
    SUCCESS(0,"SUCCESS"),
    SKIPPED(1, "SKIPPED"),
    EXECUTING(5,"EXECUTING"),
    READY(10, "READY"),
    FAILED(-1, "FAILED");

    private int code;

    private String shortDescription;

    ReturnCode(int code, String shortDescription) {
        this.code = code;
        this.shortDescription = shortDescription;
    }

    public int getCode() {
        return code;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
