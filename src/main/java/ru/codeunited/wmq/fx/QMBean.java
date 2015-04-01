package ru.codeunited.wmq.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class QMBean {

    public static class QMBeanStringConverter extends StringConverter<QMBean> {
        @Override
        public String toString(QMBean object) {
            if (object == null)
                return null;
            return object.getName();
        }

        @Override
        public QMBean fromString(String string) {
            System.out.println("from String");
            return new QMBean(string);
        }
    }

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
