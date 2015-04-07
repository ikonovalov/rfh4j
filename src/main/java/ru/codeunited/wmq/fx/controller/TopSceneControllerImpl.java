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
public class TopSceneControllerImpl extends ContextAwareController implements TopSceneController {

    public TopSceneControllerImpl() {
        System.out.println(TopSceneControllerImpl.class.getName() + " is up");
    }

    @FXML public void closeApplication(ActionEvent event) throws Exception {
        // shutdown platform
        Platform.exit();
    }

}
