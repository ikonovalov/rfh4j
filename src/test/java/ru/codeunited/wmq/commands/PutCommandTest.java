package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.codeunited.wmq.CLITestSupport;
import ru.codeunited.wmq.QueueingCapability;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;

import java.util.Arrays;

import static org.junit.Assert.*;
import static ru.codeunited.wmq.cli.CLIFactory.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
public class PutCommandTest extends QueueingCapability {

    private final static String QUEUE = "RFH.QTEST.QGENERAL1";

    @Before
    @After
    public void cleanUp() throws MissedParameterException, IncompatibleOptionsException, CommandGeneralException, MQException, ParseException {
        cleanupQueue(QUEUE);
    }

    @Test
    /**
     * --dstq was missed.
     */
    public void testInsufficientParams$dstq() throws ParseException, MissedParameterException, CommandGeneralException, IncompatibleOptionsException {
        final CommandLine commandLine = getCommandLine_With_Qc();
        // missed --dstq
        final MQPutCommand putCommand = new MQPutCommand();
        final CommandChain maker = surroundSingleCommandWithConnectionAdvices(new CLIExecutionContext(commandLine), putCommand);
        boolean exceptionOccured = false;
        try {
            maker.execute();
        } catch (MissedParameterException pe) {
            final String[] dstqParam = {"dstq"};
            assertTrue(
                    "Parameter --dstq should be missed, but really " + pe.getMessage(),
                    Arrays.equals(dstqParam, pe.getLongNames()));
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
    public void testInsufficientParams$p_t() throws ParseException, CommandGeneralException, IncompatibleOptionsException {
        final CommandLine commandLine = getCommandLine_With_Qc_dstq();
        final MQPutCommand putCommand = new MQPutCommand();
        final CommandChain maker = surroundSingleCommandWithConnectionAdvices(new CLIExecutionContext(commandLine), putCommand);
        boolean exceptionOccured = false;
        try {
            maker.execute();
        } catch (MissedParameterException pe) {
            final String[] ptParams = {OPT_PAYLOAD, OPT_STREAM, "text"};
            assertTrue(
                    String.format("Parameter --%s or --%s or --%s are missed, but we got another error here. [%s]", "text", OPT_PAYLOAD, OPT_STREAM, pe.getMessage()),
                    Arrays.equals(pe.getLongNames(), ptParams));
            exceptionOccured = true;
        }

        if (!exceptionOccured) {
            throw new CommandGeneralException(MQPutCommand.class.getName() + " has no reaction for a missed -p or -t parameter.");
        }
    }
}
