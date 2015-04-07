package ru.codeunited.wmq.fx.model;

import java.lang.reflect.Constructor;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 07.04.15.
 */
public final class ReflectionUtil {

    private ReflectionUtil() {}


    public static Constructor getDefaultConstructor(Class tClass) {
        try {
            return tClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
