package ru.codeunited.wmq.fx.model;

import com.google.inject.Singleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.codeunited.wmq.fx.Draft;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
@Singleton
public final class MainTabModelImpl implements MainTabModel {

    private final ObservableList<QueueManagerBean> qmList;

    @Draft
    @Inject private Provider<QueueManagerBean> queueManagerBeanProvider; /* should be prototype */

    public MainTabModelImpl() {
        super();
        qmList = FXCollections.observableArrayList();
    }

    @Override
    public void addQueueManager(QueueManagerBean newManager) {
        qmList.add(newManager);
    }

    @Override
    public void addQueueManager(String name) {
        QueueManagerBean qmBean = queueManagerBeanProvider.get();
        qmBean.setName(name);
        addQueueManager(qmBean);
    }

    @Override
    public ObservableList<QueueManagerBean> getQueueManagersList() {
        return FXCollections.unmodifiableObservableList(qmList);
    }
}
