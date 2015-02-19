package ru.codeunited.wmq.commands;

import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.handler.NestedHandlerException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public interface Command {

    /**
     * Set current command context.
     * @param context
     */
    AbstractCommand setContext(ExecutionContext context);

    /**
     * Execute this command.
     * @return - command execution result
     * @see ru.codeunited.wmq.commands.ReturnCode
     * @throws CommandGeneralException
     */
    ReturnCode execute() throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException, NestedHandlerException;

    /**
     * Get current Command state.
     * @see ru.codeunited.wmq.commands.ReturnCode
     * @return current ru.codeunited.wmq.commands.ReturnCode
     */
    ReturnCode getState();

}
