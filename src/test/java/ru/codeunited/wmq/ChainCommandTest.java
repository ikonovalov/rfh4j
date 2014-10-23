package ru.codeunited.wmq;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.commands.*;

import static org.junit.Assert.*;

/**
 * Created by ikonovalov on 23.10.14.
 */
public class ChainCommandTest extends CLITestSupport {

    @Test
    public void addCommandToChain() throws ParseException {
        final CommandChainMaker maker = new CommandChainMaker(getCommandLine_With_Qc());
        final AbstractCommand cmd1 = new ConnectCommand();
        final AbstractCommand cmd2 = new DisconnectCommand();

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
        final CommandChainMaker maker = new CommandChainMaker(getCommandLine_With_Qc());
        maker.getCommandChain().add(new DisconnectCommand());
    }
}
