package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.10.14.
 */
public class BuildExecutionChainTest extends CLITestSupport {

    public void assertThatCommandInstanceOf(Command command, Class shouldByClass) {
        assertThat(
                "Wrong command order. Here should be " + shouldByClass.getSimpleName() + " but there is " + command.getClass().getName(),
                command, instanceOf(shouldByClass));
    }

    public void assertThatCommandInstanceOf(List<Command> command, Class...shouldByClass) {
        final List<Class> commandClasses = Arrays.asList(shouldByClass);
        assertThat("Command list is null", command, notNullValue());
        assertThat("Command list size is empty", command.size(), not(0));
        assertThat("Command list != classes list", command.size(), is(commandClasses.size()));
        for (int z =0; z < command.size(); z++) {
            assertThat(
                    String.format(
                            "Command %s with index %d in wrong place or not equals required class %s",
                            command.get(z).getClass().getName(),
                            z,
                            commandClasses.get(z).getName()),
                    command.get(z),
                    instanceOf(commandClasses.get(z))
            );
        }
    }

    @Test
    public void hasAnyOptions() throws ParseException {
        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN -s");
        ExecutionContext context = new CLIExecutionContext(commandLine);

        // check combination
        assertThat(context.hasAnyOption('Q', 'c'), is(true));
        assertThat(context.hasAnyOption('p', 'c'), is(true));
        assertThat(context.hasAnyOption('p', 'Q'), is(true));
        assertThat(context.hasAnyOption('t', 'p', 'Q'), is(true));
        assertThat(context.hasAnyOption('p', 't'), is(false));
        assertThat(context.hasAnyOption('Q', 's'), is(true));

        // check long name notation
        assertThat(context.hasOption("stream"), is(true));
        assertThat(context.hasOption("qmanager"), is(true));
        assertThat(context.hasOption("channel"), is(true));
        assertThat(context.hasOption("host"), is(false));
        assertThat(context.hasOption("help"), is(false));
    }


    @Test
    public void ConnectDisconnectContainsAndResolveWithPassedArgs() throws ParseException, MissedParameterException {
        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN --srcq Q");

        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(new CLIExecutionContext(commandLine));

        final CommandChainMaker chain = executionPlanBuilder.buildChain();

        final List<Command> commands = chain.getCommandChain();

        // check total commands size
        assertThat("Wrong commands list size", commands.size(), is(3));

        // check that ConnectCommand and DisconnectCommand is a right position and resolve it
        assertThatCommandInstanceOf(commands, MQConnectCommand.class, MQGetCommand.class, MQDisconnectCommand.class);
    }

    @Test
    public void ConnectDisconnectContainsAndResolveWithConfig() throws ParseException, MissedParameterException {
        final CommandLine commandLine = prepareCommandLine("--config def.props -c JVM.DEF.SVRCONN --srcq Q");

        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(new CLIExecutionContext(commandLine));

        final CommandChainMaker chain = executionPlanBuilder.buildChain();

        final List<Command> commands = chain.getCommandChain();

        // check total commands size
        assertThat("Wrong commands list size", commands.size(), is(3)); // connect/disconnect/put

        // check that ConnectCommand and DisconnectCommand is a right position and resolve it
        assertThatCommandInstanceOf(commands, MQConnectCommand.class, MQGetCommand.class, MQDisconnectCommand.class);
    }

    @Test(expected = MissedParameterException.class)
    public void ConnectFailedWithoutConfig() throws ParseException, MissedParameterException {
        final CommandLine commandLine = prepareCommandLine("--dstq RFH.QTEST.QGENERAL1 -t hello");
        new DefaultExecutionPlanBuilder(new CLIExecutionContext(commandLine)).buildChain();
    }

    @Test(expected = MissedParameterException.class)
    public void ChainFailedWithoutOperationActions() throws ParseException, MissedParameterException {
        final CommandLine commandLine = prepareCommandLine("--config def.props");
        try {
            new DefaultExecutionPlanBuilder(new CLIExecutionContext(commandLine)).buildChain();
        } catch(MissedParameterException e) {
            assertThat("Wrong exception message for missed parameters", "Option(s) [dstq] [lslq] [srcq]  are missed.", equalTo(e.getMessage()));
            throw e;
        }
    }

    @Test
    public void MQPutTextContainsAndResolve() throws ParseException, MissedParameterException {
        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN --dstq RFH.QTEST.QGENERAL1 -t Hello");

        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(new CLIExecutionContext(commandLine));

        final CommandChainMaker chain = executionPlanBuilder.buildChain();

        final List<Command> commands = chain.getCommandChain();

        // check total commands size
        assertThat("Wrong commands list size", commands.size(), is(3));

        // check ConnectCommand position and resolve it
        Command unknownCommand0 = commands.get(0);
        assertThatCommandInstanceOf(unknownCommand0, MQConnectCommand.class);

        Command unknownCommand1 = commands.get(1);
        assertThatCommandInstanceOf(unknownCommand1, MQPutCommand.class);

        Command unknownCommand2 = commands.get(2);
        assertThatCommandInstanceOf(unknownCommand2, MQDisconnectCommand.class);

    }

    @Test
    public void MQPutFileContainsAndResolve() throws ParseException, MissedParameterException {
        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN --dstq RFH.QTEST.QGENERAL1 -p /tmp/some.file");
        assertTrue(commandLine.hasOption("dstq"));

        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(new CLIExecutionContext(commandLine));

        final CommandChainMaker chain = executionPlanBuilder.buildChain();

        final List<Command> commands = chain.getCommandChain();

        assertThat("Wrong commands list size", commands.size(), is(3));

        // check ConnectCommand position and resolve it
        Command unknownCommand0 = commands.get(0);
        assertThatCommandInstanceOf(unknownCommand0, MQConnectCommand.class);

        Command unknownCommand1 = commands.get(1);
        assertThatCommandInstanceOf(unknownCommand1, MQPutCommand.class);

        Command unknownCommand2 = commands.get(2);
        assertThatCommandInstanceOf(unknownCommand2, MQDisconnectCommand.class);

    }

}
