package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.commands.*;

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

    public boolean hasOption(String opt) {
        return commandLine.hasOption(opt);
    }

    public boolean hasOption(char opt) {
        return commandLine.hasOption(opt);
    }

    public boolean hasAnyOption(char...opts) {
        for (char c : opts) {
            if (hasOption(c))
                return true;
        }
        return false;
    }

    @Override
    public CommandChainMaker buildChain() {
        final CommandChainMaker chain = new CommandChainMaker(commandLine, executionContext);
        // just a scratch
        if (hasOption('Q')) { // need to connect to queue manager
            chain
                    .addCommand(new ConnectCommand())
                    .addCommand(new DisconnectCommand());
        }

        if (hasOption("--dstq") && (hasAnyOption('t', 'p') )) {
            chain.addAfter(new MQPutCommand(), chain.getCommandChain().get(0));
        }
        return chain;
    }

}
