package ru.codeunited.wmq.handler;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleTable;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableColumnName;

import java.io.File;
import java.io.IOException;

import static ru.codeunited.wmq.RFHConstants.OPT_PAYLOAD;
import static ru.codeunited.wmq.messaging.MessageTools.bytesToHex;
import static ru.codeunited.wmq.messaging.MessageTools.fileNameForMessage;
import static ru.codeunited.wmq.messaging.MessageTools.writeMessageBodyToFile;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class BodyToFileHandler extends CommonMessageHander<File> {

    private final ConsoleWriter console;

    private static final TableColumnName[] TABLE_HEADER = {
            TableColumnName.INDEX,
            TableColumnName.ACTION,
            TableColumnName.QMANAGER,
            TableColumnName.QUEUE,
            TableColumnName.MESSAGE_ID,
            TableColumnName.CORREL_ID,
            TableColumnName.OUTPUT
    };

    public BodyToFileHandler(ExecutionContext context, ConsoleWriter console) {
        super(context);
        this.console = console;
    }

    @Override
    public File onMessage(MessageEvent messageEvent) throws NestedHandlerException {
        final ConsoleTable table = console.createTable(TABLE_HEADER);
        final MQMessage message = messageEvent.getMessage();
        try {
            table.append(
                    String.valueOf(messageEvent.getMessageIndex()),
                    messageEvent.getOperation().name(),
                    getContext().getQueueManager().getName(),
                    messageEvent.getEventSource().getName(),
                    messageEvent.getHexMessageId(),
                    messageEvent.getHexCorrelationId()
            );
        } catch (MQException e) {
            throw NestedHandlerException.nest(e);
        }

        File destination = new File(getContext().getOption(OPT_PAYLOAD, fileNameForMessage(message)));

        // if payload specified as folder, then we need to append file name
        if (destination.exists() && destination.isDirectory()) {
            destination = new File(destination.getAbsoluteFile() + File.separator + fileNameForMessage(message));
        }
        try {
            writeMessageBodyToFile(message, destination);
        } catch (IOException e) {
            throw NestedHandlerException.nest(e);
        }
        table.appendToLastRow(destination.getAbsolutePath());
        table.flash();
        return destination;
    }
}
