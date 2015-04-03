package ru.codeunited.wmq.fx.controller;

import javafx.util.StringConverter;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.model.QueueManagerBean;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class QMBeanStringConverter extends StringConverter<QueueManagerBean> {

    public QMBeanStringConverter() {

    }

    @Override
    public String toString(QueueManagerBean object) {
        if (object == null)
            return null;
        return object.getName();
    }

    @Override
    public QueueManagerBean fromString(String string) {
        throw new IllegalStateException("Useless");
    }
}
