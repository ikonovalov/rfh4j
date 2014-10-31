package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.messaging.WMQConnectionFactory;
import ru.codeunited.wmq.messaging.WMQDefaultConnectionFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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

    public static final String QMANAGER = "qmanager";

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
        if (hasOption("config")) {
            try (final FileInputStream propertiesStream = new FileInputStream(getOption("config"))) {
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
        if (hasOption("channel"))
            passedProperties.put(CHANNEL_PROPERTY, getOption("channel"));
        if (hasOption(QMANAGER))
            passedProperties.put(QMANAGER, getOption(QMANAGER));
        if (hasOption("host"))
            passedProperties.put(HOST_NAME_PROPERTY, getOption("host"));
        if (hasOption("port"))
            passedProperties.put(PORT_PROPERTY, Integer.valueOf(getOption("port")));
        if (hasOption("user"))
            passedProperties.put(USER_ID_PROPERTY, getOption("user"));
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

        LOG.info("Connecting with " + mergedProperties.toString());

        // perform connection
        try {
            final MQQueueManager mqQueueManager = connectionFactory.connectQueueManager(mergedProperties.getProperty(QMANAGER), mergedProperties);
            context.setQueueManager(mqQueueManager);

            // check connection
            if (mqQueueManager.isConnected()) {
                console.writeln("[" + mqQueueManager.getName() + "] connected");
            } else {
                throw new MQConnectionException("Connection performed but queue manager looks like disconnected");
            }
        } catch (MQException e) {
            throw new CommandGeneralException(e);
        }
    }

    @Override
    public boolean resolve() {
        return true;
    }
}
