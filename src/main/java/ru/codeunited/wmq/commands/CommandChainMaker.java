package ru.codeunited.wmq.commands;

import org.apache.commons.cli.CommandLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class CommandChainMaker extends AbstractCommand {

    private final List<Command> commandChain = new ArrayList<>();

    public CommandChainMaker(CommandLine commandLine) {
        setCommandLine(commandLine);
        setContext(new ExecutionContext());
    }

    public CommandChainMaker(CommandLine commandLine, ExecutionContext executionContext) {
        setCommandLine(commandLine);
        setContext(executionContext);
    }

    private CommandChainMaker checkAndAdd(int index, Command command) {
        if (selfStateCheckOK()) {
            copyEnvironmentTo(command);
            commandChain.add(index, command);
            LOG.info("Adding " + command.getClass().getSimpleName() + " to chain...");
        } else {
            throw new IllegalStateException("CommandMaker is in invalid state. Some basic parameters are not set.");
        }
        return this;
    }

    /**
     * Add new command to chain.
     * @param command
     * @return instance of CommandMaker.
     */
    public CommandChainMaker addCommand(Command command) {
        return checkAndAdd(commandChain.size(), command);
    }

    public CommandChainMaker addCommand(int index, Command command) {
        return checkAndAdd(index, command);
    }

    /**
     * Adding new command after specified.
     * @param newCommand command for insert
     * @param afterThat after this command newCommand will be inserted.
     * @return CommandChainMaker
     */
    public CommandChainMaker addAfter(Command newCommand, Command afterThat) {
        if (commandChain.size() == 0 || afterThat == null) { // add like first element
            addCommand(newCommand);
        } else { // insert after
            for (int z = 0; z < commandChain.size(); z++) {
                if (afterThat == commandChain.get(z)) {
                    addCommand(z + 1, newCommand);
                    break;
                }
            }
        }
        return this;
    }

    /**
     * Get all loaded commands.
     * This list is unmodifiable!
     * @return List<Command>
     */
    public List<Command> getCommandChain() {
        return Collections.unmodifiableList(commandChain);
    }

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException {
        // fixing work size
        final List<Command> unmodCommandChain = getCommandChain();
        for (Command command : unmodCommandChain) {
            try {
                if (command.resolve()) {
                    command.execute();
                } else {
                    LOG.warning("Command skipped! [" + command.getClass().getSimpleName() + "]");
                }
            } catch (MissedParameterException | CommandGeneralException e) {
                LOG.severe(e.getMessage());
                throw e;
            }

        }
    }

    @Override
    public boolean resolve() {
        // always ready for action
        return true;
    }

    /**
     * Search failed return code. If found return it, otherwise return SUCCESS.
     * @param returnCodeses
     * @return
     */
    private ReturnCode scanIfFailed(List<ReturnCode> returnCodeses) {
        for (ReturnCode rCode : returnCodeses) {
            if (rCode == ReturnCode.FAILED)
                return rCode;
        }
        return ReturnCode.SUCCESS;
    }
}
