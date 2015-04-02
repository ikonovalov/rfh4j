package ru.codeunited.wmq.fx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.codeunited.wmq.ExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class MainTab {

    private ExecutionContext context;

    private ObservableList<QMBean> qmList = FXCollections.observableArrayList();

    private QMBean currentQueueManager;

    public MainTab(ExecutionContext context) {
        this.context = context;
    }

    public QMBean getCurrentQueueManager() {
        return currentQueueManager;
    }

    public void setCurrentQueueManager(QMBean currentQueueManager) {
        this.currentQueueManager = currentQueueManager;
    }

    public final void addQueueManager(QMBean newManager) {
        qmList.add(newManager);
    }

    public ObservableList<QMBean> getQueueManagersList() {
        return qmList;
    }
}
