package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import ru.codeunited.wmq.commands.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class ConnectDisconnectCommandTest extends CLITestSupport {


    @Test
    public void makeConnection() throws CommandGeneralException, ParseException, MissedParameterException {

        final String[] args = "-Q DEFQM -c JVM.DEF.SVRCONN".split(" ");

        final CommandLine commandLine = getCliParser().parse(getOptions(), args);

        final ConnectCommand connectCommand = new ConnectCommand();
        connectCommand.setContext(new CLIExecutionContext(commandLine));
        assertTrue(connectCommand.selfStateCheckOK());
        assertTrue("Bad initial state in ConnectCommand", ReturnCode.READY == connectCommand.getState());

        final ReturnCode returnCode = connectCommand.execute();
        assertTrue("Unexpected return code in connect operation " + returnCode.name(), returnCode == ReturnCode.SUCCESS);
        assertTrue("Connection command not in final state", ReturnCode.SUCCESS == connectCommand.getState());

        final DisconnectCommand disconnectCommand = new DisconnectCommand();
        connectCommand.copyEnvironmentTo(disconnectCommand);
        assertTrue("Disconnect command in bad initial state " + disconnectCommand.getState(), ReturnCode.READY == disconnectCommand.getState());

        final ReturnCode disconnectReturnCode = disconnectCommand.execute();
        assertTrue("Unexpected return code in disconnect operation " + disconnectReturnCode.name(), disconnectReturnCode == ReturnCode.SUCCESS);
    }
}
