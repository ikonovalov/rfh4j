package ru.codeunited.wmq.fx.component;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import ru.codeunited.wmq.fx.controller.MainTabPanelController;

import javax.inject.Inject;
import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
@GCustomComponent
public final class MainTabPanelImpl extends AnchorPane implements MainTabPanel {

    @Inject
    private MainTabPanelController controller;

    MainTabPanelImpl() {
        super();
    }

    @Override
    public void initialize() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainTabPanelImpl.class.getResource("mainTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(controller);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
