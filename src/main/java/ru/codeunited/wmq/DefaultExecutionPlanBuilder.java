package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.commands.CommandChainMaker;
import ru.codeunited.wmq.commands.ConnectCommand;
import ru.codeunited.wmq.commands.DisconnectCommand;
import ru.codeunited.wmq.commands.ExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 27.10.14.
 */
public class DefaultExecutionPlanBuilder implements ExecutionPlanBuilder {

    private final ExecutionContext executionContext;

    private final CommandLine commandLine;

    public DefaultExecutionPlanBuilder(ExecutionContext executionContext, CommandLine commandLine) {
        this.executionContext = executionContext;
        this.commandLine = commandLine;
    }

    @Override
    public CommandChainMaker buildChain() {
        final CommandChainMaker chain = new CommandChainMaker(commandLine, executionContext);
        // just a scratch
        if (commandLine.hasOption('Q')) { // need to connect to queue manager
            chain
                    .addCommand(new ConnectCommand())
                    .addCommand(new DisconnectCommand());
        }
        return chain;
    }

}
