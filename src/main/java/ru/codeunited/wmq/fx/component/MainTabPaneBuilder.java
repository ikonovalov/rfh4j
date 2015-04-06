package ru.codeunited.wmq.fx.component;

import javafx.util.Builder;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
public final class MainTabPaneBuilder implements Builder<MainTabPanel> {

    private String minHeight;

    private String minWidth;

    private String prefHeight;

    private String prefWidth;

    public MainTabPaneBuilder() {
        super();
    }

    public String getPrefWidth() {
        return prefWidth;
    }

    public void setPrefWidth(String prefWidth) {
        this.prefWidth = prefWidth;
    }

    public String getPrefHeight() {
        return prefHeight;
    }

    public void setPrefHeight(String prefHeight) {
        this.prefHeight = prefHeight;
    }

    public void setMinWidth(String minWidth) {
        this.minWidth = minWidth;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getMinWidth() {
        return minWidth;
    }

    public String getMinHeight() {
        return minHeight;
    }

    @Override
    public MainTabPanel build() {
        return new MainTabPanelImpl();
    }

}
