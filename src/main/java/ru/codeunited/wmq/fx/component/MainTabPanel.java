package ru.codeunited.wmq.fx.component;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class MainTabPanel extends AnchorPane {

    public MainTabPanel() throws IOException {
        System.out.println(getClass().getName() + " is up");
        FXMLLoader fxmlLoader = new FXMLLoader(MainTabPanel.class.getResource("mainTab.fxml"));
        fxmlLoader.setRoot(this);
        //fxmlLoader.setController(new MainTabController());
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
