package ru.codeunited.wmq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.cli.CLIFactory;

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

    private static ExecutionContext context;

    public static void main(String[] args) throws ParseException {
        final CommandLine cli = CLIFactory.createParser().parse(CLIFactory.createOptions(), args);
        final ExecutionContext context = new CLIExecutionContext(cli);
        up(context, args);
    }

    static void up(ExecutionContext contezt, String ... args) {
        context = contezt;
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RFHFX");

        initRootLayout();
    }


    private void initRootLayout() throws IOException {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();

            URL url = RFHFX.class.getResource("fx/application.fxml");
            loader.setLocation(url);
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
