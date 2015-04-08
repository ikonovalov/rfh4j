package ru.codeunited.wmq.fx;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.fx.component.MainTabPanel;
import ru.codeunited.wmq.fx.component.MainTabPanelImpl;
import ru.codeunited.wmq.fx.component.MainTabPanelImplBuilder;
import ru.codeunited.wmq.fx.controller.MainTabPanelController;
import ru.codeunited.wmq.fx.controller.MainTabPanelControllerImpl;
import ru.codeunited.wmq.fx.controller.TopSceneController;
import ru.codeunited.wmq.fx.controller.TopSceneControllerImpl;
import ru.codeunited.wmq.fx.model.*;


/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
public class GuiceModule extends AbstractModule {

    private final ExecutionContext context;

    public GuiceModule(ExecutionContext context) {
        this.context = context;
    }

    @Override
    protected void configure() {

        // model factory
        bind(MainTabModel.class).to(MainTabModelImpl.class);
        bind(QueueManagerBean.class).annotatedWith(Draft.class).toConstructor(ReflectionUtil.getDefaultConstructor(QueueManagerBean.class));
        bind(QueueBean.class).annotatedWith(Draft.class).toConstructor(ReflectionUtil.getDefaultConstructor(QueueBean.class));

        // Component builder factories
        bind(BuilderFactory.class).annotatedWith(DefaultBuilderFactory.class).to(JavaFXBuilderFactory.class);
        bind(BuilderFactory.class).to(GuiceBuilderFactory.class);
        bind(GuiceBuilderFactory.class).to(GuiceBuilderFactoryImpl.class);


        // top level components
        bind(TopSceneController.class).to(TopSceneControllerImpl.class);


        // MainTabPanel: component implementation, component builder and controller.
        bind(MainTabPanel.class).to(MainTabPanelImpl.class);
        bind(Builder.class).annotatedWith(Names.named(MainTabPanelImpl.class.getName())).to(MainTabPanelImplBuilder.class);
        bind(MainTabPanelController.class).to(MainTabPanelControllerImpl.class);
    }

    @Provides
    ExecutionContext executionContext() {
        return context;
    }

    @Provides
    @Singleton /** provides EventBus singleton */
    EventBus eventBus() {
        return new EventBus();
    }

}
