package ru.codeunited.wmq.commands;

import org.apache.commons.cli.CommandLine;

/**
 * Created by ikonovalov on 22.10.14.
 */
public interface Command {

    void setContext(ExecutionContext context);

    void setCommandLine(CommandLine cl);

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
    ReturnCode execute() throws CommandGeneralException;

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
