package ru.codeunited.wmq.fx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.codeunited.wmq.ExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public final class MainTab {

    private final ExecutionContext context;

    private final ObservableList<QueueManagerBean> qmList = FXCollections.observableArrayList();

    public MainTab(ExecutionContext context) {
        this.context = context;
    }

    public void addQueueManager(QueueManagerBean newManager) {
        qmList.add(newManager);
    }

    public ObservableList<QueueManagerBean> getQueueManagersList() {
        return qmList;
    }
}
