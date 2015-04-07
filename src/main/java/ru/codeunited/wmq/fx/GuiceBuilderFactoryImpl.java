package ru.codeunited.wmq.fx;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import ru.codeunited.wmq.fx.component.GCustomComponent;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.04.15.
 */
@Singleton
public final class GuiceBuilderFactoryImpl implements GuiceBuilderFactory {

    private final BuilderFactory baseFactory;

    private final Provider<Injector> injectorProvider;

    @Inject
    public GuiceBuilderFactoryImpl(Provider<Injector> injectorProvider, @DefaultBuilderFactory BuilderFactory baseFactory) {
        this.baseFactory = baseFactory;
        this.injectorProvider = injectorProvider;
    }

    @Override
    public Builder<?> getBuilder(Class<?> type) {
        if (type.isAnnotationPresent(GCustomComponent.class)) {
            return injectorProvider.get().getInstance(Key.get(Builder.class, Names.named(type.getName())));
        } else {
            /* create as-is outside guice. */
            return baseFactory.getBuilder(type);
        }
    }
}
