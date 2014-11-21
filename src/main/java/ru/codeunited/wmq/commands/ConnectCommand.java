package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.cli.TableName;
import ru.codeunited.wmq.messaging.WMQConnectionFactory;
import ru.codeunited.wmq.messaging.WMQDefaultConnectionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.ibm.mq.constants.CMQC.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class ConnectCommand extends AbstractCommand {

    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 1414;

    private static final String DEFAULT_CHANNEL = "SYSTEM.DEF.SVRCONN";

    public static final String QUEUE_MANAGER = "qmanager";

    public static final String CONFIG_OPTION = "config";
    public static final String CHANNEL_PROPERTY = "channel";
    public static final String HOST_PROPERTY = "host";
    public static final String PORT_PROPERTY = "port";
    public static final String USER_PROPERTY = "user";
    public static final String DEFAULT_CONFIG_PATH = "./default.properties";

    private final WMQConnectionFactory connectionFactory;

    private final Properties defaultProperties = new Properties();

    {
        defaultProperties.put(HOST_NAME_PROPERTY, DEFAULT_HOST);
        defaultProperties.put(CMQC.PORT_PROPERTY, DEFAULT_PORT);
        defaultProperties.put(TRANSPORT_PROPERTY, TRANSPORT_MQSERIES_CLIENT);
        defaultProperties.put(CHANNEL_PROPERTY, DEFAULT_CHANNEL);
    }

    public ConnectCommand() {
        connectionFactory = new WMQDefaultConnectionFactory();
    }

    public ConnectCommand(WMQConnectionFactory connectionFactory) {
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
            passedProperties.put(CMQC.PORT_PROPERTY, Integer.valueOf(ctx.getOption(PORT_PROPERTY)));
        if (ctx.hasOption(USER_PROPERTY))
            passedProperties.put(USER_ID_PROPERTY, ctx.getOption(USER_PROPERTY));
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
        final ConsoleWriter console = getConsoleWriter();
        // TODO SHOULD BE MOVED TO SEPARATED METHODS AND HEAVY TESTED
        // merged properties
        final Properties mergedProperties = mergeArguments();

        final String queueManagerName = mergedProperties.getProperty(QUEUE_MANAGER);

        LOG.fine("Connecting to [" + queueManagerName + "] with " + mergedProperties.toString());

        // perform connection
        try {
            long start = System.currentTimeMillis();
            final MQQueueManager mqQueueManager = connectionFactory.connectQueueManager(queueManagerName, mergedProperties);
            context.setQueueManager(mqQueueManager);
            console.head(TableName.ACTION, TableName.QMANAGER, TableName.DESCRIPTION, TableName.CHANNEL, TableName.TIME);
            console.table(
                    "CONNECT",
                    queueManagerName,
                    mqQueueManager.getDescription().trim(),
                    mergedProperties.getProperty(CHANNEL_PROPERTY),
                    String.valueOf(System.currentTimeMillis() - start) + "ms"
            );
            // check connection
            if (mqQueueManager.isConnected()) {
                LOG.fine("[" + mqQueueManager.getName() + "] connected");
            } else {
                throw new MQConnectionException("Connection performed but queue manager looks like disconnected");
            }
        } catch (MQException e) {
            throw new CommandGeneralException(e);
        }
        console.printTable();
    }
}
