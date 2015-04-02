package ru.codeunited.wmq.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class QBean {

    private final StringProperty name;

    private final Queue queue;

    private final QMBean parent;

    private final ExecutionContext context;

    public QBean(QMBean parent, Queue queue, ExecutionContext context) {
        this.name = new SimpleStringProperty(queue.getName());
        this.context = context;
        this.queue = queue;
        this.parent = parent;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String info() {
        return String.format("Cap [%d/%d], In/Out [%d, %d]",
                queue.getDepth(),
                queue.getMaxDepth(),
                queue.getInputCount(),
                queue.getOutputCount()
        );
    }

    public static class QBeanStringConverter extends StringConverter<QBean> {

        private RFHFX application;

        public QBeanStringConverter(RFHFX application) {
            this.application = application;
        }

        @Override
        public String toString(QBean object) {
            if (object == null)
                return null;
            return object.getName();
        }

        @Override
        public QBean fromString(final String string) {
            try {
                // perform search instead creation
                final MainTab tab = application.mainTabView();
                final QMBean qmgr = tab.getCurrentQueueManager();
                final List<QBean> filtred = qmgr.getQueues().filtered(qBean -> qBean.getName().equals(string));
                if (filtred.size() > 0) {
                    return filtred.get(0);
                } else {
                    return null;
                }
            } catch (QMInteractionException e) {
                return null;
            }
        }
    }

}
