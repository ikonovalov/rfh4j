package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.messaging.WMQConnectionFactory;
import ru.codeunited.wmq.messaging.WMQDefaultConnectionFactory;

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

    public static final String QUEUE_MANAGER = "qmanager";

    private final WMQConnectionFactory connectionFactory;

    private final Properties defaultProperties = new Properties();

    {
        defaultProperties.put(HOST_NAME_PROPERTY, DEFAULT_HOST);
        defaultProperties.put(PORT_PROPERTY, DEFAULT_PORT);
        defaultProperties.put(TRANSPORT_PROPERTY, TRANSPORT_MQSERIES_CLIENT);
    }

    public ConnectCommand() {
        connectionFactory = new WMQDefaultConnectionFactory();
    }

    public ConnectCommand(WMQConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    protected Properties configFileAsProperties() {
        final Properties fileProperties = new Properties();
        final ExecutionContext ctx = getExecutionContext();
        if (ctx.hasOption("config")) {
            try (final FileInputStream propertiesStream = new FileInputStream(ctx.getOption("config"))) {
                fileProperties.load(propertiesStream);

            /* fix port issue (String -> Integer) */
                fileProperties.put(PORT_PROPERTY, Integer.valueOf(fileProperties.getProperty(PORT_PROPERTY)));
            } catch (IOException e) {
                LOG.severe("config parameter is passed but we got error [" + e.getMessage() + "]");
            }
        }
        return fileProperties;
    }

    protected Properties passedArgumentsAsProperties() {
        final Properties passedProperties = new Properties();
        final ExecutionContext ctx = getExecutionContext();
        if (ctx.hasOption("channel"))
            passedProperties.put(CHANNEL_PROPERTY, ctx.getOption("channel"));
        if (ctx.hasOption(QUEUE_MANAGER))
            passedProperties.put(QUEUE_MANAGER, ctx.getOption(QUEUE_MANAGER));
        if (ctx.hasOption("host"))
            passedProperties.put(HOST_NAME_PROPERTY, ctx.getOption("host"));
        if (ctx.hasOption("port"))
            passedProperties.put(PORT_PROPERTY, Integer.valueOf(ctx.getOption("port")));
        if (ctx.hasOption("user"))
            passedProperties.put(USER_ID_PROPERTY, ctx.getOption("user"));
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
            final MQQueueManager mqQueueManager = connectionFactory.connectQueueManager(queueManagerName, mergedProperties);
            context.setQueueManager(mqQueueManager);
            console.table("CONNECT", queueManagerName);
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
