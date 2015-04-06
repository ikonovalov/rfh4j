package ru.codeunited.wmq.fx.controller;

import ru.codeunited.wmq.fx.QMInteractionException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
public interface MainTabPanelController {

    void shutdownConnections() throws QMInteractionException;
}
