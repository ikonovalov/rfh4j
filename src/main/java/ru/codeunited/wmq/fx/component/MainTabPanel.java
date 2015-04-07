package ru.codeunited.wmq.fx.component;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
public interface MainTabPanel extends InitializeableComponent {

    void setPrefHeight(double prefHeight);

    double getPrefHeight();

    void setPrefWidth(double value);

    double getPrefWidth();

    void setMinHeight(double min);

    double getMinHeight();

    void setMinWidth(double min);

    double getMinWidth();
}
