package ru.codeunited.wmq;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.CommandGeneralException;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.commands.IncompatibleOptionsException;
import ru.codeunited.wmq.commands.MissedParameterException;
import ru.codeunited.wmq.handler.NestedHandlerException;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;

import static ru.codeunited.wmq.frame.CLITestSupport.prepareCommandLine;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public class GuiceSupport {

    protected Injector injector;

    protected  ExecutionContext context;

    private final Parallel parallel = new Parallel();

    @Inject
    public Injector setup(ExecutionContext executionContext) {
        context = executionContext;
        injector = Guice.createInjector(new ContextModule(executionContext), new CommandsModule());
        return injector;
    }

    /**
     * Put message to a queue.
     * @param destination
     * @throws ParseException
     * @throws MissedParameterException
     * @throws IncompatibleOptionsException
     * @throws CommandGeneralException
     */
    protected void putToQueue(String destination) throws ParseException, MissedParameterException, IncompatibleOptionsException, CommandGeneralException, NestedHandlerException {
        CommandLine cl = prepareCommandLine(String.format("%3$s --dstq %1$s --text %2$s", destination, String.valueOf(System.currentTimeMillis()), "-Q DEFQM -c JVM.DEF.SVRCONN"));
        ExecutionContext executionContext = new CLIExecutionContext(cl);
        setup(executionContext);
        ExecutionPlanBuilder executionPlanBuilder = injector.getInstance(ExecutionPlanBuilder.class);
        executionPlanBuilder.buildChain().execute();

    }

    protected void branch(Parallel.Branch branch) {
        parallel.add(branch);
    }

    protected void parallel() throws ExecutionException, InterruptedException {
        parallel.go();
    }
}
