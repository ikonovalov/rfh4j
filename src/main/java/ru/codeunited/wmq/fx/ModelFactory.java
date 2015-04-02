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

    private final ExecutionContext context;

    private ModelFactory(ExecutionContext context) {
        this.context = context;
    }

    public static ModelFactory newInstance(ExecutionContext context) {
        return new ModelFactory(context);
    }


    public MainTab createMainTab() throws QMInteractionException {
        MainTab mainTab = new MainTab(context);
        String contextQueueManager = context.getOption(OPT_QMANAGER);

        final QueueManagerBean qmBean = new QueueManagerBean(new SimpleStringProperty(contextQueueManager), context);
        mainTab.addQueueManager(qmBean);
        qmBean.connect();
        qmBean.afterConnect();
        return mainTab;
    }
}
