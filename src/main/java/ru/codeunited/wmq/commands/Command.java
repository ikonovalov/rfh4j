package ru.codeunited.wmq.commands;

import ru.codeunited.wmq.ExecutionContext;

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
     * Copy current command environment to another command.
     * From this -> another
     * @param anotherCommand - target Command
     */
    void copyEnvironmentTo(Command anotherCommand);

    /**
     * Execute this command.
     * @return - command execution result
     * @see ru.codeunited.wmq.commands.ReturnCode
     * @throws CommandGeneralException
     */
    ReturnCode execute() throws CommandGeneralException, MissedParameterException;

    /**
     * Analize input parameters and current environment and work decision.
     * @return true if command can be executed and false if not.
     */
    boolean resolve();

    /**
     * Get current Command state.
     * @see ru.codeunited.wmq.commands.ReturnCode
     * @return current ru.codeunited.wmq.commands.ReturnCode
     */
    ReturnCode getState();

}
