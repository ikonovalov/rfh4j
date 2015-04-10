package ru.codeunited.wmq.frame;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
@Target({ METHOD }) @Retention(RUNTIME)
public @interface ContextInjection {

    String cli() default "";
}
