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
import ru.codeunited.wmq.fx.MainTab;
import ru.codeunited.wmq.fx.ModelFactory;
import ru.codeunited.wmq.fx.SceneController;

import java.io.IOException;
import java.net.URL;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 11.02.15.
 */
public class RFHFX extends Application {

    private Stage primaryStage;

    private GridPane rootLayout;

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
        primaryStage = primaryZtage;
        primaryStage.setTitle("RFHFX");

        final String[] args = inputCLIParameters();
        final CommandLine cli = CLIFactory.createParser().parse(CLIFactory.createOptions(), args);
        context = new CLIExecutionContext(cli);

        loadRootLayout();
    }

    protected String[] inputCLIParameters() {
        return getParameters().getRaw().toArray(new String[0]);
    }

    public final ExecutionContext getContext() {
        return context;
    }

    private void loadRootLayout() throws IOException {
        // Load root layout from fxml file.
        final FXMLLoader loader = new FXMLLoader();

        final URL url = RFHFX.class.getResource("fx/application.fxml");
        loader.setLocation(url);
        rootLayout = loader.load();

        // Show the scene containing the root layout.
        final Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneController controller = loader.getController();
        controller.attach(this);
    }

    public final MainTab mainTabView() {
        if (mainTab == null) {
            mainTab = ModelFactory.newInstance(context).createMainTab();
        }
        return mainTab;
    }
}
