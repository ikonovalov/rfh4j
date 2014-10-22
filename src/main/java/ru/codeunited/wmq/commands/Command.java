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
     * @param anotherCommand
     */
    void copyEnvironmentTo(Command anotherCommand);

    ReturnCode execute() throws CommandGeneralException;

    /**
     * Analize input parameters and current environment and work decision.
     * @return
     */
    boolean resolve();

    ReturnCode getState();

}
