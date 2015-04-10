package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.messaging.MQLink;
import ru.codeunited.wmq.messaging.QueueManager;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQDisconnectCommand extends AbstractCommand {

    @Override
    public void work() throws CommandGeneralException {
        final ExecutionContext context = getExecutionContext();
        final MQLink link  = context.getLink();
        final QueueManager mqQueueManager = link.getManager();

        if (mqQueueManager != null && mqQueueManager.isConnected()) {
            try {
                mqQueueManager.close();

                // check disconnection
                if (mqQueueManager.isConnected()) {
                    throw new CommandGeneralException(link.getOptions().getQueueManagerName() + " still connected but was performed disconnect.");
                }
                LOG.fine("[" + link.getOptions().getQueueManagerName() + "] disconnected");

            } catch (IOException e) {
                LOG.severe(e.getMessage());
                getConsoleWriter().errorln(e.getMessage());
                throw new CommandGeneralException(e);
            }
        }
    }
}
