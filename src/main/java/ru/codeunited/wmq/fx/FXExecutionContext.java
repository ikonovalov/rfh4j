package ru.codeunited.wmq.fx;

import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.RFHFX;
import ru.codeunited.wmq.cli.CLIExecutionContext;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 03.04.15.
 */
public class FXExecutionContext extends CLIExecutionContext {

    private static FXExecutionContext INSTANCE;

    private final RFHFX application;

    public static synchronized FXExecutionContext create(CommandLine commandLine, RFHFX application) {
        if (INSTANCE == null) {
            INSTANCE = new FXExecutionContext(commandLine, application);
        } else {
            throw new IllegalStateException(FXExecutionContext.class.getName() + " already created.");
        }
        return INSTANCE;
    }

    public static FXExecutionContext getInstance() {
        return INSTANCE;
    }

    private FXExecutionContext(CommandLine commandLine, RFHFX application) {
        super(commandLine);
        this.application = application;
    }

    public RFHFX getApplication() {
        return application;
    }
}
