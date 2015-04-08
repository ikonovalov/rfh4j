package ru.codeunited.wmq.fx.controller;

import javafx.fxml.Initializable;
import ru.codeunited.wmq.fx.QMInteractionException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
public interface MainTabPanelController extends Initializable {

    /**
     * Close all QueueManagers
     * @throws QMInteractionException
     */
    void shutdownConnections() throws QMInteractionException;

    /**
     * Reload queues list for currently selected queue manager.
     * @throws QMInteractionException
     */
    void reloadQueues() throws QMInteractionException;
}
