package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleTable;
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
    protected void validateOptions() throws IncompatibleOptionsException, MissedParameterException {
        if (getExecutionContext().hasntOption("lslq"))
            raiseIncompatibeException("Option --lslq are missed.");
    }

    @Override
    protected void work() throws CommandGeneralException, MissedParameterException {
        final ConsoleWriter console = getConsoleWriter();
        final ConsoleTable table = console.createTable(TableColumnName.QUEUE, TableColumnName.CAPACITY, TableColumnName.OPEN_INPUT, TableColumnName.OPEN_OUTPUT);

        final ExecutionContext ctx = getExecutionContext();
        try {
            final ManagerInspector managerInspector = new ManagerInspectorImpl(ctx.getQueueManager());
            final String filter = ctx.getOption("lslq", "*");
            final List<Queue> queues = managerInspector.selectLocalQueues(filter);
            for (Iterator<Queue> iterator = queues.iterator(); iterator.hasNext(); ) {
                Queue next = iterator.next();
                table.append(
                        next.getName(),                                 // queue name
                        next.getDepth() + "/" + next.getMaxDepth(),     // current depth / max depth
                        String.valueOf(next.getInputCount()),           // opened input count
                        String.valueOf(next.getOutputCount())           // opened output count
                );
            }
            table.flash();

        } catch (IOException | MQException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }

}
