package ru.codeunited.wmq;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.BuilderFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.fx.*;
import ru.codeunited.wmq.fx.bus.ShutdownEvent;
import ru.codeunited.wmq.fx.controller.TopSceneController;
import ru.codeunited.wmq.fx.model.MainTabModelImpl;

/**
 * This is realy a part of a model.
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 11.02.15.
 */
public class RFHFX extends Application {

    // ---------------------------------------
    private MainTabModelImpl mainTab;

    private EventBus eventBus;

    public static void main(String[] args) throws ParseException {
        up(args);
    }

    static void up(String... args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        eventBus.post(new ShutdownEvent(this)); // notify about shutdown request
        super.stop();
    }

    @Override
    public void start(final Stage primaryZtage) throws Exception {

        primaryZtage.setTitle("RFHFX");

        String[] args = getParameters().getRaw().toArray(new String[0]);
        CommandLine cli = CLIFactory.createParser().parse(CLIFactory.createOptions(), args);
        ExecutionContext context = new FXExecutionContext(cli);
        final Injector injector = Guice.createInjector(new ContextModule(context), new CommandsModule(), new GuiceModule());

        // set event bus instance
        eventBus = injector.getInstance(EventBus.class);

        // Load root layout from fxml file.
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("fx/application.fxml"));
        loader.setController(injector.getInstance(TopSceneController.class));
        loader.setControllerFactory(param -> injector.getInstance(param));
        loader.setBuilderFactory(injector.getInstance(BuilderFactory.class));

        GridPane rootLayout = loader.load();

        // Show the scene containing the root layout.
        final Scene scene = new Scene(rootLayout);
        primaryZtage.setScene(scene);
        primaryZtage.show();
    }

}
