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
    public CommandChainMaker buildChain() throws MissedParameterException {
        final CommandChainMaker chain = new CommandChainMaker(executionContext);

        // create connect/disconnect commands
        if (executionContext.hasOption('Q') || executionContext.hasOption("config") || ConnectCommand.isDefaultConfigAvailable()) { // need to connect to queue manager
            chain
                    .addCommand(new ConnectCommand())
                    .addCommand(new DisconnectCommand());
        } else {
            // this is mandatory arguments (one and two)
            throw new MissedParameterException("qmanager", "config").withMessage("And default.properties not available also.");
        }

        // insert PUT command
        if (executionContext.hasOption("dstq") && (executionContext.hasAnyOption('t', 'p', 's') )) {
            chain.addAfter(new MQPutCommand(), chain.getCommandChain().get(0));
        }

        LOG.fine("Command list: " + chain.getCommandChain().toString());

        return chain;
    }

}
