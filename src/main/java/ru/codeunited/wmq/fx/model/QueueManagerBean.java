package ru.codeunited.wmq.fx.model;

import com.ibm.mq.MQException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.Draft;
import ru.codeunited.wmq.handler.NestedHandlerException;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.ManagerInspectorImpl;
import ru.codeunited.wmq.messaging.pcf.Queue;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class QueueManagerBean {

    //==============================================================

    private final StringProperty name = new SimpleStringProperty();

    private ManagerInspector inspector;

    private ObservableList<QueueBean> queues = FXCollections.observableArrayList();

    @Inject private ExecutionContext context;

    @Inject @Draft
    private Provider<QueueBean> queueBeanProvider;

    //==============================================================
    public QueueManagerBean() {
        super();
    }

    public void setName(String name) {
        this.name.set(Objects.requireNonNull(name));
    }

    private void postConnectOperations() throws QMInteractionException {
        try {
            this.inspector = new ManagerInspectorImpl(context.getQueueManager());
        } catch (MQException e) {
            throw new QMInteractionException("Inspector creation failure", e);
        }
        reloadQueues();

    }

    public ReturnCode connect() throws QMInteractionException {
        final CommandChain chain = new CommandChain(context);
        chain.addCommand(new MQConnectCommand());
        try {
            ReturnCode rc =  chain.execute();
            if (rc == ReturnCode.SUCCESS) {
                postConnectOperations();
            }
            return rc;
        } catch (CommandGeneralException | MissedParameterException | IncompatibleOptionsException | NestedHandlerException e) {
            throw  new QMInteractionException("Connect failed. ", e);
        }
    }

    public ReturnCode disconnect() throws QMInteractionException {
        final CommandChain chain = new CommandChain(context);
        chain.addCommand(new MQDisconnectCommand());
        try {
            return chain.execute();
        } catch (CommandGeneralException | MissedParameterException | IncompatibleOptionsException | NestedHandlerException e) {
            throw  new QMInteractionException("Disconnect operation failed. ", e);
        }
    }

    public ObservableList<QueueBean> getQueues() {
        return queues;
    }

    /**
     * Update queues list for current queue manager.
     * @throws QMInteractionException
     */
    public ObservableList<QueueBean> reloadQueues() throws QMInteractionException {
        try {
            List<Queue> listOfQueues = inspector.listLocalQueues();
            queues = FXCollections.observableArrayList();
            for (Queue q : listOfQueues) {
                QueueBean queueBean = queueBeanProvider.get();
                queueBean.initFromRealQueue(q);
                queueBean.setParent(this);
                queues.add(queueBean);
            }
        } catch (MQException | IOException e) {
            throw new QMInteractionException("Queue lookup failure. ", e);
        }
        return queues;
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
