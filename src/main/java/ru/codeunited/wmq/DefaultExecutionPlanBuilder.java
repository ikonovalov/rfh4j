package ru.codeunited.wmq;

import static ru.codeunited.wmq.RFHConstants.*;

import ru.codeunited.wmq.commands.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.logging.Logger;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 27.10.14.
 */
@Singleton
public class DefaultExecutionPlanBuilder implements ExecutionPlanBuilder {

    private static final Logger LOG = Logger.getLogger(DefaultExecutionPlanBuilder.class.getName());

    private final ExecutionContext executionContext;

    @ConnectCommand
    @Inject  private Provider<Command> connectCommandProvider;

    @DisconnectCommand
    @Inject private Provider<Command> disconnectCommandProvider;

    @PutCommand
    @Inject private Provider<Command> putCommandProvider;

    @GetCommand
    @Inject private Provider<Command> getCommandProvider;

    @InspectCommand
    @Inject private Provider<Command> inspectCommandProvider;

    @Inject private Provider<CommandChain> commandChainProvider;

    @Inject
    public DefaultExecutionPlanBuilder(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    @Override
    public CommandChain buildChain() throws MissedParameterException {
        final CommandChain chain = commandChainProvider.get();

        // create connect/disconnect commands
        if (executionContext.hasOption(OPT_QMANAGER) ||
                executionContext.hasOption(OPT_CONFIG) || MQFilePropertiesComposer.isDefaultConfigAvailable()) { // need to connect to queue manager
            chain
                    .addCommand(connectCommandProvider.get())
                    .addCommand(disconnectCommandProvider.get());
        } else {
            // this is mandatory arguments (one and two)
            throw new MissedParameterException(OPT_QMANAGER, OPT_CONFIG).withMessage("And default.properties not available also.");
        }

        final String[] activeActions = {OPT_SRCQ, OPT_DSTQ, OPT_LIST_QLOCAL};
        if (executionContext.hasntOption(activeActions)) {
            throw new MissedParameterException(activeActions);
        }

        // insert PUT command (if srcq not present) - simple PUT case
        if (executionContext.hasOption(OPT_DSTQ) && !executionContext.hasOption(OPT_SRCQ)) {
            chain.addAfter(putCommandProvider.get(), chain.getCommandChain().get(0));
        }

        // insert GET command (if dstq not present) - simple GET case
        if (executionContext.hasOption(OPT_SRCQ) && !executionContext.hasOption(OPT_DSTQ)) {
            chain.addAfter(getCommandProvider.get(), chain.getCommandChain().get(0));
        }

        if (executionContext.hasOption(OPT_LIST_QLOCAL) && executionContext.hasntOption(OPT_SRCQ, OPT_DSTQ)) {
            chain.addAfter(inspectCommandProvider.get(), chain.getCommandChain().get(0));
        }

        LOG.fine("Command list: " + chain.getCommandChain().toString());

        return chain;
    }

}
