package ru.codeunited.wmq.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class QMBean {

    private final StringProperty name;

    public QMBean(StringProperty qmname) {
        this.name = qmname;
    }

    public QMBean(String qmname) {
        this.name = new SimpleStringProperty(qmname);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
