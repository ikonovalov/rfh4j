package ru.codeunited.wmq.fx;

import com.google.inject.Injector;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import ru.codeunited.wmq.fx.component.GCustomComponent;
import ru.codeunited.wmq.fx.component.MainTabPaneBuilder;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
            GCustomComponent spec = type.getAnnotation(GCustomComponent.class);
            /*return () -> injectorProvider.get().getInstance(spec.binding());*/
            /*return new MainTabPaneBuilder();*/ // it works with a properties getter and setter
            Builder proxy = (Builder) Proxy.newProxyInstance(
                    Builder.class.getClassLoader(),
                    new Class[]{Builder.class},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            return null;
                        }
                    });
            return proxy;
        } else {
            /* create as-is outside guice. */
            return baseFactory.getBuilder(type);
        }
    }
}
