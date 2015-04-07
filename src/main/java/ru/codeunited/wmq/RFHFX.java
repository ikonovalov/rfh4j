package ru.codeunited.wmq;

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
import ru.codeunited.wmq.fx.*;
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
        super.stop();
    }

    @Override
    public void start(final Stage primaryZtage) throws Exception {

        primaryZtage.setTitle("RFHFX");

        final String[] args = getParameters().getRaw().toArray(new String[0]);
        final CommandLine cli = CLIFactory.createParser().parse(CLIFactory.createOptions(), args);

        final Injector injector = Guice.createInjector(new GuiceModule(new FXExecutionContext(cli)));

        // Load root layout from fxml file.
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("fx/application.fxml"));

        loader.setControllerFactory(param -> injector.getInstance(param));
        loader.setBuilderFactory(injector.getInstance(BuilderFactory.class));

        GridPane rootLayout = loader.load();

        // Show the scene containing the root layout.
        final Scene scene = new Scene(rootLayout);
        primaryZtage.setScene(scene);
        primaryZtage.show();
    }

}
