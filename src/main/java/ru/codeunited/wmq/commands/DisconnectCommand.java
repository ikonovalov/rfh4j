package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.cli.ConsoleWriter;

/**
 * Created by ikonovalov on 22.10.14.
 */
public class DisconnectCommand extends AbstractCommand {

    @Override
    public void work() throws CommandGeneralException {
        final ExecutionContext context = getExecutionContext();
        final MQQueueManager mqQueueManager = context.getQueueManager();
        final ConsoleWriter console = getConsoleWriter();

        if (mqQueueManager != null && mqQueueManager.isConnected()) {
            try {
                mqQueueManager.disconnect();
                console.writeln("Disconnected.");
            } catch (MQException e) {
                LOG.severe(e.getMessage());
                console.errorln(e.getMessage());
                throw new CommandGeneralException(e);
            }
        }
        LOG.info("Perform disconnect.");
    }

    @Override
    public boolean resolve() {
        return true;
    }
}
