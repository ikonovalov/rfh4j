package ru.codeunited.wmq;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.fx.*;
import ru.codeunited.wmq.fx.model.MainTab;

import java.net.URL;

/**
 * This is realy a part of a model.
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 11.02.15.
 */
public class RFHFX extends Application {

    // ---------------------------------------
    private MainTab mainTab;

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

        final Injector injector = Guice.createInjector(new GuiceModule());

        primaryZtage.setTitle("RFHFX");

        final String[] args = inputCLIParameters();
        final CommandLine cli = CLIFactory.createParser().parse(CLIFactory.createOptions(), args);
        FXExecutionContext.create(cli, this);

        // Load root layout from fxml file.
        final FXMLLoader loader = new FXMLLoader();
        final URL url = RFHFX.class.getResource("fx/application.fxml");

        loader.setLocation(url);
        loader.setControllerFactory(param -> injector.getInstance(param));
        loader.setBuilderFactory(injector.getInstance(BuilderFactory.class));

        GridPane rootLayout = loader.load();

        // Show the scene containing the root layout.
        final Scene scene = new Scene(rootLayout);
        primaryZtage.setScene(scene);
        primaryZtage.show();

    }

    protected String[] inputCLIParameters() {
        return getParameters().getRaw().toArray(new String[0]);
    }


    public final MainTab mainTabView() throws QMInteractionException {
        if (mainTab == null) {
            mainTab = ModelFactory.newInstance().createMainTab();
        }
        return mainTab;
    }
}
