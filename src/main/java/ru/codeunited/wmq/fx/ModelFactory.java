package ru.codeunited.wmq.fx;

import ru.codeunited.wmq.fx.model.MainTabModel;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 07.04.15.
 */
@Deprecated
public interface ModelFactory {

    MainTabModel createMainTabModel() throws QMInteractionException;

}
