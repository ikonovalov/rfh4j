package ru.codeunited.wmq;

import com.ibm.mq.constants.CMQC;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import static com.ibm.mq.constants.MQConstants.*;
import static ru.codeunited.wmq.RFHConstants.OPT_CONFIG;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class MQPropertiesComposer {

    public static final String DEFAULT_HOST = "localhost";

    public static final int DEFAULT_PORT = 1414;

    public static final String DEFAULT_CHANNEL = "SYSTEM.DEF.SVRCONN";

    public final ExecutionContext context;

    public static final String DEFAULT_CONFIG_PATH = "./default.properties";

    private static Logger LOG = Logger.getLogger(MQPropertiesComposer.class.getName());

    private final Properties defaultProperties = new Properties();

    {
        defaultProperties.put(HOST_NAME_PROPERTY, DEFAULT_HOST);
        defaultProperties.put(PORT_PROPERTY, DEFAULT_PORT);
        defaultProperties.put(TRANSPORT_PROPERTY, TRANSPORT_MQSERIES_CLIENT);
        defaultProperties.put(CHANNEL_PROPERTY, DEFAULT_CHANNEL);
        defaultProperties.put(CONNECT_OPTIONS_PROPERTY,
                MQCNO_ACTIVITY_TRACE_DISABLED | MQCNO_ACCOUNTING_MQI_DISABLED | MQCNO_ACCOUNTING_Q_DISABLED
        );
    }

    public MQPropertiesComposer(ExecutionContext context) {
        this.context = context;
    }

    /**
     * Returns default properties for RFH4J.
     * They can be useful for merged parameters.
     * @return
     */
    protected final Properties getDefaultProperties() {
        return defaultProperties;
    }

    /**
     * Use checkCompatibility before return!
     * @return
     */
    public Properties compose() {
        final Properties properties = getDefaultProperties();
        return checkCompatibility(properties);
    }

    /**
     * Use this method before exposing to external!
     * @param properties
     * @return
     */
    public final static Properties checkCompatibility(Properties properties) {
        return properties;
    }
}
