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
    public CommandChain buildChain() throws MissedParameterException {
        final CommandChain chain = new CommandChain(executionContext);

        // create connect/disconnect commands
        if (executionContext.hasOption("qmanager") ||
                executionContext.hasOption("config") || MQConnectCommand.isDefaultConfigAvailable()) { // need to connect to queue manager
            chain
                    .addCommand(new MQConnectCommand())
                    .addCommand(new MQDisconnectCommand());
        } else {
            // this is mandatory arguments (one and two)
            throw new MissedParameterException("qmanager", "config").withMessage("And default.properties not available also.");
        }

        final String[] activeActions = {"srcq", "dstq", "lslq"};
        if (executionContext.hasntOption(activeActions)) {
            throw new MissedParameterException(activeActions);
        }

        // insert PUT command (if srcq not present) - simple PUT case
        if (executionContext.hasOption("dstq") && !executionContext.hasOption("srcq")) {
            chain.addAfter(new MQPutCommand(), chain.getCommandChain().get(0));
        }

        // insert GET command (if dstq not present) - simple GET case
        if (executionContext.hasOption("srcq") && !executionContext.hasOption("dstq")) {
            chain.addAfter(new MQGetCommand(), chain.getCommandChain().get(0));
        }

        if (executionContext.hasOption("lslq") && executionContext.hasntOption("srcq", "dstq")) {
            chain.addAfter(new MQInspectCommand(), chain.getCommandChain().get(0));
        }

        LOG.fine("Command list: " + chain.getCommandChain().toString());

        return chain;
    }

}
