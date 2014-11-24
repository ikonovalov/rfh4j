package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableColumnName;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.ManagerInspectorImpl;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
public class MQInspectCommand extends QueueCommand {

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException {
        final ConsoleWriter console = getConsoleWriter();
        final ExecutionContext ctx = getExecutionContext();
        try {
            final ManagerInspector managerInspector = new ManagerInspectorImpl(ctx.getQueueManager());
            if (ctx.hasOption("lslq")) {
                console.head(TableColumnName.QUEUE, TableColumnName.CAPACITY, TableColumnName.OPEN_INPUT, TableColumnName.OPEN_OUTPUT);
                final String filter = ctx.getOption("lslq", "*");
                final List<Queue> queues = managerInspector.selectLocalQueues(filter);
                for (Iterator<Queue> iterator = queues.iterator(); iterator.hasNext(); ) {
                    Queue next = iterator.next();
                    console.table(
                            next.getName(),
                            next.getDepth() + "/" + next.getMaxDepth(),
                            String.valueOf(next.getInputCount()),
                            String.valueOf(next.getOutputCount())
                    );
                }
            }
            console.printTable();

        } catch (IOException| MQException e) {
        LOG.severe(e.getMessage());
        console.errorln(e.getMessage());
        throw new CommandGeneralException(e);
    }
    }
}
