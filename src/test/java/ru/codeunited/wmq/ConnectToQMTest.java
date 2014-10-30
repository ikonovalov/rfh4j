package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import org.junit.Before;
import org.junit.Test;
import ru.codeunited.wmq.messaging.WMQConnectionFactory;
import ru.codeunited.wmq.messaging.WMQDefaultConnectionFactory;

import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class ConnectToQMTest implements TestEnvironmentSetting {

    private static final Logger LOG = Logger.getLogger(ConnectToQMTest.class.getName());

    private final Properties properties = new Properties();

    @Before
    public void init() throws MQException {
        properties.putAll(ClientModeUtils.createProps());
    }

    @Test
    public void doConnect() throws MQException {
        final WMQConnectionFactory connectionFactory = new WMQDefaultConnectionFactory();
        MQQueueManager mqQueueManager = connectionFactory.connectQueueManager(QMGR_NAME, properties);
        assertTrue("Connection lost.", mqQueueManager.isConnected());
        LOG.info("Connected to " + QMGR_NAME);
        mqQueueManager.disconnect();
        assertFalse("Still connected", mqQueueManager.isConnected());
    }
}
