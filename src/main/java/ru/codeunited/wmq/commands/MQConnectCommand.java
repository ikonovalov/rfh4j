package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.messaging.WMQConnectionFactory;
import ru.codeunited.wmq.messaging.WMQDefaultConnectionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQConnectCommand extends AbstractCommand {

    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 1414;

    private static final String DEFAULT_CHANNEL = "SYSTEM.DEF.SVRCONN";

    public static final String QUEUE_MANAGER = "qmanager";

    public static final String CONFIG_OPTION = "config";
    public static final String CHANNEL_PROPERTY = "channel";
    public static final String HOST_PROPERTY = "host";
    public static final String USER_PROPERTY = "user";
    public static final String DEFAULT_CONFIG_PATH = "./default.properties";

    private final WMQConnectionFactory connectionFactory;

    private final Properties defaultProperties = new Properties();

    {
        //defaultProperties.put(APPNAME_PROPERTY, "RFH4J");
        defaultProperties.put(HOST_NAME_PROPERTY, DEFAULT_HOST);
        defaultProperties.put(PORT_PROPERTY, DEFAULT_PORT);
        //defaultProperties.put(TRANSPORT_PROPERTY, TRANSPORT_MQSERIES_BINDINGS); // anti-RC2495 use with -Djava.library.path=/opt/mqm/java/lib64
        defaultProperties.put(TRANSPORT_PROPERTY, TRANSPORT_MQSERIES_CLIENT);
        defaultProperties.put(CHANNEL_PROPERTY, DEFAULT_CHANNEL);
        defaultProperties.put(CONNECT_OPTIONS_PROPERTY,
                MQCNO_NONE
                        | MQCNO_ACTIVITY_TRACE_DISABLED | MQCNO_ACCOUNTING_MQI_DISABLED | MQCNO_ACCOUNTING_Q_DISABLED
        );
    }

    public MQConnectCommand() {
        connectionFactory = new WMQDefaultConnectionFactory();
    }

    public MQConnectCommand(ExecutionContext context) {
        this();
        setContext(context);
    }

    public MQConnectCommand(WMQConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private Properties loadFromFile(String filePath) {
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

    protected Properties configFileAsProperties() {
        final ExecutionContext ctx = getExecutionContext();
        String configPath = null;
        if (ctx.hasOption(CONFIG_OPTION)) {
            configPath = ctx.getOption(CONFIG_OPTION);
        } else if (isDefaultConfigAvailable()) {
            configPath = DEFAULT_CONFIG_PATH;
        }
        return loadFromFile(configPath);
    }

    public static boolean isDefaultConfigAvailable() {
        return new File(DEFAULT_CONFIG_PATH).exists();
    }

    protected Properties passedArgumentsAsProperties() {
        final Properties passedProperties = new Properties();
        final ExecutionContext ctx = getExecutionContext();
        if (ctx.hasOption(CHANNEL_PROPERTY))
            passedProperties.put(CMQC.CHANNEL_PROPERTY, ctx.getOption(CHANNEL_PROPERTY));
        if (ctx.hasOption(QUEUE_MANAGER))
            passedProperties.put(QUEUE_MANAGER, ctx.getOption(QUEUE_MANAGER));
        if (ctx.hasOption(HOST_PROPERTY))
            passedProperties.put(HOST_NAME_PROPERTY, ctx.getOption(HOST_PROPERTY));
        if (ctx.hasOption(PORT_PROPERTY))
            passedProperties.put(PORT_PROPERTY, Integer.valueOf(ctx.getOption(PORT_PROPERTY)));
        if (ctx.hasOption(USER_PROPERTY))
            passedProperties.put(USER_ID_PROPERTY, ctx.getOption(USER_PROPERTY));
        if(ctx.hasOption(TRANSPORT_PROPERTY)) {
            final String transportAlias = ctx.getOption(TRANSPORT_PROPERTY).toUpperCase();
            String decodedTransport;
            switch (transportAlias) {
                case "BINDING":
                    decodedTransport = TRANSPORT_MQSERIES_BINDINGS;
                    break;
                case "CLIENT":
                    decodedTransport = TRANSPORT_MQSERIES_CLIENT;
                    break;
                default:
                    LOG.warning(String.format("%s unrecognized with passed value %s. Switched to %s", TRANSPORT_PROPERTY, transportAlias, TRANSPORT_MQSERIES_BINDINGS));
                    decodedTransport = TRANSPORT_MQSERIES_BINDINGS;

            }
            passedProperties.put(TRANSPORT_PROPERTY, decodedTransport);
        }
        return passedProperties;
    }

    protected Properties mergeArguments() {
        final Properties mergedProperties = new Properties();

        // apply default setting
        mergedProperties.putAll(defaultProperties); /** MQQueueManager use Hashtable and not real Properties, so **/

        // try to load config file is specified
        mergedProperties.putAll(configFileAsProperties());

        // Override config with CLI passed arguments
        mergedProperties.putAll(passedArgumentsAsProperties());

        return mergedProperties;
    }

    @Override
    protected void work() throws CommandGeneralException {
        final ExecutionContext context = getExecutionContext();
        // TODO SHOULD BE MOVED TO SEPARATED METHODS AND HEAVY TESTED
        // merged properties
        final Properties mergedProperties = mergeArguments();

        final String queueManagerName = mergedProperties.getProperty(QUEUE_MANAGER);

        LOG.fine("Connecting to [" + queueManagerName + "] with " + mergedProperties.toString());

        // perform connection
        try {
            final MQQueueManager mqQueueManager = connectionFactory.connectQueueManager(queueManagerName, mergedProperties);
            context.setQueueManager(mqQueueManager);
            // check connection
            if (mqQueueManager.isConnected()) {
                LOG.fine("[" + mqQueueManager.getName() + "] connected");
            } else {
                throw new MQConnectionException("Connection performed but queue manager looks like disconnected");
            }
        } catch (MQException e) {
            throw new CommandGeneralException(e);
        }
    }
}
