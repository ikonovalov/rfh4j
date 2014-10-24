package ru.codeunited.wmq;

import org.apache.commons.cli.*;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.commands.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 21.10.14.
 */
public class RFH4J {

    private static final Options options = CLIFactory.createOptions();

    private static final CommandLineParser cliParser = CLIFactory.createParser();

    private static final ConsoleWriter consoleWriter = new ConsoleWriter(System.out, System.err);

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
                        .addCommand(new MQPutCommand())
                        .addCommand(new DisconnectCommand());
               commandMaker.execute();
            }

        } catch (MissedParameterException | ParseException | CommandGeneralException e) {
            consoleWriter.errorln(e.getMessage());
            CLIFactory.showHelp();
        }

    }
}
