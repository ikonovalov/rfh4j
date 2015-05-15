package ru.codeunited.wmq;

import com.google.inject.Guice;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.format.FormatterModule;
import ru.codeunited.wmq.handler.HandlerModule;
import ru.codeunited.wmq.handler.NestedHandlerException;
import ru.codeunited.wmq.messaging.MessagingModule;

import javax.inject.Inject;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 21.10.14.
 */
public class RFH4J {

    private final Options options = CLIFactory.createOptions();

    @Inject
    private ConsoleWriter consoleWriter;

    @Inject
    private ExecutionPlanBuilder executionPlanBuilder;

    public static void main(String[] args) {
        RFH4J application = new RFH4J();
        application.start(args);
    }

    public void start(String... args) {

        try {
            CommandLineParser cliParser = CLIFactory.createParser();
            CommandLine cli = cliParser.parse(options, args);

            if (cli.hasOption('h') || cli.getOptions().length == 0) {
                CLIFactory.showHelp();
            } else {
                final ExecutionContext context = new CLIExecutionContext(cli);
                Guice.createInjector(
                        new ContextModule(context),
                        new CommandsModule(),
                        new FormatterModule(),
                        new HandlerModule(),
                        new MessagingModule()
                ).injectMembers(this);

                context.setConsoleWriter(consoleWriter);

                final CommandChain commandMaker = executionPlanBuilder.buildChain();

                commandMaker.execute();
            }

        } catch (NestedHandlerException | MissedParameterException | ParseException | CommandGeneralException | IncompatibleOptionsException e) {
            System.err.println("Error occurred. Details: " + e.getMessage());
            System.err.flush();
        }
    }
}
