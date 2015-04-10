package ru.codeunited.wmq;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.apache.commons.cli.*;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.handler.NestedHandlerException;

import java.util.concurrent.ExecutionException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class CLITestSupport {

    private final Parallel parallel = new Parallel();

    public static CommandLineParser getCliParser() {
        return CLIFactory.createParser();
    }

    public static Options getOptions() {
        return CLIFactory.createOptions();
    }

    protected static Injector getStandartInjector(ExecutionContext context) {
        return Guice.createInjector(new ContextModule(context), new CommandsModule());
    }

    protected CommandLine prepareCommandLine(String line) throws ParseException {
        final String[] args = line.split(" ");
        return prepareCommandLine(args);
    }

    public static CommandLine prepareCommandLine(String[] args) throws ParseException {
        try {
            return getCliParser().parse(getOptions(), args);
        } catch (AlreadySelectedException ase) {
            throw ase;
        }
    }

    public static String connectionParameter() {
        return "-Q DEFQM -c JVM.DEF.SVRCONN";
    }

    public static CommandLine getCommandLine_With_Qc() throws ParseException {
        final String[] args = connectionParameter().split(" ");
        return prepareCommandLine(args);
    }

    protected CommandLine getCommandLine_With_Qc_dstq() throws ParseException {
        final String[] args = String.format("%s --dstq RFH.QTEST.QGENERAL1", connectionParameter()).split(" ");
        return prepareCommandLine(args);
    }

    /**
     * Create chain with Connect -> YOUR_COMMAND -> Disconnect
     * @param context
     * @param surroundableClassAnnotation
     * @return
     */
    protected CommandChainImpl surroundSingleCommandWithConnectionAdvices(ExecutionContext context, Class surroundableClassAnnotation) {
        Injector injector = getStandartInjector(context);

        CommandChain maker = injector.getInstance(CommandChain.class);
        Command cmdConnect = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command cmdDisconnect = injector.getInstance(Key.get(Command.class, DisconnectCommand.class));
        return maker
                .addCommand(cmdConnect)
                .addCommand(
                        injector.<Command>getInstance(Key.get(Command.class, surroundableClassAnnotation))
                )
                .addCommand(cmdDisconnect);
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
        CommandLine cl = prepareCommandLine(String.format("%3$s --dstq %1$s --text %2$s", destination, String.valueOf(System.currentTimeMillis()), connectionParameter()));
        ExecutionContext executionContext = new CLIExecutionContext(cl);
        Injector injector = getStandartInjector(executionContext);
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
