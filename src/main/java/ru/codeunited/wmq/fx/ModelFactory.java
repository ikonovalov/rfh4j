package ru.codeunited.wmq.fx;

import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.RFHFX;

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


    public MainTab createMainTab() {
        MainTab mainTab = new MainTab();
        String contextQueueManager = context.getOption(OPT_QMANAGER);
        String[] qms = contextQueueManager.split(",");
        mainTab.addQueueManager(new QMBean(contextQueueManager));
        return mainTab;
    }
}
