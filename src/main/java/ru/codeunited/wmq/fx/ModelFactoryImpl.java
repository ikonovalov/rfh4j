package ru.codeunited.wmq.fx;

import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.fx.model.MainTabModel;
import ru.codeunited.wmq.fx.model.MainTabModelImpl;
import ru.codeunited.wmq.fx.model.QueueManagerBean;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import java.lang.reflect.Constructor;

import static ru.codeunited.wmq.RFHConstants.OPT_QMANAGER;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 01.04.15.
 */
@Singleton
public class ModelFactoryImpl implements ModelFactory {

    @Inject
    private ExecutionContext context;

    @Inject /* should be prototype */
    private Provider<QueueManagerBean> queueManagerBeanProvider;

    ModelFactoryImpl() {

    }

    public MainTabModel createMainTabModel() throws QMInteractionException {
        MainTabModelImpl mainTab = new MainTabModelImpl();
        String contextQueueManager = context.getOption(OPT_QMANAGER);

        QueueManagerBean qmBean = queueManagerBeanProvider.get();
        qmBean.setName(contextQueueManager);
        mainTab.addQueueManager(qmBean);
        qmBean.connect();
        qmBean.afterConnect();

        return mainTab;
    }


}
