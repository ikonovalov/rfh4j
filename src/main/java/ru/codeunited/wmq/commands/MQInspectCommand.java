package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleTable;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.impl.ManagerInspectorImpl;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.io.IOException;
import java.util.List;
import static ru.codeunited.wmq.RFHConstants.*;
import static ru.codeunited.wmq.cli.TableColumnName.*;

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
        final ConsoleTable table = console.createTable(QUEUE, CAPACITY, OPEN_INPUT, OPEN_OUTPUT);

        final ExecutionContext ctx = getExecutionContext();
        try {
            final ManagerInspector managerInspector = new ManagerInspectorImpl(ctx.getQueueManager());
            final String filter = ctx.getOption(OPT_LIST_QLOCAL, "*");
            final List<Queue> queues = managerInspector.selectLocalQueues(filter);
            for (final Queue next : queues) {
                table.append(
                        next.getName(),                                 // queue name
                        next.getDepth() + "/" + next.getMaxDepth(),     // current depth / max depth
                        String.valueOf(next.getInputCount()),           // opened input count
                        String.valueOf(next.getOutputCount())           // opened output count
                );
            }
            table.make();

        } catch (IOException | MQException e) {
            LOG.severe(e.getMessage());
            console.errorln(e.getMessage());
            throw new CommandGeneralException(e);
        }
    }

}
