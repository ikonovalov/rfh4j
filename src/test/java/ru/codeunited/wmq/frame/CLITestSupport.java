package ru.codeunited.wmq.frame;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.apache.commons.cli.*;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.ExecutionContext;
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
public final class CLITestSupport {



    public static CommandLineParser getCliParser() {
        return CLIFactory.createParser();
    }

    public static Options getOptions() {
        return CLIFactory.createOptions();
    }

    public static Injector getStandartInjector(ExecutionContext context) {
        return Guice.createInjector(new ContextModule(context), new CommandsModule());
    }

    public static CommandLine prepareCommandLine(String line) throws ParseException {
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

    public static CommandLine getCommandLine_With_Qc() throws ParseException {
        final String[] args = "-Q DEFQM -c JVM.DEF.SVRCONN".split(" ");
        return prepareCommandLine(args);
    }
}
