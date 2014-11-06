package ru.codeunited.wmq.commands;

import org.apache.commons.cli.CommandLine;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.11.14.
 */
public class CLIExecutionContext extends ExecutionContext {

    private final CommandLine commandLine;

    public CLIExecutionContext(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    protected CommandLine getCommandLine() {
        return commandLine;
    }

    @Override
    public boolean hasOption(String opt) {
        return commandLine.hasOption(opt);
    }

    @Override
    public boolean hasOption(char opt) {
        return commandLine.hasOption(opt);
    }

    @Override
    public boolean hasAnyOption(char...opts) {
        for (char c : opts) {
            if (hasOption(c))
                return true;
        }
        return false;
    }

    public String getOption(char option) {
        return getCommandLine().getOptionValue(option);
    }

    public String getOption(String option) {
        return getCommandLine().getOptionValue(option);
    }
}
