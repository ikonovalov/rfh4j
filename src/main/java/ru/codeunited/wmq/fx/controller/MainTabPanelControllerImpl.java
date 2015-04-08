package ru.codeunited.wmq.fx.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.bus.ShutdownEvent;
import ru.codeunited.wmq.fx.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static ru.codeunited.wmq.RFHConstants.OPT_QMANAGER;

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

    @Inject private ExecutionContext context;

    @Inject private MainTabModel mainTab;

    @Inject private EventBus eventBus;

    MainTabPanelControllerImpl() throws IOException {

    }

    private <T> T getSelected(ComboBox<T> combo) {
        return combo.getValue();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // perform registration in the bus
        eventBus.register(this);

        // add queue manager to the model
        // TODO only single queue manager now supported.
        String contextQueueManager = context.getOption(OPT_QMANAGER);
        mainTab.addQueueManager(contextQueueManager);

        queueListControl.setPromptText("[None]");
        queueListControl.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue() != null) {
                underQtext.setText(observable.getValue().info());
            } else {
                underQtext.setText("");
            }
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
        } catch (QMInteractionException e) {
            e.printStackTrace();
        }
        queueListControl.setItems(qm.getQueues());
        queueListControl.getSelectionModel().selectFirst();
    }

    @Subscribe public void onShutdown(ShutdownEvent event) throws QMInteractionException {
        shutdownConnections();
    }

    @Override
    @FXML public void shutdownConnections() throws QMInteractionException {
        List<QueueManagerBean> qmgrs = queueManagerListControl.getItems();
        for (QueueManagerBean qmgr: qmgrs) {
            qmgr.disconnect();
        }
        System.out.println("Connections offline");
    }

    @Override
    @FXML public void reloadQueues() throws QMInteractionException {
        QueueManagerBean queueManagerBean = getSelected(queueManagerListControl);
        queueListControl.setItems(queueManagerBean.reloadQueues());
        queueListControl.getSelectionModel().selectFirst();
    }

}
