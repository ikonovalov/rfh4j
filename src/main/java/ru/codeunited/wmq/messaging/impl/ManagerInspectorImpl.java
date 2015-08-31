package ru.codeunited.wmq.messaging.impl;

import com.ibm.mq.MQException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;
import com.ibm.mq.pcf.PCFParameter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import ru.codeunited.wmq.messaging.MQLink;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.QueueInspector;
import ru.codeunited.wmq.messaging.QueueManagerAttributes;
import ru.codeunited.wmq.messaging.pcf.InquireCommand;
import ru.codeunited.wmq.messaging.pcf.PCFUtilService;
import ru.codeunited.wmq.messaging.Queue;

import java.io.IOException;
import java.util.*;

import static com.ibm.mq.constants.MQConstants.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
public class ManagerInspectorImpl implements ManagerInspector {

    private final PCFMessageAgent pcfAgent;

    private final MQLink link;

    public ManagerInspectorImpl(MQLink link) {
        try {
            this.pcfAgent = new PCFMessageAgent(link.getManager().get());
        } catch (MQException e) {
            throw new IllegalStateException(e);
        }
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
    public QueueManagerAttributes managerAttributes() throws MQException, IOException {
        QueueManagerAttributes attrs = new QueueManagerAttributes();

        PCFMessage request = new PCFMessage (InquireCommand.QMGR.object());
        request.addParameter(MQIACF_Q_MGR_ATTRS, new int[]{MQIACF_ALL}); /* MQIACF_Q_MGR_ATTRS is a MQCFIL type (IL -> integer list) */
        PCFMessage[] responses = query(request);

        PCFMessage response = responses[0];
        @SuppressWarnings("unchecked")
        Enumeration<PCFParameter> parameterEnumeration = response.getParameters();
        while(parameterEnumeration.hasMoreElements()) {
            PCFParameter parameter = parameterEnumeration.nextElement();
            Pair<Integer, String> keyPair = ImmutablePair.of(parameter.getParameter(), parameter.getParameterName());
            Pair<Object, String> valuePair = ImmutablePair.of(parameter.getValue(), PCFUtilService.decodeValue(parameter));
            attrs.put(keyPair, valuePair);
        }
        return attrs;
    }

    @Override
    public List<Queue> selectLocalQueues(String queueNameFilter) throws MQException, IOException {
        final PCFMessage request = new PCFMessage (InquireCommand.QUEUE_NAMES.object());

        request.addParameter(MQCA_Q_NAME, queueNameFilter);
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
