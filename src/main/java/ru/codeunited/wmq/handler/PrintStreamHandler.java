package ru.codeunited.wmq.handler;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleTable;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableColumnName;

import java.io.IOException;

import static com.ibm.mq.constants.CMQC.MQFMT_ADMIN;
import static com.ibm.mq.constants.CMQC.MQFMT_STRING;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class PrintStreamHandler extends CommonMessageHander<Void> {

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

    public PrintStreamHandler(ExecutionContext context, ConsoleWriter console) {
        super(context);
        this.console = console;
    }

    @Override
    public Void onMessage(MessageEvent messageEvent) throws NestedHandlerException {
        final String messageFormat = messageEvent.getMessageFormat();
        try {
            switch (messageFormat) {
                case MQFMT_STRING:
                    final ConsoleTable table = console.createTable(TABLE_HEADER);

                    table.append(
                            String.valueOf(messageEvent.getMessageIndex()),
                            messageEvent.getOperation().name(),
                            getContext().getQueueManager().getName(),
                            messageEvent.getEventSource().getName(),
                            messageEvent.getHexMessageId(),
                            messageEvent.getHexCorrelationId()
                    );

                    table.appendToLastRow("<stream>").flash();
                    table.flash();
                case MQFMT_ADMIN:
                default:
                    console.write(messageEvent.getMessage());
            }
        } catch (MQException | IOException e) {
            throw NestedHandlerException.nest(e);
        }
        return null;
    }
}
