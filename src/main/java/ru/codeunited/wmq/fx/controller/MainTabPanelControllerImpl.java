package ru.codeunited.wmq.fx.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.bus.MessageGetEvent;
import ru.codeunited.wmq.fx.bus.MessagePutEvent;
import ru.codeunited.wmq.fx.bus.ReconnectRequiredEvent;
import ru.codeunited.wmq.fx.bus.ShutdownEvent;
import ru.codeunited.wmq.fx.model.*;
import ru.codeunited.wmq.messaging.*;
import ru.codeunited.wmq.messaging.impl.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.impl.MessageProducerImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static ru.codeunited.wmq.RFHConstants.OPT_QMANAGER;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * Main tab controller
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
@Singleton
public final class MainTabPanelControllerImpl implements MainTabPanelController {

    @FXML private ComboBox<QueueManagerBean> queueManagerListControl;

    @FXML private ComboBox<QueueBean> queueListControl;

    @FXML private Text underQtext;

    @FXML private Text infoBar;

    @FXML private TextArea mainTextArea;

    @FXML private ToggleGroup contentPresentationTG;

    @Inject private ExecutionContext context;

    @Inject private MainTabModel mainTab;

    @Inject private EventBus eventBus;

    MainTabPanelControllerImpl() throws IOException {

    }

    private <T> T getSelected(ComboBox<T> combo) {
        return combo.getValue();
    }

    private String getCurrentPresentationFormat() {
        return ((RadioButton)contentPresentationTG.getSelectedToggle()).getText();
    }

    private void updateTextArea(String newText) {

        mainTextArea.setText(newText);
    }

    private void updateInfoBar(String text) {
        infoBar.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // perform registration in the bus
        eventBus.register(this);

        // add queue manager to the model
        // TODO only single queue manager now supported.
        String contextQueueManager = context.getOption(OPT_QMANAGER);
        mainTab.addQueueManager(contextQueueManager);

        // setup queue list properties and listeners
        queueListControl.setPromptText("[None]");
        queueListControl.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue() != null) {
                underQtext.setText(observable.getValue().info());
            } else {
                underQtext.setText("");
            }
        });

        // setup toggle group listener
        contentPresentationTG.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("toggle changed" + newValue);
        });

        // initialize converters
        queueManagerListControl.setConverter(new QMBeanStringConverter());
        queueListControl.setConverter(new QBeanStringConverter(queueManagerListControl));

        // attach models to controller
        queueManagerListControl.setItems(mainTab.getQueueManagersList());
        queueManagerListControl.getSelectionModel().selectFirst();

        final QueueManagerBean qm = queueManagerListControl.getSelectionModel().getSelectedItem();
        try {
            qm.connect();
            updateInfoBar("Queue manager " + qm.getName() + " connected");
        } catch (QMInteractionException e) {
            e.printStackTrace();
        }
        queueListControl.setItems(qm.getQueues());
        queueListControl.getSelectionModel().selectFirst();
    }

    @Subscribe public void onShutdown(ShutdownEvent event) throws QMInteractionException {
        System.out.println("onShutdown <- " + event.getSource().getClass().getName());
        shutdownConnections();
        System.out.println("Connections offline");
    }

    @Subscribe public void onMessagePut(MessagePutEvent event) throws QMInteractionException {
        // update queue info record
        reloadQueueInfo();
    }

    @Subscribe public void onReconnectRequired(ReconnectRequiredEvent event) throws QMInteractionException {
        final QueueManagerBean qmBean = getSelected(queueManagerListControl);
        try {
            qmBean.disconnect();
        } catch (QMInteractionException e) {

        }
        qmBean.connect();
        reloadQueues();
        updateInfoBar("Queue manager " + qmBean.getName() + " reconnected");
    }

    @Subscribe public void onMessageGet(MessageGetEvent event) throws QMInteractionException {
        // reload queue info
        reloadQueueInfo();

        // print message content
        if (event.getMessage().isPresent()) {
            MQMessage mqMessage = event.getMessage().get();
            try {
                String messageData = MessageTools.readMessageBodyToString(mqMessage);
                updateTextArea(messageData);
            } catch (IOException e) {
                updateInfoBar("Oops, message broken: " + e.getMessage());
            }
        } else if (event.getNoMessages().isPresent()) {
            updateInfoBar("Queue is empty");
        } else if (event.getOtherProblem().isPresent()) {
            updateInfoBar(event.getOtherProblem().get().getMessage());
        } else { // something strange
            updateInfoBar("Area 51");
        }
    }

    public void reloadQueueInfo() throws QMInteractionException {
        QueueBean currenttlySelectedQueue = getSelected(queueListControl);
        currenttlySelectedQueue.reload();
        underQtext.setText(currenttlySelectedQueue.info());
    }

    @Override
    public void shutdownConnections() throws QMInteractionException {
        List<QueueManagerBean> qmgrs = queueManagerListControl.getItems();
        for (QueueManagerBean qmgr: qmgrs) {
            qmgr.disconnect();
        }
    }

    @Override
    @FXML public void reloadQueues() throws QMInteractionException {
        QueueManagerBean queueManagerBean = getSelected(queueManagerListControl);
        QueueBean currenttlySelectedQueue = getSelected(queueListControl);
        queueListControl.setItems(queueManagerBean.reloadQueues());
        queueListControl.getSelectionModel().select(currenttlySelectedQueue);
        updateInfoBar("Queue list reloaded");
    }

    @Override
    @FXML public void getMessage() throws QMInteractionException {
        final QueueBean queueBean = getSelected(queueListControl);
        try(final MessageConsumer messageConsumer = new MessageConsumerImpl(queueBean.getName(), context.getLink())) {
            MQMessage message = messageConsumer.get();
            eventBus.post(new MessageGetEvent(message, queueBean));
        } catch (MQException | IOException e) {
            eventBus.post(new MessageGetEvent(e, queueBean));
            throw new QMInteractionException("Get message failed", e);
        } catch (NoMessageAvailableException e) {
            eventBus.post(new MessageGetEvent(e, queueBean));
        }
    }

    @Override
    @FXML public void putMessage() throws QMInteractionException {
        final QueueBean queueBean = getSelected(queueListControl);
        final String textForSend = mainTextArea.getText();
        if (StringUtils.isEmpty(textForSend)) {
            return;
        }

        try(final MessageProducer messageProducer = new MessageProducerImpl(queueBean.getName(), context.getLink())) {
            MQMessage mqMessage = messageProducer.send(new CustomSendAdjuster() {

                @Override
                public void setup(MQMessage message) throws IOException, MQException {
                    message.format = MQFMT_STRING;
                    message.writeString(textForSend);
                }

                @Override
                public void setup(MQPutMessageOptions options) {

                }
            });

            eventBus.post(new MessagePutEvent(mqMessage, queueBean));

        } catch (MQException | IOException e) {
            eventBus.post(new MessagePutEvent(e, queueBean));
            throw new QMInteractionException("Put message failed", e);
        }
    }

}
