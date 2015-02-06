package ru.codeunited.wmq;

import org.apache.commons.cli.*;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.commands.*;

import java.util.concurrent.ExecutionException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class CLITestSupport {

    private final Parallel parallel = new Parallel();

    protected CommandLineParser getCliParser() {
        return CLIFactory.createParser();
    }

    Options getOptions() {
        return CLIFactory.createOptions();
    }

    protected CommandLine prepareCommandLine(String line) throws ParseException {
        final String[] args = line.split(" ");
        return prepareCommandLine(args);
    }

    protected CommandLine prepareCommandLine(String[] args) throws ParseException {
        try {
            return getCliParser().parse(getOptions(), args);
        } catch (AlreadySelectedException ase) {
            throw ase;
        }
    }

    protected String connectionParameter() {
        return "-Q DEFQM -c JVM.DEF.SVRCONN";
    }

    protected CommandLine getCommandLine_With_Qc() throws ParseException {
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
     * @param command
     * @return
     */
    protected CommandChain surroundSingleCommandWithConnectionAdvices(ExecutionContext context, Command command) {
        final CommandChain maker = new CommandChain(context);
        final AbstractCommand cmdConnect = new MQConnectCommand();
        final AbstractCommand cmdDisconnect = new MQDisconnectCommand();
        return maker.addCommand(cmdConnect).addCommand(command).addCommand(cmdDisconnect);
    }

    /**
     * Put message to a queue.
     * @param destination
     * @throws ParseException
     * @throws MissedParameterException
     * @throws IncompatibleOptionsException
     * @throws CommandGeneralException
     */
    protected void putToQueue(String destination) throws ParseException, MissedParameterException, IncompatibleOptionsException, CommandGeneralException {
        final CommandLine cl = prepareCommandLine(String.format("%3$s --dstq %1$s --text %2$s", destination, String.valueOf(System.currentTimeMillis()), connectionParameter()));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(executionContext);
        executionPlanBuilder.buildChain().execute();

    }

    protected void branch(Parallel.Branch branch) {
        parallel.add(branch);
    }

    protected void parallel() throws ExecutionException, InterruptedException {
        parallel.go();
    }


}
