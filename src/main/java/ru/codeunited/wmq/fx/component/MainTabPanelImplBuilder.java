package ru.codeunited.wmq.fx.component;

import javafx.util.Builder;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
@Singleton
public final class MainTabPanelImplBuilder implements Builder<MainTabPanel> {

    private String minHeight;

    private String minWidth;

    private String prefHeight;

    private String prefWidth;

    @Inject
    private Provider<MainTabPanel> mainTabPanelProvider;

    public MainTabPanelImplBuilder() {
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
        MainTabPanel panel = mainTabPanelProvider.get();
        panel.setMinHeight(Double.valueOf(getMinHeight()));
        panel.setMinWidth(Double.valueOf(getMinWidth()));
        panel.setPrefHeight(Double.valueOf(getPrefHeight()));
        panel.setPrefWidth(Double.valueOf(getPrefWidth()));
        panel.initialize();
        return panel;
    }

}
