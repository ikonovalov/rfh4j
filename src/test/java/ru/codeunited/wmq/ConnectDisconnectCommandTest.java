package ru.codeunited.wmq;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.handler.NestedHandlerException;

import static org.junit.Assert.assertTrue;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class ConnectDisconnectCommandTest extends CLITestSupport {


    @Test
    public void makeBindingConnection() throws Exception {

        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN --transport=binding");

        connectOperation(commandLine);
    }

    @Test
    public void makeClientConnection() throws Exception {

        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN --transport=client");

        connectOperation(commandLine);
    }

    @Test
    public void makeDefaultConnection() throws Exception {

        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN");

        connectOperation(commandLine);
    }

    private void connectOperation(CommandLine commandLine) throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException, NestedHandlerException {
        Injector injector = getStandartInjector(new CLIExecutionContext(commandLine));
        final MQConnectCommand connectCommand = (MQConnectCommand) injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        assertTrue(connectCommand.selfStateCheckOK());
        assertTrue("Bad initial state in ConnectCommand", ReturnCode.READY == connectCommand.getState());

        final ReturnCode returnCode = connectCommand.execute();
        assertTrue("Unexpected return code in connect operation " + returnCode.name(), returnCode == ReturnCode.SUCCESS);
        assertTrue("Connection command not in final state", ReturnCode.SUCCESS == connectCommand.getState());

        final MQDisconnectCommand disconnectCommand = new MQDisconnectCommand();
        connectCommand.copyEnvironmentTo(disconnectCommand);
        assertTrue("Disconnect command in bad initial state " + disconnectCommand.getState(), ReturnCode.READY == disconnectCommand.getState());

        final ReturnCode disconnectReturnCode = disconnectCommand.execute();
        assertTrue("Unexpected return code in disconnect operation " + disconnectReturnCode.name(), disconnectReturnCode == ReturnCode.SUCCESS);
    }
}
