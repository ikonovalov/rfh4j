package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ibm.mq.constants.CMQCFC.*;
import static com.ibm.mq.constants.CMQC.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
public class ManagerInspectorImpl implements ManagerInspector {

    private final PCFMessageAgent pcfAgent;

    public ManagerInspectorImpl(MQQueueManager queueManager) throws MQException {
        this.pcfAgent = new PCFMessageAgent(queueManager);
    }

    @Override
    public List<String> listLocalQueues() throws MQException, IOException {
        return listLocalQueues("*");
    }

    private PCFMessage[] query(PCFMessage request) throws MQException, IOException {
        return pcfAgent.send(request);
    }

    @Override
    public List<String> listLocalQueues(String filter) throws MQException, IOException {
        final PCFMessage request = new PCFMessage (MQCMD_INQUIRE_Q_NAMES);

        request.addParameter(MQCA_Q_NAME, "*");
        request.addParameter(MQIA_Q_TYPE, MQQT_LOCAL);

        final PCFMessage[] responses = query(request);
        final String[] names = (String[]) responses[0].getParameterValue(MQCACF_Q_NAMES);
        return Collections.unmodifiableList(Arrays.asList(names));
    }
}
