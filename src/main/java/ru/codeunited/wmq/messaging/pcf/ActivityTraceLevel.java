package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.04.15.
 */
public enum ActivityTraceLevel {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    UNKNOWN(-1);

    private int levelCode = -1;

    ActivityTraceLevel(int levelCode) {
        this.levelCode = levelCode;
    }

    public int code(){
        return levelCode;
    }

    public static ActivityTraceLevel forCode(final Integer code) {
        if (code == null)
            return UNKNOWN;
        switch (code) {
            case 1:
                return LOW;
            case 2:
                return MEDIUM;
            case 3:
                return HIGH;
            default:
                return UNKNOWN;
        }
    }
}
