package ru.codeunited.wmq.mock;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.CLITestSupport;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.handler.NestedHandlerException;

import static ru.codeunited.wmq.CLITestSupport.getCommandLine_With_Qc;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.10.14.
 */
public class ConnectDisconnectMockTest {

    @Test
    public void connectDisconnectWithMockFactory() throws ParseException, MissedParameterException, CommandGeneralException, IncompatibleOptionsException, NestedHandlerException {
        final MQConnectCommand connectCommand = new MQConnectCommand(new WMQConnectionFactoryMocked());
        final MQDisconnectCommand disconnectCommand = new MQDisconnectCommand();
        final CommandChain chain = new CommandChainImpl();
        chain.setContext(new CLIExecutionContext(getCommandLine_With_Qc()));
        chain
                .addCommand(connectCommand)
                .addCommand(disconnectCommand);
        chain.execute();

    }
}
