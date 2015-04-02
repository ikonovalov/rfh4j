package ru.codeunited.wmq.fx;

import javafx.util.StringConverter;
import ru.codeunited.wmq.messaging.pcf.Queue;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class QBean {

    private final String name;

    public QBean(String name) {
        this.name = name;
    }

    public QBean(Queue queue) {
        this(queue.getName());
    }

    public String getName() {
        return name;
    }

    public static class QMBeanStringConverter extends StringConverter<QBean> {
        @Override
        public String toString(QBean object) {
            if (object == null)
                return null;
            return object.getName();
        }

        @Override
        public QBean fromString(String string) {
            System.out.println("from String");
            return new QBean(string);
        }
    }

}
