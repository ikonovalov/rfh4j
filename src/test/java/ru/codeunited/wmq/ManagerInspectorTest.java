package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import org.junit.Test;
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
public class ManagerInspectorTest extends QueueingCapability {

    @Test(timeout = 5000)
    public void listQueuesWithoutFilter() throws Exception {
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                final ManagerInspector inspector = new ManagerInspectorImpl(context.getLink());
                final List<Queue> allQueues = inspector.listLocalQueues();
                assertThat(allQueues, notNullValue());
                assertThat(allQueues.isEmpty(), not(true));
            }
        });
    }

    @Test(timeout = 5000)
    public void searchRFHQueues() throws Exception {
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                final ManagerInspector inspector = new ManagerInspectorImpl(context.getLink());
                final List<Queue> rfhQueues = inspector.selectLocalQueues("RFH.*");
                boolean notRFHQueue = false;
                for (Queue queue : rfhQueues) {
                    System.out.println(queue);
                    if (!queue.getName().startsWith("RFH.")) {
                        notRFHQueue = true;
                        break;
                    }
                }
                assertThat("Not RFH queue encountered at the search", notRFHQueue, not(true));
            }
        });
    }

}
