package ru.codeunited.wmq.commands;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
public @interface ConnectCommand {
}
