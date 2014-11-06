package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.commands.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class CLITestSupport {
    private final Options options = CLIFactory.createOptions();

    private final CommandLineParser cliParser = CLIFactory.createParser();

    CommandLineParser getCliParser() {
        return cliParser;
    }

    Options getOptions() {
        return options;
    }

    private final ConsoleWriter consoleWriter = new ConsoleWriter(System.out, System.err);

    protected CommandLine prepareCommandLine(String line) throws ParseException {
        final String[] args = line.split(" ");
        return prepareCommandLine(args);
    }

    protected CommandLine prepareCommandLine(String[] args) throws ParseException {
        return getCliParser().parse(getOptions(), args);
    }

    protected CommandLine getCommandLine_With_Qc() throws ParseException {
        final String[] args = "-Q DEFQM -c JVM.DEF.SVRCONN".split(" ");
        return prepareCommandLine(args);
    }

    protected CommandLine getCommandLine_With_Qc_dstq() throws ParseException {
        final String[] args = "-Q DEFQM -c JVM.DEF.SVRCONN --dstq Q1".split(" ");
        return prepareCommandLine(args);
    }

    /**
     * Create chain with Connect -> YOUR_COMMAND -> Disconnect
     * @param context
     * @param command
     * @return
     */
    protected CommandChainMaker surroundSingleCommandWithConnectionAdvices(ExecutionContext context, Command command) {
        final CommandChainMaker maker = new CommandChainMaker(context);
        final AbstractCommand cmdConnect = new ConnectCommand();
        final AbstractCommand cmdDisconnect = new DisconnectCommand();
        return maker.addCommand(cmdConnect).addCommand(command).addCommand(cmdDisconnect);
    }


}
