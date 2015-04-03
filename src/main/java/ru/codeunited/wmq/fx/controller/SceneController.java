package ru.codeunited.wmq.fx.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.QMInteractionException;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class SceneController implements Initializable {

    private RFHFX application;

    public SceneController() {
        System.out.println(getClass().getName() + " is up");
    }

    @FXML public void closeApplication(ActionEvent event) throws Exception {
        // shutdown platform
        Platform.exit();
    }

    public final void attach(final RFHFX rfhfx) throws QMInteractionException {
        System.out.println("Controller attach() " + this.toString());
        application = rfhfx;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
