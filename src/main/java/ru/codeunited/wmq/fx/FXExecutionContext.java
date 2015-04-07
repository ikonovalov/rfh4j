package ru.codeunited.wmq.fx;

import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.cli.CLIExecutionContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 03.04.15.
 */
public class FXExecutionContext extends CLIExecutionContext {

    public FXExecutionContext(CommandLine commandLine) {
        super(commandLine);
    }
}
