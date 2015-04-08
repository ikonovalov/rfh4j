package ru.codeunited.wmq.fx.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ru.codeunited.wmq.fx.bus.ShutdownEvent;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
@Singleton
public final class TopSceneControllerImpl implements TopSceneController {

    @Inject private EventBus eventBus;

    TopSceneControllerImpl() {
        super();
    }

    @Override
    @FXML public void closeApplication(ActionEvent event) throws Exception {
        // shutdown platform
        Platform.exit();
    }

}
