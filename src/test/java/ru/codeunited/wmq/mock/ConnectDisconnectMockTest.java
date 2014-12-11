package ru.codeunited.wmq.mock;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.CLITestSupport;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.10.14.
 */
public class ConnectDisconnectMockTest extends CLITestSupport {

    @Test
    public void connectDisconnectWithMockFactory() throws ParseException, MissedParameterException, CommandGeneralException, IncompatibleOptionsException {
        final MQConnectCommand connectCommand = new MQConnectCommand(new WMQConnectionFactoryMocked());
        final MQDisconnectCommand disconnectCommand = new MQDisconnectCommand();
        final CommandChainMaker chain = new CommandChainMaker(new CLIExecutionContext(getCommandLine_With_Qc()))
                .addCommand(connectCommand)
                .addCommand(disconnectCommand);
        chain.execute();

    }
}
