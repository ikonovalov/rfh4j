package ru.codeunited.wmq.handler;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public final class EventSource {

    private String sourceName;

    public EventSource(String sourceName) {
        this.sourceName = sourceName;
    }

    public final String getName() {
        return sourceName;
    }
}
