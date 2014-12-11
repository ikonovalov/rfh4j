package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.messaging.*;

import java.io.IOException;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public abstract class QueueingCapability extends CLITestSupport {

    private static final Logger LOG = Logger.getLogger(QueueingCapability.class.getName());

    protected static final int MESSAGE_ID_LENGTH = 24;

    public static interface QueueWork {
        public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException;
    }

    /**
     * Wrap QueueWork.work in connection and disconnection boundaries.
     * @param work
     * @throws Exception
     */
    public void communication(QueueWork work) throws Exception {
        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());

        final Command cmd1 = new MQConnectCommand().setContext(context);
        final Command cmd2 = new MQDisconnectCommand().setContext(context);

        cmd1.execute();
        try {
            work.work(context);
        } catch (RuntimeException rte){
            context.getQueueManager().backout();
            throw rte;
        } finally {
            cmd2.execute();
        }
    }

    protected MessageConsumerImpl getMessageConsumer(String queue, ExecutionContext context) throws MQException {
        return new MessageConsumerImpl(queue, context.getQueueManager());
    }

    protected QueueInspectorImpl getMessageInspector(String queue, ExecutionContext context) throws MQException {
        return new QueueInspectorImpl(queue, context.getQueueManager());
    }

    protected MQMessage putMessages(String queue, String text) throws ParseException, MissedParameterException, CommandGeneralException, IOException, MQException, IncompatibleOptionsException {
        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());

        final Command cmd1 = new MQConnectCommand().setContext(context);
        final Command cmd2 = new MQDisconnectCommand().setContext(context);

        cmd1.execute();
        final MessageProducer consumer = new MessageProducerImpl(queue, context.getQueueManager());
        // send first message
        final MQMessage message = consumer.send(text);
        assertThat(message, notNullValue());
        assertThat(message.messageId, notNullValue());
        assertThat(message.messageId.length, is(24));
        LOG.fine("Sent payload [" + text + "]");

        cmd2.execute();
        return message;
    }

    protected void cleanupQueue(String queueName) throws ParseException, MissedParameterException, CommandGeneralException, MQException, IncompatibleOptionsException {
        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());

        final Command cmd1 = new MQConnectCommand().setContext(context);
        final Command cmd2 = new MQDisconnectCommand().setContext(context);

        cmd1.execute();
        final MessageConsumer consumer = getMessageConsumer(queueName, context);
        boolean queueNotEmpty = true;
        while (queueNotEmpty) {
            try {
                consumer.get();
            } catch (NoMessageAvailableException e) {
                queueNotEmpty = false;
                System.out.println(queueName + " is cleanup");
            }
        }
        cmd2.execute();
    }
}
