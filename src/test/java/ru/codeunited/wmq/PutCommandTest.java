package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.commands.CommandChainMaker;
import ru.codeunited.wmq.commands.CommandGeneralException;
import ru.codeunited.wmq.commands.MQPutCommand;
import ru.codeunited.wmq.commands.ParameterException;

import static org.junit.Assert.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
public class PutCommandTest extends CLITestSupport {

    @Test
    public void testInsuficientParams() throws ParseException, ParameterException, CommandGeneralException {
        final CommandLine commandLine = getCommandLine_With_Qc();
        // missed --dstq
        final MQPutCommand putCommand = new MQPutCommand();
        final CommandChainMaker maker = surroundSingleCommandWithConnectionAdvices(commandLine, putCommand);
        try {
            maker.execute();
        } catch (ParameterException pe) {
            assertTrue("Parameter --dstq should be missed, but really " + pe.getMessage(), pe.getLongName().equals("dstq"));
        }
    }
}
