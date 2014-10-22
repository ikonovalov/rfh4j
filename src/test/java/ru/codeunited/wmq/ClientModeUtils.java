package ru.codeunited.wmq;

import java.util.Properties;

import static com.ibm.mq.constants.CMQC.*;
import static com.ibm.mq.constants.CMQC.TRANSPORT_MQSERIES_CLIENT;
import static com.ibm.mq.constants.CMQC.USER_ID_PROPERTY;

/**
 * Created by ikonovalov on 22.10.14.
 */
public class ClientModeUtils implements TestEnvironmentSetting{

    /**
     * Create all needed properties for MQ_CLIENT_CONNECTION
     * @return
     */
    public static Properties createProps() {
        final Properties properties = new Properties();
        properties.put(HOST_NAME_PROPERTY, HOSTNAME); // required
        properties.put(PORT_PROPERTY, PORT); // required
        properties.put(CHANNEL_PROPERTY, CHANNEL_NAME); // required
        properties.put(TRANSPORT_PROPERTY, TRANSPORT_MQSERIES_CLIENT); // opt: if default not defined
        properties.put(USER_ID_PROPERTY, AUTH_AS_USER); // opt: if necessary.
        return properties;
    }

}
