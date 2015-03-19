package ru.codeunited.wmq.fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import ru.codeunited.wmq.RFHFX;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class SceneController implements Initializable {

    private RFHFX application;

    private MainTab mainTab;

    @FXML
    private TabPane globalTabPane;

    @FXML
    private ComboBox<QMBean> currentQM;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        globalTabPane.getTabs().size();
    }

    public final void attach(final RFHFX rfhfx) {
        application = rfhfx;
        mainTab = application.createMainTabView();
        currentQM.setItems(mainTab.getQmList());
        currentQM.getSelectionModel().select(0);
    }
}
