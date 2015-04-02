package ru.codeunited.wmq.fx.model;

import com.ibm.mq.MQException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.handler.NestedHandlerException;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.ManagerInspectorImpl;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.io.IOException;
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
            throw new IllegalStateException("Useless");
        }
    }

    //==============================================================

    private final StringProperty name;

    private final ExecutionContext context;

    private ManagerInspector inspector;

    private ObservableList<QBean> queues;

    //==============================================================

    public QMBean(StringProperty qmname, ExecutionContext context) throws QMInteractionException {
        this.name = qmname;
        this.context = context;

    }

    public void afterConnect() throws QMInteractionException {
        try {
            this.inspector = new ManagerInspectorImpl(context.getQueueManager());
        } catch (MQException e) {
            throw new QMInteractionException("Inspector creation failure", e);
        }
        reloadQueuesList();

    }

    public ReturnCode connect() throws QMInteractionException {
        final CommandChain chain = new CommandChain(context);
        chain.addCommand(new MQConnectCommand());
        try {
            return chain.execute();
        } catch (CommandGeneralException | MissedParameterException | IncompatibleOptionsException | NestedHandlerException e) {
            throw  new QMInteractionException("Connect failed. ", e);
        }
    }

    public ObservableList<QBean> getQueues() {
        return queues;
    }

    /**
     * Update queues list for current queue manager.
     * @throws QMInteractionException
     */
    protected void reloadQueuesList() throws QMInteractionException {
        try {

            List<Queue> listOfQueues = inspector.listLocalQueues();
            queues = FXCollections.observableArrayList();
            for (Queue q : listOfQueues) {
                queues.add(new QBean(this, q, context));
            }
        } catch (MQException | IOException e) {
            throw new QMInteractionException("Queue lookup failure. ", e);
        }
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
