package ru.codeunited.wmq;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.*;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.handler.NestedHandlerException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 21.10.14.
 */
public class RFH4J {

    private final Options options = CLIFactory.createOptions();

    private final CommandLineParser cliParser = CLIFactory.createParser();

    private final ConsoleWriter consoleWriter = new ConsoleWriter(System.out, System.err);

    public static void main(String[] args) {
        RFH4J application = new RFH4J();
        application.start(args);
    }

    public void start(String... args) {

        try {
            final CommandLine cli = cliParser.parse(options, args);
            if (cli.hasOption('h') || cli.getOptions().length == 0) {
                CLIFactory.showHelp();
            } else {
                final ExecutionContext context = new CLIExecutionContext(cli);
                final Injector injector = Guice.createInjector(new ContextModule(context), new CommandsModule());

                context.setConsoleWriter(consoleWriter);
                final ExecutionPlanBuilder executionPlanBuilder = injector.getInstance(ExecutionPlanBuilder.class);
                final CommandChain commandMaker = executionPlanBuilder.buildChain();
                commandMaker.execute();
            }

        } catch (NestedHandlerException | MissedParameterException | ParseException | CommandGeneralException | IncompatibleOptionsException e) {
            consoleWriter.errorln("Error occurred. ").errorln("Details: " + e.getMessage());
            consoleWriter.end().flush();
        }
    }
}
