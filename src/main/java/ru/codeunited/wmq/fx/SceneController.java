package ru.codeunited.wmq.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.util.StringConverter;
import ru.codeunited.wmq.DefaultExecutionPlanBuilder;
import ru.codeunited.wmq.ExecutionPlanBuilder;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.handler.NestedHandlerException;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class SceneController implements Initializable {

    private RFHFX application;

    @FXML
    private TabPane globalTabPane;

    @FXML
    private ComboBox<QMBean> currentQM;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller initialize()");
        globalTabPane.getTabs().size();

        currentQM.setConverter(new QMBean.QMBeanStringConverter());
        currentQM.valueProperty().addListener(new ChangeListener<QMBean>() {
            @Override
            public void changed(ObservableValue<? extends QMBean> observable, QMBean oldValue, QMBean newValue) {
                System.out.println("CHANGED!");
            }
        });
    }

    public final void attach(final RFHFX rfhfx) {
        System.out.println("Controller attach()");
        application = rfhfx;

        // initialize main tab
        currentQM.setItems(rfhfx.mainTabView().getQueueManagersList());
        currentQM.getSelectionModel().select(0);

        final CommandChain chain = new CommandChain(application.getContext());
        chain.addCommand(new MQConnectCommand());
        try {
            chain.execute();
        } catch (CommandGeneralException | MissedParameterException | IncompatibleOptionsException | NestedHandlerException e) {
            e.printStackTrace();
        }

    }
}
