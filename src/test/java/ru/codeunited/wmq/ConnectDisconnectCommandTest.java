package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import ru.codeunited.wmq.commands.*;

/**
 * Created by ikonovalov on 23.10.14.
 */
public class ConnectDisconnectCommandTest extends CLITestSupport {


    @Test
    public void makeConnection() throws CommandGeneralException, ParseException {

        final String[] args = "-Q DEFQM -c JVM.DEF.SVRCONN".split(" ");

        final CommandLine commandLine = getCliParser().parse(getOptions(), args);

        ConnectCommand connectCommand = new ConnectCommand();
        connectCommand.setCommandLine(commandLine);
        connectCommand.setContext(new ExecutionContext());
        assertTrue(connectCommand.selfStateCheckOK());
        assertTrue("Bad initial state in ConnectCommand", ReturnCode.READY == connectCommand.getState());

        ReturnCode returnCode = connectCommand.execute();
        assertTrue("Unexpected return code in connect operation " + returnCode.name(), returnCode == ReturnCode.SUCCESS);
        assertTrue("Connection command not in final state", ReturnCode.SUCCESS == connectCommand.getState());

        DisconnectCommand disconnectCommand = new DisconnectCommand();
        connectCommand.copyEnvironmentTo(disconnectCommand);
        assertTrue("Disconnect command in bad initial state " + disconnectCommand.getState(), ReturnCode.READY == disconnectCommand.getState());

        ReturnCode disconnectReturnCode = disconnectCommand.execute();
        assertTrue("Unexpected return code in disconnect operation " + disconnectReturnCode.name(), disconnectReturnCode == ReturnCode.SUCCESS);
    }
}
