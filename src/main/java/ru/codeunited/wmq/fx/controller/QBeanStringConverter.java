package ru.codeunited.wmq.fx.controller;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.model.MainTab;
import ru.codeunited.wmq.fx.model.QueueBean;
import ru.codeunited.wmq.fx.model.QueueManagerBean;

import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class QBeanStringConverter extends StringConverter<QueueBean> {

    private final RFHFX application;

    private final ComboBox<QueueManagerBean> queueManagerListControl;

    public QBeanStringConverter(RFHFX application, ComboBox<QueueManagerBean> queueManagerListControl) {
        this.application = application;
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
        try {
            // perform search instead creation
            final MainTab tab = application.mainTabView();
            final QueueManagerBean qmgr = queueManagerListControl.getValue();
            final List<QueueBean> filtred = qmgr.getQueues().filtered(qBean -> qBean.getName().equals(string));
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
