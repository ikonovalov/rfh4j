package ru.codeunited.wmq.fx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    @Inject private MainTabModel mainTab;

    MainTabPanelControllerImpl() throws IOException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        queueListControl.setItems(qm.getQueues());
        queueListControl.getSelectionModel().selectFirst();
    }

    @FXML public void shutdownConnections() throws QMInteractionException {
        // close all connections
        List<QueueManagerBean> qmgrs = queueManagerListControl.getItems();
        for (QueueManagerBean qmgr: qmgrs) {
            qmgr.disconnect();
        }
    }

}
