package ru.codeunited.wmq;

import com.ibm.mq.constants.CMQC;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import static com.ibm.mq.constants.CMQC.*;
import static com.ibm.mq.constants.CMQC.MQCNO_ACCOUNTING_MQI_DISABLED;
import static com.ibm.mq.constants.CMQC.MQCNO_ACCOUNTING_Q_DISABLED;
import static ru.codeunited.wmq.cli.CLIFactory.OPT_CONFIG;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.02.15.
 */
public class PropertiesComposer {

    public static final String DEFAULT_HOST = "localhost";

    public static final int DEFAULT_PORT = 1414;

    public static final String DEFAULT_CHANNEL = "SYSTEM.DEF.SVRCONN";

    public final ExecutionContext context;

    public static final String DEFAULT_CONFIG_PATH = "./default.properties";

    private static Logger LOG = Logger.getLogger(PropertiesComposer.class.getName());

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

    public PropertiesComposer(ExecutionContext context) {
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
     * Load properties from file specified in context (config option) or from default location.
     * @return
     */
    protected final Properties getConfigFileProperties() {
        String configPath = null;
        if (context.hasOption(OPT_CONFIG)) {
            configPath = context.getOption(OPT_CONFIG);
        } else if (isDefaultConfigAvailable()) {
            configPath = DEFAULT_CONFIG_PATH;
        }
        return loadFromFile(configPath);
    }

    public static boolean isDefaultConfigAvailable() {
        return new File(DEFAULT_CONFIG_PATH).exists();
    }

    private static Properties loadFromFile(String filePath) {
        final Properties fileProperties = new Properties();
        if (filePath == null)
            return fileProperties;

        try (final FileInputStream propertiesStream = new FileInputStream(filePath)) {
            fileProperties.load(propertiesStream);

            /* fix port issue (String -> Integer) */
            fileProperties.put(CMQC.PORT_PROPERTY, Integer.valueOf(fileProperties.getProperty(CMQC.PORT_PROPERTY)));
        } catch (IOException e) {
            LOG.severe("config parameter is passed but we got error [" + e.getMessage() + "]");
        }
        return fileProperties;
    }

    public Properties compose() {
        final Properties mergedProperties = new Properties();

        // apply default setting
        mergedProperties.putAll(getDefaultProperties()); /** MQQueueManager use Hashtable and not real Properties, so **/

        // try to load config file is specified
        mergedProperties.putAll(getConfigFileProperties());

        return mergedProperties;
    }
}
