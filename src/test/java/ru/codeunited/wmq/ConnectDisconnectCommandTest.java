package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class ConnectDisconnectCommandTest extends CLITestSupport {


    @Test
    public void makeBindingConnection() throws CommandGeneralException, ParseException, MissedParameterException, IncompatibleOptionsException {

        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN --transport=binding");

        connectOperation(commandLine);
    }

    @Test
    public void makeClientConnection() throws CommandGeneralException, ParseException, MissedParameterException, IncompatibleOptionsException {

        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN --transport=client");

        connectOperation(commandLine);
    }

    @Test
    public void makeDefaultConnection() throws CommandGeneralException, ParseException, MissedParameterException, IncompatibleOptionsException {

        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN");

        connectOperation(commandLine);
    }

    private void connectOperation(CommandLine commandLine) throws CommandGeneralException, MissedParameterException, IncompatibleOptionsException {
        final MQConnectCommand connectCommand = new MQConnectCommand(new CLIExecutionContext(commandLine));
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
