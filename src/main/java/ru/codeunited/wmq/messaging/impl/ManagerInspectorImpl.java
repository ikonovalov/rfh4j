package ru.codeunited.wmq.messaging.impl;

import com.ibm.mq.MQException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;
import ru.codeunited.wmq.messaging.MQLink;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.QueueInspector;
import ru.codeunited.wmq.messaging.pcf.Filter;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ibm.mq.constants.MQConstants.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
public class ManagerInspectorImpl implements ManagerInspector {

    private final PCFMessageAgent pcfAgent;

    private final MQLink link;

    public ManagerInspectorImpl(MQLink link) throws MQException {
        this.pcfAgent = new PCFMessageAgent(link.getManager().get());
        this.link = link;
    }

    @Override
    public List<Queue> listLocalQueues() throws MQException, IOException {
        return selectLocalQueues("*");
    }

    private PCFMessage[] query(PCFMessage request) throws MQException, IOException {
        return pcfAgent.send(request);
    }

    @Override
    public List<Queue> selectLocalQueues(String filter) throws MQException, IOException {
        final PCFMessage request = new PCFMessage (Filter.QUEUE.object());

        request.addParameter(MQCA_Q_NAME, filter);
        request.addParameter(MQIA_Q_TYPE, MQQT_LOCAL);

        final PCFMessage[] responses = query(request);
        final String[] names = (String[]) responses[0].getParameterValue(MQCACF_Q_NAMES);
        final List<Queue> queues = new ArrayList<>(names.length);
        for (String queueName : names) {
            final Queue queue = new Queue(queueName);
            try (final QueueInspector inspector = new QueueInspectorImpl(queueName, link)) {
                queue.setDepth(inspector.depth());
                queue.setMaxDepth(inspector.maxDepth());
                queue.setInputCount(inspector.openInputCount());
                queue.setOutputCount(inspector.opentOutputCount());
                queues.add(queue);
            }
        }
        return queues;
    }

    @Override
    public void close() throws IOException {
        // PCF Agent disconnect() are performing drop single connection with a QM
    }
}
