package ru.codeunited.wmq.commands;

import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.messaging.MQLink;

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
        try {
            if (link != null)
                link.close();
        } catch (IOException e) {
            throw new CommandGeneralException(e);
        }
    }
}
