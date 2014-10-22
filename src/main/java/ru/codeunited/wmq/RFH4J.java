package ru.codeunited.wmq;

import org.apache.commons.cli.*;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.commands.*;

/**
 * Created by ikonovalov on 21.10.14.
 */
public class RFH4J {

    private static Options options = CLIFactory.createOptions();

    private static CommandLineParser cliParser = CLIFactory.createParser();

    private static ConsoleWriter consoleWriter = new ConsoleWriter(System.out, System.err);

    public static void main(String[] args) {
        try {
            final CommandLine cli = cliParser.parse(options, args);
            if (cli.hasOption('h')) {
                CLIFactory.showHelp();
            } else {
                final ExecutionContext context = new ExecutionContext();
                context.setConsoleWriter(consoleWriter);
                CommandChainMaker commandMaker = new CommandChainMaker(cli, context)
                        .addCommand(new ConnectCommand())
                        .addCommand(new PutFileCommand())
                        .addCommand(new DisconnectCommand());
                ReturnCode code = commandMaker.work();

                consoleWriter.writeln(code).flash();
            }

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            CLIFactory.showHelp();
        }

    }
}
