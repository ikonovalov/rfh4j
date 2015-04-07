package ru.codeunited.wmq.fx.model;

import javafx.collections.ObservableList;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 07.04.15.
 */
public interface MainTabModel {

    /**
     * Add new queue manager bean to the model.
     * @param newManager
     */
    void addQueueManager(QueueManagerBean newManager);

    void addQueueManager(String name);

    ObservableList<QueueManagerBean> getQueueManagersList();
}
