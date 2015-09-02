package ru.codeunited.wmq.commands;

import com.google.common.eventbus.EventBus;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.bus.MainBus;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.handler.NestedHandlerException;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public abstract class AbstractCommand implements Command {

    protected ExecutionContext executionContext;

    protected ReturnCode currentState = ReturnCode.READY;

    protected static final Logger LOG = Logger.getLogger(Command.class.getName());


    @Inject @MainBus
    private EventBus eventBus;

    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Real work implementation for command.
     *
     * @throws CommandGeneralException if something goes wrong.
     */
    protected abstract void work() throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException, NestedHandlerException;

    @Override
    public final ReturnCode execute() throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException, NestedHandlerException {
        updateCurrentState(ReturnCode.EXECUTING);
        try {
            validateOptions();
            work();
            getConsoleWriter().flush();
            updateCurrentState(ReturnCode.SUCCESS);
        } catch (Exception e) {
            updateCurrentState(ReturnCode.FAILED);
            throw e;
        }
        return getState();
    }

    public void copyEnvironmentTo(Command anotherCommand) {
        anotherCommand.setContext(getExecutionContext());
    }

    @Override
    @Inject
    public Command setContext(ExecutionContext context) {
        if (selfStateCheckFailed())
            this.executionContext = context;
        return this;
    }

    /**
     * Implements parameters validation logic here. This method should be executed be external mechanism out of command scope.
     *
     * @throws IncompatibleOptionsException
     * @throws MissedParameterException
     */
    protected void validateOptions() throws IncompatibleOptionsException, MissedParameterException {

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
     * Check inner command state.
     *
     * @return true if all right and false if something wrong.
     */
    public boolean selfStateCheckOK() {
        return (executionContext != null);
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
            LOG.fine(implClassName + " changing state [" + getState() + "] -> [" + newState + "]");
            this.currentState = newState;
        }

    }

    protected void raiseIncompatibeException(String errorString) throws IncompatibleOptionsException {
        throw new IncompatibleOptionsException(errorString);
    }

    protected void raiseMissedParameters(String message, String...parameters) throws MissedParameterException {
        throw new MissedParameterException(parameters).withMessage(message);
    }

    protected void raiseMissedParameters(String... parameters) throws MissedParameterException {
        raiseMissedParameters(null, parameters);
    }

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + "]";
    }
}
