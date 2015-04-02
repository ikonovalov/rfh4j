package ru.codeunited.wmq.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class MainTabView extends Tab {

    public MainTabView() throws IOException {
        // Load root layout from fxml file.
        final FXMLLoader loader = new FXMLLoader(MainTabView.class.getResource("mainTab.fxml"));
        loader.load();
    }
}
