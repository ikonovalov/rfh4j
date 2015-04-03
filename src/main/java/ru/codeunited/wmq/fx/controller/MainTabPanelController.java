package ru.codeunited.wmq.fx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.model.QueueBean;
import ru.codeunited.wmq.fx.model.QueueManagerBean;

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
public class MainTabPanelController implements Initializable {

    private RFHFX application;

    @FXML private ComboBox<QueueManagerBean> queueManagerListControl;

    @FXML private ComboBox<QueueBean> queueListControl;

    @FXML private Text underQtext;

    public MainTabPanelController() throws IOException {
        System.out.println(getClass().getName() + " is up");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(getClass().getName() + " controller initialize()");

        queueListControl.setPromptText("[None]");
        queueListControl.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue() != null) {
                underQtext.setText(observable.getValue().info());
            } else {
                underQtext.setText("");
            }
        });
    }

    public void attach(RFHFX application) throws QMInteractionException {
        this.application = application;

        // initialize converters
        queueManagerListControl.setConverter(new QMBeanStringConverter(application));
        queueListControl.setConverter(new QBeanStringConverter(application, queueManagerListControl));

        // attach models
        queueManagerListControl.setItems(application.mainTabView().getQueueManagersList());
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
