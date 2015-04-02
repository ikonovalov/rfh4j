package ru.codeunited.wmq.fx;

import com.ibm.mq.MQException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.ManagerInspectorImpl;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    //==============================================================

    private final StringProperty name;

    private ObservableList<QBean> queues;

    //==============================================================

    public QMBean(StringProperty qmname) {
        this.name = qmname;
    }

    public void afterConnect(final ExecutionContext context) throws QMInteractionException {
        reloadQueuesList(context);
    }

    public ObservableList<QBean> getQueues() {
        return queues;
    }

    /**
     * Update queues list for current queue manager.
     * @param context
     * @throws QMInteractionException
     */
    protected void reloadQueuesList(ExecutionContext context) throws QMInteractionException {
        try {
            ManagerInspector inspector = new ManagerInspectorImpl(context.getQueueManager());
            List<Queue> listOfQueues = inspector.listLocalQueues();
            queues = FXCollections.observableArrayList();
            for (Queue q : listOfQueues) {
                queues.add(new QBean(q));
            }
        } catch (MQException | IOException e) {
            throw new QMInteractionException("Queue lookup failure. ", e);
        }
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
