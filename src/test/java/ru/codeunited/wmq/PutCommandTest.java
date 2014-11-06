package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
public class PutCommandTest extends CLITestSupport {

    @Test
    /**
     * --dstq was missed.
     */
    public void testInsufficientParams$dstq() throws ParseException, MissedParameterException, CommandGeneralException {
        final CommandLine commandLine = getCommandLine_With_Qc();
        // missed --dstq
        final MQPutCommand putCommand = new MQPutCommand() {
            @Override
            public boolean resolve() { /** we should override it because chain will reject this command without parameters **/
                return true;
            }
        };
        final CommandChainMaker maker = surroundSingleCommandWithConnectionAdvices(new CLIExecutionContext(commandLine), putCommand);
        boolean exceptionOccured = false;
        try {
            maker.execute();
        } catch (MissedParameterException pe) {
            final String[] dstqParam = {"dstq"};
            assertTrue(
                    "Parameter --dstq should be missed, but really " + pe.getMessage(),
                    Arrays.equals(dstqParam, pe.getLongName()));
            exceptionOccured = true;
        }
        if (!exceptionOccured) {
            throw new CommandGeneralException(MQPutCommand.class.getName() + " has no reaction for a missed --dstq parameter.");
        }
    }

    @Test
    /**
     * -t or -p was missed.
     */
    public void testInsufficientParams$p_t() throws ParseException, CommandGeneralException {
        final CommandLine commandLine = getCommandLine_With_Qc_dstq();
        final MQPutCommand putCommand = new MQPutCommand() {
            @Override
            public boolean resolve() { /** we should override it because chain will reject this command without parameters **/
                return true;
            }
        };
        final CommandChainMaker maker = surroundSingleCommandWithConnectionAdvices(new CLIExecutionContext(commandLine), putCommand);
        boolean exceptionOccured = false;
        try {
            maker.execute();
        } catch (MissedParameterException pe) {
            final char[] ptParams = {'p','t'};
            assertTrue(
                    "Parameter -t or -p are missed, but we got another error here. [" + pe.getMessage() + "]",
                    Arrays.equals(pe.getSingleCharName(), ptParams));
            exceptionOccured = true;
        }

        if (!exceptionOccured) {
            throw new CommandGeneralException(MQPutCommand.class.getName() + " has no reaction for a missed -p or -t parameter.");
        }
    }
}
