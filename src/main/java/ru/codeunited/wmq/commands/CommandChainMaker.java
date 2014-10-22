package ru.codeunited.wmq.commands;

import org.apache.commons.cli.CommandLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ikonovalov on 22.10.14.
 */
public class CommandChainMaker extends AbstractCommand {

    private static final Logger LOG = Logger.getLogger(CommandChainMaker.class.getName());

    private final List<Command> commandChain = new ArrayList<>();

    public CommandChainMaker(CommandLine commandLine) {
        setCommandLine(commandLine);
        setContext(new ExecutionContext());
    }

    public CommandChainMaker(CommandLine commandLine, ExecutionContext executionContext) {
        setCommandLine(commandLine);
        setContext(executionContext);
    }

    /**
     * Add new command to chain.
     * @param command
     * @return instance of CommandMaker.
     */
    public CommandChainMaker addCommand(Command command) {
        if (selfStateCheckOK()) {
            copyEnvironmentTo(command);
            commandChain.add(command);
        } else {
            throw new IllegalStateException("CommandMaker is in invalid state. Some basic parameters are not set.");
        }
        return this;
    }


    @Override
    public ReturnCode work() {
        final List<Command> unmodCommandChain = Collections.unmodifiableList(commandChain);
        List<ReturnCode> allReturnCodes = new ArrayList<>(unmodCommandChain.size());
        for (Command command : commandChain) {
            try {
                if (command.resolve()) {
                    ReturnCode rCode = command.execute();
                    allReturnCodes.add(rCode);
                } else {
                    allReturnCodes.add(command.getState());
                }
            } catch (CommandGeneralException e) {
                LOG.severe(e.getMessage());
                allReturnCodes.add(ReturnCode.FAILED);
                break;
            }

        }       ReturnCode finalReturnCode = scanIfFailed(allReturnCodes);
        return finalReturnCode;
    }

    @Override
    public boolean resolve() {
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
