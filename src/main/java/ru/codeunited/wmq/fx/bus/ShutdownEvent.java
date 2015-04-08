package ru.codeunited.wmq.fx.bus;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 08.04.15.
 */
public final class ShutdownEvent implements BusEvent {

    private final Object source;

    public ShutdownEvent(Object source) {
        this.source = source;
    }
}
