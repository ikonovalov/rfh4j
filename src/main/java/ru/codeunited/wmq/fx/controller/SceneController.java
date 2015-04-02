package ru.codeunited.wmq.fx.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.model.QBean;
import ru.codeunited.wmq.fx.model.QMBean;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class SceneController implements Initializable {

    private RFHFX application;

    @FXML private TabPane globalTabPane;

    @FXML private ComboBox<QMBean> queueManagerListControl;

    @FXML private ComboBox<QBean> queueListControl;

    @FXML private javafx.scene.text.Text underQtext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller initialize()");
        globalTabPane.getTabs().size();


        queueListControl.setPromptText("[None]");
        queueListControl.valueProperty().addListener(new ChangeListener<QBean>() {
            @Override
            public void changed(ObservableValue<? extends QBean> observable, QBean oldValue, QBean newValue) {
                underQtext.setText(observable.getValue().info());
            }
        });
    }

    @FXML
    public void closeApplication(ActionEvent event) throws Exception {
        Platform.exit();
    }

    public final void attach(final RFHFX rfhfx) throws QMInteractionException {
        System.out.println("Controller attach() " + this.toString());
        application = rfhfx;

        // initialize converters
        queueManagerListControl.setConverter(new QMBean.QMBeanStringConverter());
        queueListControl.setConverter(new QBean.QBeanStringConverter(application));

        // attach models
        queueManagerListControl.setItems(rfhfx.mainTabView().getQueueManagersList());
        queueManagerListControl.getSelectionModel().selectFirst();
        rfhfx.mainTabView().setCurrentQueueManager(queueManagerListControl.getValue());

        final QMBean qm = queueManagerListControl.getSelectionModel().getSelectedItem();
        queueListControl.setItems(qm.getQueues());
        queueListControl.getSelectionModel().selectFirst();

    }
}
