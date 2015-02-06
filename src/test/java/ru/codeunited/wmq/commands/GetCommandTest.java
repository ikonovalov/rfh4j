package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.codeunited.wmq.*;
import ru.codeunited.wmq.cli.CLIExecutionContext;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static ru.codeunited.wmq.cli.CLIFactory.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 29.11.14.
 */
public class GetCommandTest extends QueueingCapability {

    private final static String QUEUE = "RFH.QTEST.QGENERAL1";

    @Test(expected = IncompatibleOptionsException.class)
    public void getIncompatibleParamsStreamAll() throws ParseException, IncompatibleOptionsException, CommandGeneralException, MissedParameterException {
        final CommandLine cl = prepareCommandLine(String.format("-Q DEFQM --%s --all", OPT_STREAM));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MQGetCommand getCmd = (MQGetCommand) new MQGetCommand().setContext(executionContext);
        // should throw IncompatibleOptionsException here
        getCmd.execute();
    }

    @Test(expected = MissedParameterException.class) /* very synthetic test, not from real life */
    public void getMissedParameterExceptiontreamLimit10() throws ParseException, IncompatibleOptionsException, CommandGeneralException, MissedParameterException {
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
    public void getMissedParameterException() throws ParseException, IncompatibleOptionsException, CommandGeneralException, MissedParameterException {
        final CommandLine cl = prepareCommandLine(String.format("-Q DEFQM --%s", OPT_STREAM));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MQGetCommand getCmd = (MQGetCommand) new MQGetCommand().setContext(executionContext);
        // should throw IncompatibleOptionsException here
        getCmd.execute();
    }

    @Test(expected = MissedParameterException.class)
    public void streamOrPayloadMissed() throws ParseException, MissedParameterException, IncompatibleOptionsException, CommandGeneralException {
        final CommandLine cl = prepareCommandLine("-Q DEFQM --srcq Q");
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(executionContext);
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
    public void initListenerMode() throws MissedParameterException, ParseException {
        final CommandLine cl = prepareCommandLine(String.format("%1$s --srcq %2$s --stream --limit -1", connectionParameter(), QUEUE));
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(executionContext);
        final CommandChain chain = executionPlanBuilder.buildChain();
        final List<Command> commands = chain.getCommandChain();
        final MQGetCommand getCmd = (MQGetCommand) commands.get(1);
        assertThat("isListenerMode", getCmd.isListenerMode(), is(true));
        assertThat("shouldWait", getCmd.shouldWait(), is(true)); // engage MQGMO_WAIT
        assertThat("waitTime", getCmd.waitTime(), is(-1)); // engage MQWI_UNLIMITED
    }

    @Test(timeout = 20000)
    public void waitTwoMessages() throws ParseException, MissedParameterException, IncompatibleOptionsException, CommandGeneralException, ExecutionException, InterruptedException {

        branch(new Parallel.Branch() {
            @Override
            protected void perform() throws Exception {
                final CommandLine cl = prepareCommandLine(String.format("%1$s --srcq %2$s --stream --limit 2 --wait 3000", connectionParameter(), QUEUE));
                final ExecutionContext executionContext = new CLIExecutionContext(cl);
                final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(executionContext);
                final CommandChain chain = executionPlanBuilder.buildChain();
                final List<Command> commands = chain.getCommandChain();
                final MQGetCommand getCmd = (MQGetCommand) commands.get(1);
                assertThat("isListenerMode", getCmd.isListenerMode(), is(false));
                assertThat("shouldWait", getCmd.shouldWait(), is(true));
                assertThat("waitTime", getCmd.waitTime(), is(3000));

                chain.execute();
            }
        });

        branch(new Parallel.Branch(2000) {
            @Override
            protected void perform() throws Exception {
                putToQueue(QUEUE);
            }
        });

        branch(new Parallel.Branch(5000) {
            @Override
            protected void perform() throws Exception {
                putToQueue(QUEUE);
            }
        });

        parallel();


    }

    @Before
    @After
    public void cleanUp() throws MissedParameterException, IncompatibleOptionsException, CommandGeneralException, MQException, ParseException {
        cleanupQueue(QUEUE);
    }

}
