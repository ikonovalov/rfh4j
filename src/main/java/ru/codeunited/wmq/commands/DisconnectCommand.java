package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;

/**
 * codeunited.ru
 * konovalov84@gmail.com
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

                // check disconnection
                if (mqQueueManager.isConnected()) {
                    throw new CommandGeneralException(mqQueueManager.getName() + " still connected but was performed disconnect.");
                }
                LOG.fine("[" + mqQueueManager.getName() + "] disconnected");
            } catch (MQException e) {
                LOG.severe(e.getMessage());
                console.errorln(e.getMessage());
                throw new CommandGeneralException(e);
            }
        }
    }
}
