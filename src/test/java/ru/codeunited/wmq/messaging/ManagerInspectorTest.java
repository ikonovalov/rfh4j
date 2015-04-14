package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.QueueingCapability;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.impl.ManagerInspectorImpl;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class})
public class ManagerInspectorTest extends QueueingCapability {

    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void inquireQMGRParameters() throws Exception {
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                try (ManagerInspector managerInspector = new ManagerInspectorImpl(context.getLink())) {
                    managerInspector.managerAttributes();
                }
            }
        });
    }

    @Test(timeout = 5000)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN --transport=binding")
    public void listQueuesWithoutFilter() throws Exception {
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                try (final ManagerInspector inspector = new ManagerInspectorImpl(context.getLink())) {
                    final List<Queue> allQueues = inspector.listLocalQueues();
                    assertThat(allQueues, notNullValue());
                    assertThat(allQueues.isEmpty(), not(true));
                }
            }
        });
    }

    @Test(timeout = 5000)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void searchRFHQueues() throws Exception {
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                try (final ManagerInspector inspector = new ManagerInspectorImpl(context.getLink())) {
                    final List<Queue> rfhQueues = inspector.selectLocalQueues("RFH.*");
                    boolean notRFHQueue = false;
                    for (Queue queue : rfhQueues) {
                        if (!queue.getName().startsWith("RFH.")) {
                            notRFHQueue = true;
                            break;
                        }
                    }
                    assertThat("Not RFH queue encountered at the search", notRFHQueue, not(true));
                }
            }
        });
    }

}
