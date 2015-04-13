package ru.codeunited.wmq;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import org.apache.commons.cli.ParseException;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.handler.NestedHandlerException;
import ru.codeunited.wmq.messaging.*;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.MessageProducer;
import ru.codeunited.wmq.messaging.impl.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.impl.MessageProducerImpl;
import ru.codeunited.wmq.messaging.impl.QueueInspectorImpl;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static ru.codeunited.wmq.frame.CLITestSupport.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
public abstract class QueueingCapability extends GuiceSupport {

    private static final Logger LOG = Logger.getLogger(QueueingCapability.class.getName());

    protected static final int MESSAGE_ID_LENGTH = 24;

    @Inject protected CommandChain commandChain;

    @ConnectCommand
    @Inject protected Command connectCommand;

    @PutCommand
    @Inject protected Command putCommand;

    @DisconnectCommand
    @Inject protected Command disconnectCommand;


    public interface QueueWork {
        void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException;
    }

    /**
     * Wrap QueueWork.work in connection and disconnection boundaries.
     * @param work
     * @throws Exception
     */
    public void communication(QueueWork work) throws Exception {
        connectCommand.execute();
        try {
            work.work(context);
        } catch (RuntimeException rte){
            context.getLink().getManager().get().backout();
            throw rte;
        } finally {
            disconnectCommand.execute();
        }
    }

    public static MessageConsumerImpl getMessageConsumer(String queue, ExecutionContext context) throws MQException {
        return new MessageConsumerImpl(queue, context.getLink());
    }

    protected QueueInspectorImpl getMessageInspector(String queue, ExecutionContext context) throws MQException {
        return new QueueInspectorImpl(queue, context.getLink());
    }

    protected MQMessage putMessages(String queue, String text) throws ParseException, MissedParameterException, CommandGeneralException, IOException, MQException, IncompatibleOptionsException, NestedHandlerException {
        final MessageProducer consumer = new MessageProducerImpl(queue, context.getLink());
        // send first message
        final MQMessage message = consumer.send(text);
        assertThat(message, notNullValue());
        assertThat(message.messageId, notNullValue());
        assertThat(message.messageId.length, is(MESSAGE_ID_LENGTH));
        LOG.fine("Sent payload [" + text + "]");

        return message;
    }

    protected static void cleanupQueue(String queueName) throws Exception {
        ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());
        Injector injector = getStandartInjector(context);
        Command cmd1 = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command cmd2 = injector.getInstance(Key.get(Command.class, DisconnectCommand.class));

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
