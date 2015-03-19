package ru.codeunited.wmq.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.03.15.
 */
public class MainTab {

    private ObservableList<QMBean> qmList = FXCollections.observableArrayList(
            new QMBean("DEFQM")
    );


    public ObservableList<QMBean> getQmList() {
        return qmList;
    }
}
