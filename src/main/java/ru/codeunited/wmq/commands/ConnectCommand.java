package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import org.apache.commons.cli.CommandLine;
import ru.codeunited.wmq.WMQConnectionFactory;
import ru.codeunited.wmq.WMQDefaultConnectionFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;

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

    private final WMQConnectionFactory connectionFactory;

    public ConnectCommand() {
        connectionFactory = new WMQDefaultConnectionFactory();
    }

    public ConnectCommand(WMQConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    protected void work() throws CommandGeneralException {
        final CommandLine commandLine = getCommandLine();
        final ExecutionContext context = getExecutionContext();
        final ConsoleWriter console = getConsoleWriter();

        final String channelName = commandLine.getOptionValue("channel");
        final String queueManagerName = commandLine.getOptionValue("qmanager");
        final String host = (commandLine.hasOption("host") ? commandLine.getOptionValue("host") : DEFAULT_HOST);
        final int port = commandLine.hasOption("port") ? Integer.valueOf(commandLine.getOptionValue("port")) : DEFAULT_PORT;
        final String user = commandLine.getOptionValue("user");

        // print connection input parameters
        LOG.info(
                new StringBuilder()
                        .append("Perform connection.")
                        .append(" host [").append(host)
                        .append("], port [").append(port)
                        .append("], queue manager [").append(queueManagerName)
                        .append("], channel [").append(channelName)
                        .append("], user [").append(user != null ? user : "<empty>")
                        .append("]")
                        .toString());
        final Properties connectionProperties = createProps(host, port, channelName, user);
        try {
            final MQQueueManager mqQueueManager = connectionFactory.connectQueueManager(queueManagerName, connectionProperties);
            context.setQueueManager(mqQueueManager);

            // check connection
            if (mqQueueManager.isConnected()) {
                console.writeln("Connected to [" + queueManagerName + "]");
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

    /**
     * HOST     - arg[0]
     * PORT     - arg[1]
     * CHANNEL  - arg[2]
     * USER     - arg[3]
     * All if optional but order is strict.
     * If you want omit one or more params then set null in this position.
     *
     * @param args
     * @return
     */
    private static Properties createProps(Object... args) {
        final Properties properties = new Properties();
        if (args.length > 0 && args[0] != null)
            properties.put(HOST_NAME_PROPERTY, args[0]); // required
        if (args.length > 1 && args[1] != null)
            properties.put(PORT_PROPERTY, args[1]); // required
        if (args.length > 2 && args[2] != null)
            properties.put(CHANNEL_PROPERTY, args[2]); // required
        properties.put(TRANSPORT_PROPERTY, TRANSPORT_MQSERIES_CLIENT); // opt: if default not defined
        if (args.length > 3 && args[3] != null)
            properties.put(USER_ID_PROPERTY, args[3]); // opt: if necessary.
        return properties;
    }
}
