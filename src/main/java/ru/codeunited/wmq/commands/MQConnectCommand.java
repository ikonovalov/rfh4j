package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.cli.CLIPropertiesComposer;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.messaging.WMQConnectionFactory;
import ru.codeunited.wmq.messaging.WMQDefaultConnectionFactory;

import java.util.Properties;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQConnectCommand extends AbstractCommand {

    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 1414;

    private static final String DEFAULT_CHANNEL = "SYSTEM.DEF.SVRCONN";

    private final WMQConnectionFactory connectionFactory;


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


    @Override
    protected void work() throws CommandGeneralException {
        final ExecutionContext context = getExecutionContext();

        final Properties mergedProperties = new CLIPropertiesComposer(context).compose();

        final String queueManagerName = mergedProperties.getProperty(CLIFactory.OPT_QMANAGER);

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
