package ru.codeunited.wmq.fx.model;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import ru.codeunited.wmq.fx.FXExecutionContext;
import ru.codeunited.wmq.fx.QMInteractionException;

import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class QBeanStringConverter extends StringConverter<QueueBean> {

    private final ComboBox<QueueManagerBean> queueManagerListControl;

    public QBeanStringConverter(ComboBox<QueueManagerBean> queueManagerListControl) {
        this.queueManagerListControl = queueManagerListControl;
    }

    @Override
    public String toString(QueueBean object) {
        if (object == null)
            return null;
        return object.getName();
    }

    @Override
    public QueueBean fromString(final String string) {
        // perform search instead creation
        final QueueManagerBean qmgr = queueManagerListControl.getValue();
        final List<QueueBean> filtred = qmgr.getQueues().filtered(qBean -> qBean.getName().equals(string));
        if (filtred.size() > 0) {
            return filtred.get(0);
        } else {
            return null;
        }
    }
}
