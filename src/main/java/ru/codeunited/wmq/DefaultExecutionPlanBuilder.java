package ru.codeunited.wmq;

import ru.codeunited.wmq.commands.*;

import java.util.logging.Logger;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 27.10.14.
 */
public class DefaultExecutionPlanBuilder implements ExecutionPlanBuilder {

    private static final Logger LOG = Logger.getLogger(DefaultExecutionPlanBuilder.class.getName());

    private final ExecutionContext executionContext;

    public DefaultExecutionPlanBuilder(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    @Override
    public CommandChainMaker buildChain() {
        final CommandChainMaker chain = new CommandChainMaker(executionContext);
        // just a scratch
        if (executionContext.hasOption('Q') || executionContext.hasOption("config")) { // need to connect to queue manager
            chain
                    .addCommand(new ConnectCommand())
                    .addCommand(new DisconnectCommand());
        }

        if (executionContext.hasOption("--dstq") && (executionContext.hasAnyOption('t', 'p') )) {
            chain.addAfter(new MQPutCommand(), chain.getCommandChain().get(0));
        }

        LOG.fine("Command list: " + chain.getCommandChain().toString());

        return chain;
    }

}
