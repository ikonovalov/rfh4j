package ru.codeunited.wmq.fx;

import javafx.beans.property.SimpleStringProperty;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.fx.model.MainTab;
import ru.codeunited.wmq.fx.model.QueueManagerBean;

import static ru.codeunited.wmq.RFHConstants.OPT_QMANAGER;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 01.04.15.
 */
public class ModelFactory {

    private ModelFactory() {

    }

    public static ModelFactory newInstance() {
        return new ModelFactory();
    }


    public MainTab createMainTab() throws QMInteractionException {
        final ExecutionContext context = FXExecutionContext.getInstance();
        MainTab mainTab = new MainTab();
        String contextQueueManager = context.getOption(OPT_QMANAGER);

        final QueueManagerBean qmBean = new QueueManagerBean(new SimpleStringProperty(contextQueueManager));
        mainTab.addQueueManager(qmBean);
        qmBean.connect();
        qmBean.afterConnect();
        return mainTab;
    }
}
