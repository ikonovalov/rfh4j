package ru.codeunited.wmq.commands;

import com.ibm.mq.*;
import ru.codeunited.wmq.MessageTools;
import ru.codeunited.wmq.cli.ConsoleWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.ibm.mq.constants.CMQC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class PutFileCommand extends AbstractCommand {

    @Override
    public void work() throws CommandGeneralException {
        final ExecutionContext context = getExecutionContext();
        final ConsoleWriter console = getConsoleWriter();
        try {
            final MQQueueManager manager = context.getQueueManager();
            final MQQueue queue = manager.accessQueue("MFC.APPLICATION_OUT", MQOO_OUTPUT);

            final MQMessage message = MessageTools.createUTFMessage();

            if (hasOption('p')) { // file payload
                try (final FileInputStream fileStream = new FileInputStream(getOption('p'))) {
                    MessageTools.writeStreamToMessage(fileStream, message);
                }
            } else if (getCommandLine().hasOption('t')) { // just text message
                MessageTools.writeStringToMessage(getOption('t'), message);
            }

            MQPutMessageOptions putSpec = new MQPutMessageOptions();
            putSpec.options = putSpec.options | MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT;
            queue.put(message, putSpec);
            console.writeln("Message PUT with messageId = " + UUID.nameUUIDFromBytes(message.messageId));
        } catch (IOException | MQException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }

    @Override
    public boolean resolve() {
        return true;
    }
}
