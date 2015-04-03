package ru.codeunited.wmq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.fx.QMInteractionException;
import ru.codeunited.wmq.fx.model.MainTab;
import ru.codeunited.wmq.fx.ModelFactory;
import ru.codeunited.wmq.fx.controller.SceneController;

import java.net.URL;

/**
 * This is realy a part of a model.
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 11.02.15.
 */
public class RFHFX extends Application {

    private ExecutionContext context;

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
        primaryZtage.setTitle("RFHFX");

        final String[] args = inputCLIParameters();
        final CommandLine cli = CLIFactory.createParser().parse(CLIFactory.createOptions(), args);
        context = new CLIExecutionContext(cli);

        // Load root layout from fxml file.
        final FXMLLoader loader = new FXMLLoader();

        final URL url = RFHFX.class.getResource("fx/application.fxml");
        loader.setLocation(url);
        GridPane rootLayout = loader.load();

        // Show the scene containing the root layout.
        final Scene scene = new Scene(rootLayout);
        primaryZtage.setScene(scene);
        primaryZtage.show();

        SceneController controller = loader.getController(); /* controller specified in the fxml */
        controller.attach(this);
    }

    protected String[] inputCLIParameters() {
        return getParameters().getRaw().toArray(new String[0]);
    }

    public final ExecutionContext getContext() {
        return context;
    }

    public final MainTab mainTabView() throws QMInteractionException {
        if (mainTab == null) {
            mainTab = ModelFactory.newInstance(context).createMainTab();
        }
        return mainTab;
    }
}
