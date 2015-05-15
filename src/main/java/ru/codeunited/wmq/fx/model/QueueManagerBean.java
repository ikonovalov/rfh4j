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
import ru.codeunited.wmq.messaging.impl.ManagerInspectorImpl;
import ru.codeunited.wmq.messaging.Queue;


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

    @Inject CommandChain commandChain;

    @ConnectCommand
    @Inject Command connectCommand;

    @DisconnectCommand
    @Inject Command disconnectCommand;

    //==============================================================
    public QueueManagerBean() {
        super();
    }

    public void setName(String name) {
        this.name.set(Objects.requireNonNull(name));
    }

    private void postConnectOperations() throws QMInteractionException {
        this.inspector = new ManagerInspectorImpl(context.getLink());
        reloadQueues();
    }

    public ReturnCode connect() throws QMInteractionException {
        commandChain.addCommand(connectCommand);
        try {
            ReturnCode rc =  commandChain.execute();
            if (rc == ReturnCode.SUCCESS) {
                postConnectOperations();
            }
            return rc;
        } catch (CommandGeneralException | MissedParameterException | IncompatibleOptionsException | NestedHandlerException e) {
            throw  new QMInteractionException("Connect failed. ", e);
        }
    }

    public ReturnCode disconnect() throws QMInteractionException {
        commandChain.addCommand(disconnectCommand);
        try {
            return commandChain.execute();
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

    public QueueBean reloadQueue(QueueBean bean) throws QMInteractionException {
        try {
            List<Queue> queueList = inspector.selectLocalQueues(bean.getName());
            if (queueList.size() > 1) {
                throw new IllegalArgumentException("Queue with name " + bean.getName() + " has several responses");
            }
            bean.initFromRealQueue(queueList.get(0));
        } catch (MQException | IOException e) {
           throw new QMInteractionException("Reload queue [" + bean.getName() + "] failed", e);
        }
        return bean;
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
