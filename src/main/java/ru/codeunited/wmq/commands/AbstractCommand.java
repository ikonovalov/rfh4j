package ru.codeunited.wmq.commands;

import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.cli.ConsoleWriter;

import java.util.logging.Logger;

/**
 * Created by ikonovalov on 22.10.14.
 */
public abstract class AbstractCommand implements Command {

    private ExecutionContext executionContext;

    private CommandLine commandLine;

    private ReturnCode currentState = ReturnCode.READY;

    protected static Logger LOG = Logger.getLogger(Command.class.getName());

    /**
     * Real work implementation for command.
     * @throws CommandGeneralException if something goes wrong.
     */
    protected abstract void work() throws CommandGeneralException;

    @Override
    public final ReturnCode execute() throws CommandGeneralException {
        updateCurrentState(ReturnCode.EXECUTING);
        try {
            work();
            updateCurrentState(ReturnCode.SUCCESS);
        } catch (Exception e) {
            updateCurrentState(ReturnCode.FAILED);
            throw e;
        }
        return getState();
    }

    @Override
    public void copyEnvironmentTo(Command anotherCommand) {
        anotherCommand.setContext(getExecutionContext());
        anotherCommand.setCommandLine(getCommandLine());
    }

    @Override
    public void setContext(ExecutionContext context) {
        if (selfStateCheckFailed())
            this.executionContext = context;
    }

    @Override
    public void setCommandLine(CommandLine cl) {
        if (selfStateCheckFailed())
            this.commandLine = cl;
    }

    protected ExecutionContext getExecutionContext() {
        return executionContext;
    }

    protected CommandLine getCommandLine() {
        return commandLine;
    }

    /**
     * Check inner command state.
     * @return true if all right and false if something wrong.
     */
    public boolean selfStateCheckOK() {
        return (executionContext != null && commandLine != null);
    }

    /**
     * Check inner command environment and throw exception if something wrong.
     */
    public void selfStateCheckOKForced() {
        if (executionContext == null || commandLine == null) {
            throw new IllegalStateException(
                    "Command is in a illegal state. "
                    + (executionContext == null ? "SharedContex is null. " : "")
                    + (commandLine == null ? "CommandLine is null." : "")
            );
        }
    }

    /**
     * This is a revers of selfStateCheckOK method.
     * @see AbstractCommand selfStateCheckOK
     * @return true if inner test failed and false if all right.
     */
    public boolean selfStateCheckFailed() {
        return !selfStateCheckOK();
    }

    public ConsoleWriter getConsoleWriter() {
        return getExecutionContext().getConsoleWriter();
    }

    public ReturnCode getState() {
        return currentState;
    }

    public void updateCurrentState(ReturnCode newState) {
        final String implClassName = this.getClass().getSimpleName();
        if (newState != null && currentState == newState) {
            LOG.warning(implClassName + " perform strange state changing form " + currentState + " to " + newState);
        } else {
            LOG.info(implClassName + " changing state [" + getState() + "] -> [" + newState + "]");
            this.currentState = newState;
        }

    }
}
