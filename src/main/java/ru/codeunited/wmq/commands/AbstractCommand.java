package ru.codeunited.wmq.commands;

import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.cli.ConsoleWriter;

import java.util.logging.Logger;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public abstract class AbstractCommand implements Command {

    protected ExecutionContext executionContext;

    protected CommandLine commandLine;

    protected ReturnCode currentState = ReturnCode.READY;

    protected static final Logger LOG = Logger.getLogger(Command.class.getName());

    /**
     * Check if single character parameter is passed in command line.
     *
     * @param option single character option.
     * @return true if passed, false if missed.
     */
    public boolean hasOption(char option) {
        return getCommandLine().hasOption(option);
    }

    public boolean hasOption(String option) {
        return getCommandLine().hasOption(option);
    }

    /**
     * Get single character parameter argument.
     *
     * @param option single character option.
     * @return String value of option if passed, null otherwise.
     */
    public String getOption(char option) {
        return getCommandLine().getOptionValue(option);
    }

    /**
     * Get value of long named parameter argument.
     *
     * @param option long option name.
     * @return String value of option if passed, null otherwise.
     */
    public String getOption(String option) {
        return getCommandLine().getOptionValue(option);
    }

    /**
     * Real work implementation for command.
     *
     * @throws CommandGeneralException if something goes wrong.
     */
    protected abstract void work() throws CommandGeneralException, MissedParameterException;

    @Override
    public final ReturnCode execute() throws CommandGeneralException, MissedParameterException {
        updateCurrentState(ReturnCode.EXECUTING);
        try {
            work();
            getConsoleWriter().flush();
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

    /**
     * Get current execution context.
     *
     * @return ExecutionContext
     */
    protected ExecutionContext getExecutionContext() {
        return executionContext;
    }

    /**
     * Get current command line. Already parsed.
     *
     * @return CommandLine
     */
    protected CommandLine getCommandLine() {
        return commandLine;
    }

    /**
     * Check inner command state.
     *
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
                            + (executionContext == null ? "SharedContext is null. " : "")
                            + (commandLine == null ? "CommandLine is null." : "")
            );
        }
    }

    /**
     * This is a revers of selfStateCheckOK method.
     *
     * @return true if inner test failed and false if all right.
     * @see AbstractCommand selfStateCheckOK
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

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + "]";
    }
}
