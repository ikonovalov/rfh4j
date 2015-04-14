package ru.codeunited.wmq.frame;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.CLIExecutionContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static ru.codeunited.wmq.frame.CLITestSupport.prepareCommandLine;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public class GuiceContextTestRunner extends BlockJUnit4ClassRunner {



    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public GuiceContextTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        return super.createTest();
    }



    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        ContextInjection injection = method.getMethod().getAnnotation(ContextInjection.class);
        if (injection != null) {
            if (StringUtils.isNotBlank(injection.cli())) {
                String cli = injection.cli();
                try {
                    ExecutionContext context = new CLIExecutionContext(prepareCommandLine(cli));
                    GuiceModules modAnnotation = test.getClass().getAnnotation(GuiceModules.class);
                    if (modAnnotation != null) {
                        Class<? extends Module>[] moduleClasses = modAnnotation.value();
                        List<Module> instantinatedModules = new ArrayList<>(moduleClasses.length);
                        for (Class moduleClass : moduleClasses) {
                            // find execution context's constructors
                            try {
                                Constructor<Module> constructor = moduleClass.getConstructor(ExecutionContext.class);
                                Module moduleInstance = constructor.newInstance(context);
                                instantinatedModules.add(moduleInstance);
                            } catch (NoSuchMethodException e) {
                                // try to get default constructor
                                try {
                                    Constructor<Module> constructor = moduleClass.getConstructor();
                                    Module moduleInstance = constructor.newInstance();
                                    instantinatedModules.add(moduleInstance);
                                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e1) {
                                    // now it's time to blow
                                    throw new RuntimeException(e);
                                }
                            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        // create Injector
                        Injector injector = Guice.createInjector(instantinatedModules);
                        injector.injectMembers(test);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e); // blow silent
                }
            }
        }
        return super.methodInvoker(method, test);
    }
}
