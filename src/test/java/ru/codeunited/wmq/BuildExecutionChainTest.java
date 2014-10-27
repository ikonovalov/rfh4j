package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
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


    }

    public void assertThatCommandResolved(Command command) {
        assertTrue(
                "ConnectCommand not resolved",
               command.resolve());
    }

    @Test
    public void ConnectDisconnectContainsAndResolve() throws ParseException {
        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN");

        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(new ExecutionContext(), commandLine);

        final CommandChainMaker chain = executionPlanBuilder.buildChain();

        final List<Command> commands = chain.getCommandChain();

        // check total commands size
        assertThat("Wrong commands list size", commands.size(), is(2));

        // check that ConnectCommand and DisconnectCommand is a right position and resolve it
        assertThatCommandInstanceOf(commands, ConnectCommand.class, DisconnectCommand.class);
    }

    @Test
    public void MQPutTextContainsAndResolve() throws ParseException {
        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN -dstq Q1 -t Hello");

        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(new ExecutionContext(), commandLine);

        final CommandChainMaker chain = executionPlanBuilder.buildChain();

        final List<Command> commands = chain.getCommandChain();

        // check total commands size
        assertThat("Wrong commands list size", commands.size(), is(3));

        // check ConnectCommand position and resolve it
        Command unknownCommand0 = commands.get(0);
        assertThatCommandInstanceOf(unknownCommand0, ConnectCommand.class);
        assertThatCommandResolved(unknownCommand0);

        Command unknownCommand1 = commands.get(1);
        assertThatCommandInstanceOf(unknownCommand1, MQPutCommand.class);
        assertThatCommandResolved(unknownCommand1);

        Command unknownCommand2 = commands.get(2);
        assertThatCommandInstanceOf(unknownCommand2, DisconnectCommand.class);
        assertThatCommandResolved(unknownCommand2);

    }

    @Test
    public void MQPutFileContainsAndResolve() throws ParseException {
        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN --dstq Q1 -p /tmp/some.file");

        final ExecutionPlanBuilder executionPlanBuilder = new DefaultExecutionPlanBuilder(new ExecutionContext(), commandLine);

        final CommandChainMaker chain = executionPlanBuilder.buildChain();

        final List<Command> commands = chain.getCommandChain();

        // check total commands size
        assertThat("Wrong commands list size", commands.size(), is(3));

    }

}
