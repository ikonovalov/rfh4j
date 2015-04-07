package ru.codeunited.wmq.fx.model;

import com.google.inject.Injector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.codeunited.wmq.ExecutionContext;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
@Singleton
public final class MainTabModelImpl implements MainTabModel {

    private final ObservableList<QueueManagerBean> qmList;

    @Inject /* should be prototype */
    private Provider<QueueManagerBean> queueManagerBeanProvider;

    public MainTabModelImpl() {
        super();
        qmList = FXCollections.observableArrayList();
    }

    @Override
    public void addQueueManager(QueueManagerBean newManager) {
        qmList.add(newManager);
    }

    @Override
    public ObservableList<QueueManagerBean> getQueueManagersList() {
        return FXCollections.unmodifiableObservableList(qmList);
    }
}
