package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import org.junit.Test;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.ManagerInspectorImpl;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

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

    @Test
    public void listQueuesWithoutFilter() throws Exception {
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                final ManagerInspector inspector = new ManagerInspectorImpl(context.getQueueManager());
                final List<String> allQueues = inspector.listLocalQueues();
                assertThat(allQueues, notNullValue());
                assertThat(allQueues.isEmpty(), not(true));
            }
        });
    }

}
