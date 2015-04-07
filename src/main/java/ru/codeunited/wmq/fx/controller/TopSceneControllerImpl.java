package ru.codeunited.wmq.fx.controller;

import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
@Singleton
public final class TopSceneControllerImpl implements TopSceneController {

    TopSceneControllerImpl() {
        System.out.println(TopSceneControllerImpl.class.getName() + " is up");
    }

    @Override
    @FXML
    public void closeApplication(ActionEvent event) throws Exception {
        // shutdown platform
        Platform.exit();
    }

}
