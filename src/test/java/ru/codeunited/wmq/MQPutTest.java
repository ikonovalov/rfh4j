package ru.codeunited.wmq;

import com.ibm.mq.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by Igor on 21.05.2014.
 */
public class MQPutTest implements TestEnvironmentSetting {

    private static final Logger LOG = Logger.getLogger(MQPutTest.class.getName());

    private static final String APPLICATION_ID = "application_id";

    private MQQueueManager queueManager = null;

    @Before
    public void init() throws MQException {
        final Properties properties = new Properties();
        properties.putAll(ClientModeUtils.createProps());

        queueManager = new MQQueueManager(QMGR_NAME, properties);
        LOG.info("Connected to " + QMGR_NAME);
    }

    @After
    public void destroy() throws MQException {
        if (queueManager != null && queueManager.isConnected()) {
            queueManager.disconnect();
            LOG.info("QMGR disconnected");
        }
    }

    @Test
    /**
     * MQPUT out of syncpoint
     */
    public void put() throws MQException, IOException {
        MQQueue queue = queueManager.accessQueue("Q1", MQOO_OUTPUT);

        MQMessage message = new MQMessage();
        message.format = MQFMT_STRING;
        message.writeUTF("UNIXTime: " + System.currentTimeMillis());

        message.setIntProperty("application_id", 11622717);

        MQPutMessageOptions putSpec = new MQPutMessageOptions();
        putSpec.options =  putSpec.options | MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT;

        queue.put(message, putSpec);
        LOG.info("Message PUT with messageId = " + UUID.nameUUIDFromBytes(message.messageId) + " application_id = " + message.getIntProperty(APPLICATION_ID));

    }
}
