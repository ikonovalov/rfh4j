package ru.codeunited.wmq.commands;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.MQCLIPropertiesComposer;
import ru.codeunited.wmq.messaging.ConnectionOptions;
import ru.codeunited.wmq.messaging.MQLink;
import ru.codeunited.wmq.messaging.WMQConnectionFactory;
import ru.codeunited.wmq.messaging.impl.WMQDefaultConnectionFactory;

import javax.inject.Inject;

import static ru.codeunited.wmq.RFHConstants.*;
import java.util.Properties;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class MQConnectCommand extends AbstractCommand {

    @Inject
    private WMQConnectionFactory connectionFactory;

    public MQConnectCommand() {
        super();
        connectionFactory = new WMQDefaultConnectionFactory();
    }

    @Inject
    public MQConnectCommand(WMQConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    protected void work() throws CommandGeneralException {
        final ExecutionContext context = getExecutionContext();

        final Properties mergedProperties = new MQCLIPropertiesComposer(context).compose();
        final String queueManagerName = mergedProperties.getProperty(OPT_QMANAGER);

        final ConnectionOptions connectionOptions = new ConnectionOptions(queueManagerName).withOptions(mergedProperties);

        LOG.fine("Connecting to [" + queueManagerName + "] with " + mergedProperties.toString());

        // perform connection
        try {
            MQLink mqLink = connectionFactory.connectQueueManager(connectionOptions);
            context.setLink(mqLink);
            // check connection
            if (mqLink.getManager().isConnected()) {
                LOG.fine("[" + connectionOptions.getQueueManagerName() + "] connected");
            } else {
                throw new MQConnectionException("Connection performed but queue manager looks like disconnected");
            }
        } catch (MQException e) {
            throw new CommandGeneralException(e);
        }
    }
}
