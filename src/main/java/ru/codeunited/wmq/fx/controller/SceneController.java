package ru.codeunited.wmq.fx.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.model.QueueBean;
import ru.codeunited.wmq.fx.model.QueueManagerBean;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class SceneController implements Initializable {

    private RFHFX application;

    @FXML private TabPane globalTabPane;

    @FXML private ComboBox<QueueManagerBean> queueManagerListControl;

    @FXML private ComboBox<QueueBean> queueListControl;

    @FXML private Text underQtext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller initialize()");
        globalTabPane.getTabs().size();


        queueListControl.setPromptText("[None]");
        queueListControl.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue() != null) {
                underQtext.setText(observable.getValue().info());
            } else {
                underQtext.setText("");
            }
        });
    }

    @FXML public void closeApplication(ActionEvent event) throws Exception {
        // close all connections
        List<QueueManagerBean> qmgrs = queueManagerListControl.getItems();
        for (QueueManagerBean qmgr: qmgrs) {
            qmgr.disconnect();
        }

        // shutdown platform
        Platform.exit();
    }

    public final void attach(final RFHFX rfhfx) throws QMInteractionException {
        System.out.println("Controller attach() " + this.toString());
        application = rfhfx;

        // initialize converters
        queueManagerListControl.setConverter(new QMBeanStringConverter(application));
        queueListControl.setConverter(new QBeanStringConverter(application, queueManagerListControl));

        // attach models
        queueManagerListControl.setItems(rfhfx.mainTabView().getQueueManagersList());
        queueManagerListControl.getSelectionModel().selectFirst();

        final QueueManagerBean qm = queueManagerListControl.getSelectionModel().getSelectedItem();
        queueListControl.setItems(qm.getQueues());
        queueListControl.getSelectionModel().selectFirst();

    }
}
