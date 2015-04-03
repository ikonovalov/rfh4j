package ru.codeunited.wmq.fx.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.fx.QMInteractionException;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class TopSceneController extends ContextAwareController {

    public TopSceneController() {
        System.out.println(getClass().getName() + " is up");
    }

    @FXML public void closeApplication(ActionEvent event) throws Exception {
        // shutdown platform
        Platform.exit();
    }

    public final void attach(final RFHFX rfhfx) throws QMInteractionException {
        System.out.println("Controller attach() " + this.toString());
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
