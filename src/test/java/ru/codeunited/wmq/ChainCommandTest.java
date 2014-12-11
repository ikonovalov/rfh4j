package ru.codeunited.wmq;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class ChainCommandTest extends CLITestSupport {

    @Test
    public void addCommandToChain() throws ParseException {
        final CommandChainMaker maker = new CommandChainMaker(new CLIExecutionContext(getCommandLine_With_Qc()));
        final AbstractCommand cmd1 = new MQConnectCommand();
        final AbstractCommand cmd2 = new MQDisconnectCommand();

        assertTrue("ConnectCommand already initialized", cmd1.selfStateCheckFailed());
        assertTrue("DisconnectCommand already initialized", cmd2.selfStateCheckFailed());

        maker.addCommand(cmd1);
        maker.addCommand(cmd2);

        assertTrue("ConnectCommand not initialized in chain maker", cmd1.selfStateCheckOK());
        assertTrue("DisconnectCommand not initialized in chain maker", cmd2.selfStateCheckOK());

        assertNotNull("Workload is empty! Expected not null.", maker.getCommandChain());

        final int workSize = maker.getCommandChain().size();
        assertTrue("Wrong chain size expected 2 but there is " + workSize, workSize == 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void immutableWorkload() throws ParseException {
        final CommandChainMaker maker = new CommandChainMaker(new CLIExecutionContext(getCommandLine_With_Qc()));
        maker.getCommandChain().add(new MQDisconnectCommand());
    }

    @Test
    public void insertAfterCommandS1() throws ParseException {
        final CommandChainMaker maker = new CommandChainMaker(new CLIExecutionContext(getCommandLine_With_Qc()));
        maker.addAfter(new MQConnectCommand(), null);
        assertThat("Wrong chain size after add one command", maker.getCommandChain().size(), is(1));
        assertThat(maker.getCommandChain().get(0), instanceOf(MQConnectCommand.class));
    }

    @Test
    public void insertAfterCommandS3() throws ParseException {
        final CommandChainMaker maker = new CommandChainMaker(new CLIExecutionContext(getCommandLine_With_Qc()));
        final Command connect = new MQConnectCommand();
        final Command disconnect = new MQDisconnectCommand();
        final MQPutCommand put = new MQPutCommand();
        maker
                .addCommand(connect)
                .addAfter(put,connect)
                .addAfter(disconnect, put);
        List<Command> chain = maker.getCommandChain();
        assertThat("Wrong chain size after add one command. " + chain.toString(), chain.size(), is(3));
        assertThat(chain.get(0), instanceOf(MQConnectCommand.class));
        assertThat(chain.get(1), instanceOf(MQPutCommand.class));
        assertThat(chain.get(2), instanceOf(MQDisconnectCommand.class));
    }
}
