package ru.codeunited.wmq.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class MainTab {

    private ObservableList<QMBean> qmList = FXCollections.observableArrayList();

    public MainTab() {

    }

    public final void addQueueManager(QMBean newManager) {
        qmList.add(newManager);
    }

    public ObservableList<QMBean> getQueueManagersList() {
        return qmList;
    }
}
