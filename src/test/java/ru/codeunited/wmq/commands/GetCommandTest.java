package ru.codeunited.wmq.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.*;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.format.FormatterModule;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.handler.HandlerModule;
import ru.codeunited.wmq.handler.NestedHandlerException;
import ru.codeunited.wmq.messaging.MessagingModule;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ru.codeunited.wmq.RFHConstants.OPT_PAYLOAD;
import static ru.codeunited.wmq.RFHConstants.OPT_STREAM;
import static ru.codeunited.wmq.frame.CLITestSupport.prepareCommandLine;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 29.11.14.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class, MessagingModule.class, FormatterModule.class, HandlerModule.class})
public class GetCommandTest extends QueueingCapability {

    private final static String QUEUE = "RFH.QTEST.QGENERAL1";

    @Inject private ExecutionPlanBuilder executionPlanBuilder;

    @Test(expected = IncompatibleOptionsException.class)
    public void getIncompatibleParamsStreamAll() throws ParseException, IncompatibleOptionsException, CommandGeneralException, MissedParameterException, NestedHandlerException {
        final CommandLine cl = prepareCommandLine(String.format("-Q DEFQM --%s --all", OPT_STREAM));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MQGetCommand getCmd = (MQGetCommand) new MQGetCommand().setContext(executionContext);
        // should throw IncompatibleOptionsException here
        getCmd.execute();
    }

    @Test(expected = MissedParameterException.class) /* very synthetic test, not from real life */
    public void getMissedParameterExceptiontreamLimit10() throws ParseException, IncompatibleOptionsException, CommandGeneralException, MissedParameterException, NestedHandlerException {
        final CommandLine cl = prepareCommandLine(String.format("-Q DEFQM --%s --limit 10", OPT_STREAM));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MQGetCommand getCmd = (MQGetCommand) new MQGetCommand().setContext(executionContext);
        assertThat("shouldWait", getCmd.shouldWait(), is(false));
        assertThat("getMessagesCountLimit", getCmd.getMessagesCountLimit(1), is(10));
        assertThat("waitTime", getCmd.waitTime(), is(-1));
        // should throw MissedParameterException here from the work() of MQGetCommand
        getCmd.execute();
    }

    @Test
    public void justGetOneMessageNOWaitAndExit() throws ParseException {
        final CommandLine cl = prepareCommandLine(String.format("-Q DEFQM --%1$s --srcq %2$s", OPT_STREAM, QUEUE));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MQGetCommand getCmd = (MQGetCommand) new MQGetCommand().setContext(executionContext);
        assertThat(getCmd.isListenerMode(), is(false));
        assertThat(getCmd.shouldWait(), is(false));
        assertThat(getCmd.getMessagesCountLimit(1), is(1));
    }

    @Test
    public void justGetOneMessageWithWaitAndExit() throws ParseException {
        final CommandLine cl = prepareCommandLine(String.format("-Q DEFQM --%1$s --srcq %2$s --wait 1000", OPT_STREAM, QUEUE));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MQGetCommand getCmd = (MQGetCommand) new MQGetCommand().setContext(executionContext);
        assertThat(getCmd.isListenerMode(), is(false));
        assertThat(getCmd.shouldWait(), is(true));
        assertThat(getCmd.getMessagesCountLimit(1), is(1));
        assertThat(getCmd.waitTime(), is(1000));
    }

    @Test(expected = MissedParameterException.class)
    public void getMissedParameterException() throws ParseException, IncompatibleOptionsException, CommandGeneralException, MissedParameterException, NestedHandlerException {
        final CommandLine cl = prepareCommandLine(String.format("-Q DEFQM --%s", OPT_STREAM));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MQGetCommand getCmd = (MQGetCommand) new MQGetCommand().setContext(executionContext);
        // should throw IncompatibleOptionsException here
        getCmd.execute();
    }

    @Test(expected = MissedParameterException.class)
    public void streamOrPayloadMissed() throws ParseException, MissedParameterException, IncompatibleOptionsException, CommandGeneralException, NestedHandlerException {
        final CommandLine cl = prepareCommandLine("-Q DEFQM --srcq Q");
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        setup(executionContext);
        final ExecutionPlanBuilder executionPlanBuilder = injector.getInstance(ExecutionPlanBuilder.class);
        try {
            List<Command> commands = executionPlanBuilder.buildChain().getCommandChain();
            MQGetCommand getCmd = (MQGetCommand) commands.get(1);
            getCmd.execute();
        } catch (MissedParameterException missed) {
            assertThat(missed.getMessage(), equalTo(String.format("Option(s) [%s] [%s]  are missed.", OPT_PAYLOAD, OPT_STREAM)));
            throw missed;
        }
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN --srcq RFH.QTEST.QGENERAL1 --stream --limit -1")
    public void initListenerMode() throws MissedParameterException, ParseException {

        CommandChain chain = executionPlanBuilder.buildChain();
        List<Command> commands = chain.getCommandChain();
        MQGetCommand getCmd = (MQGetCommand) commands.get(1);

        assertThat("isListenerMode", getCmd.isListenerMode(), is(true));
        assertThat("shouldWait", getCmd.shouldWait(), is(true)); // engage MQGMO_WAIT
        assertThat("waitTime", getCmd.waitTime(), is(-1)); // engage MQWI_UNLIMITED
    }

    @Test(timeout = 20000)
    public void waitTwoMessages() throws ParseException, MissedParameterException, IncompatibleOptionsException, CommandGeneralException, ExecutionException, InterruptedException {

        branch(new Parallel.Branch() {
            @Override
            protected void perform() throws Exception {
                CommandLine cl = prepareCommandLine(String.format("%1$s --srcq %2$s --stream --limit 2 --wait 200", "-Q DEFQM -c JVM.DEF.SVRCONN", QUEUE));
                ExecutionContext executionContext = new CLIExecutionContext(cl);
                setup(executionContext);

                ExecutionPlanBuilder executionPlanBuilder = injector.getInstance(ExecutionPlanBuilder.class);
                CommandChain chain = executionPlanBuilder.buildChain();
                List<Command> commands = chain.getCommandChain();
                MQGetCommand getCmd = (MQGetCommand) commands.get(1);

                assertThat("isListenerMode", getCmd.isListenerMode(), is(false));
                assertThat("shouldWait", getCmd.shouldWait(), is(true));
                assertThat("waitTime", getCmd.waitTime(), is(200));

                chain.execute();
            }
        });

        branch(new Parallel.Branch(200) {
            @Override
            protected void perform() throws Exception {
                putToQueue(QUEUE);
            }
        });

        branch(new Parallel.Branch(300) {
            @Override
            protected void perform() throws Exception {
                putToQueue(QUEUE);
            }
        });

        parallel();

    }

    @Before
    @After
    public void cleanUp() throws Exception {
        cleanupQueue(QUEUE);
    }

}
