package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class CLITestSupport {
    private final Options options = CLIFactory.createOptions();

    private final CommandLineParser cliParser = CLIFactory.createParser();

    public ConsoleWriter getConsoleWriter() {
        return consoleWriter;
    }

    CommandLineParser getCliParser() {
        return cliParser;
    }

    Options getOptions() {
        return options;
    }

    private final ConsoleWriter consoleWriter = new ConsoleWriter(System.out, System.err);

    CommandLine getCommandLine_With_Qc() throws ParseException {
        final String[] args = "-Q DEFQM -c JVM.DEF.SVRCONN".split(" ");
        return getCliParser().parse(getOptions(), args);
    }
}
