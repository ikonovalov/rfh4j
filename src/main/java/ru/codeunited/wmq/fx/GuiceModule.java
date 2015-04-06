package ru.codeunited.wmq.fx;

import com.google.inject.AbstractModule;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.BuilderFactory;
import ru.codeunited.wmq.fx.component.MainTabPanel;
import ru.codeunited.wmq.fx.component.MainTabPanelImpl;
import ru.codeunited.wmq.fx.controller.MainTabPanelController;
import ru.codeunited.wmq.fx.controller.MainTabPanelControllerImpl;
import ru.codeunited.wmq.fx.controller.TopSceneController;
import ru.codeunited.wmq.fx.controller.TopSceneControllerImpl;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {

        // Component builder factories
        bind(BuilderFactory.class).annotatedWith(DefaultBuilderFactory.class).to(JavaFXBuilderFactory.class);
        bind(BuilderFactory.class).to(GuiceBuilderFactory.class);
        bind(GuiceBuilderFactory.class).to(GuiceBuilderFactoryImpl.class);

        // top level components
        bind(TopSceneController.class).to(TopSceneControllerImpl.class);

        // custom components
        bind(MainTabPanel.class).to(MainTabPanelImpl.class);
        bind(MainTabPanelController.class).to(MainTabPanelControllerImpl.class);
    }
}
