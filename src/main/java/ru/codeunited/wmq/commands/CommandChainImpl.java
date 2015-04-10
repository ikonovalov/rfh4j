package ru.codeunited.wmq.commands;

import com.google.inject.Inject;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.handler.NestedHandlerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class CommandChainImpl extends AbstractCommand implements CommandChain {

    private final List<Command> commandChain = new ArrayList<>();

    public CommandChainImpl() {
        super();
    }

    private CommandChainImpl checkAndAdd(int index, Command command) {
        if (selfStateCheckOK()) {
            copyEnvironmentTo(command);
            commandChain.add(index, command);
            LOG.fine("Adding " + command.getClass().getSimpleName() + " to chain...");
        } else {
            throw new IllegalStateException("CommandMaker is in invalid state. Some basic parameters are not set.");
        }
        return this;
    }

    /**
     * Add new command to chain.
     * @param command new command instance.
     * @return instance of CommandMaker.
     */
    @Override
    public CommandChainImpl addCommand(Command command) {
        return checkAndAdd(commandChain.size(), command);
    }

    @Override
    public CommandChainImpl addCommand(int index, Command command) {
        return checkAndAdd(index, command);
    }

    /**
     * Adding new command after specified.
     * @param newCommand command for insert
     * @param afterThat after this command newCommand will be inserted.
     * @return CommandChainMaker
     */
    @Override
    public CommandChainImpl addAfter(Command newCommand, Command afterThat) {
        if (commandChain.size() == 0 || afterThat == null) { // add like first element
            addCommand(newCommand);
        } else { // insert after
            for (int z = 0; z < commandChain.size(); z++) {
                if (afterThat == commandChain.get(z)) { // compare instances!
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
    @Override
    public List<Command> getCommandChain() {
        return Collections.unmodifiableList(commandChain);
    }

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException, NestedHandlerException {
        // fixing work size
        final List<Command> unmodCommandChain = getCommandChain();
        for (Command command : unmodCommandChain) {
            command.execute();
        }
    }
}
