package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.QueueingCapability;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.handler.NestedHandlerException;

import javax.inject.Inject;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static ru.codeunited.wmq.RFHConstants.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.10.14.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class})
public class PutCommandTest extends QueueingCapability {

    private final static String QUEUE = "RFH.QTEST.QGENERAL1";

    @Before @After
    public void cleanUp() throws Exception {
        cleanupQueue(QUEUE);
    }

    /** --dstq was missed. */
    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void testInsufficientParams$dstq() throws ParseException, MissedParameterException, CommandGeneralException, IncompatibleOptionsException, NestedHandlerException {
        commandChain
                .addCommand(connectCommand)
                .addCommand(putCommand)
                .addCommand(disconnectCommand);


        boolean exceptionOccured = false;
        try {
            commandChain.execute();
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

    /** -t or -p was missed. */
    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN --dstq RFH.QTEST.QGENERAL1")
    public void testInsufficientParams$p_t() throws ParseException, CommandGeneralException, IncompatibleOptionsException, NestedHandlerException {
        commandChain
                .addCommand(connectCommand)
                .addCommand(putCommand)
                .addCommand(disconnectCommand);

        boolean exceptionOccured = false;
        try {
            commandChain.execute();
        } catch (MissedParameterException pe) {
            final String[] ptParams = {OPT_PAYLOAD, OPT_STREAM, OPT_TEXT};
            assertTrue(
                    String.format("Parameter --%s or --%s or --%s are missed, but we got another error here. [%s]", OPT_TEXT, OPT_PAYLOAD, OPT_STREAM, pe.getMessage()),
                    Arrays.equals(pe.getLongNames(), ptParams));
            exceptionOccured = true;
        }

        if (!exceptionOccured) {
            throw new CommandGeneralException(MQPutCommand.class.getName() + " has no reaction for a missed -p or -t parameter.");
        }
    }
}
