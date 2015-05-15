package ru.codeunited.wmq.fx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
public interface TopSceneController {

    /**
     * Close application.
     * @param event
     * @throws Exception
     */
    void closeApplication(ActionEvent event) throws Exception;

    @FXML
    void reconnectQueueManager();
}
