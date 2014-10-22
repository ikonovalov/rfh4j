package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;

/**
 * Created by ikonovalov on 23.10.14.
 */
public class CLITestSupport {
    private Options options = CLIFactory.createOptions();

    private CommandLineParser cliParser = CLIFactory.createParser();

    public ConsoleWriter getConsoleWriter() {
        return consoleWriter;
    }

    public CommandLineParser getCliParser() {
        return cliParser;
    }

    public Options getOptions() {
        return options;
    }

    private ConsoleWriter consoleWriter = new ConsoleWriter(System.out, System.err);
}
