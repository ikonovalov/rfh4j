package ru.codeunited.wmq.cli;

import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.ExecutionContext;

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

    @Override
    public boolean hasOption(String opt) {
        return commandLine.hasOption(opt);
    }

    @Override
    public boolean hasOption(char opt) {
        return commandLine.hasOption(opt);
    }


    public String getOption(char option) {
        return commandLine.getOptionValue(option);
    }

    public String getOption(String option) {
        return commandLine.getOptionValue(option).trim();
    }

    @Override
    public ExecutionContext putOption(String key, String value) {
        throw new RuntimeException("Pass options via CLI, not set in a program");
    }
}
