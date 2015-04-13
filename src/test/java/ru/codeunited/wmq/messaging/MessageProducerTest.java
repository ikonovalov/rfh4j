package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.QueueingCapability;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.messaging.*;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.MessageProducer;
import ru.codeunited.wmq.messaging.impl.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.impl.MessageProducerImpl;
import ru.codeunited.wmq.messaging.impl.QueueInspectorImpl;

import java.io.IOException;

import static com.ibm.mq.constants.CMQC.MQMO_MATCH_MSG_ID;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class})
public class MessageProducerTest extends QueueingCapability {

    private static final String QUEUE = "RFH.QTEST.PUT1";

    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void singlePut() throws Exception {
        communication(new QueueWork() {

            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                final MessageProducer producer = new MessageProducerImpl(QUEUE, context.getLink());
                final QueueInspector inspector = new QueueInspectorImpl(QUEUE, context.getLink());
                final MessageConsumer consumer = new MessageConsumerImpl(QUEUE, context.getLink());

                final byte[] putMessageID = producer.send("Hello " + System.currentTimeMillis()).messageId;
                assertThat(inspector.depth(), is(1));
                assertThat(putMessageID, notNullValue());
                assertThat(putMessageID.length, is(MESSAGE_ID_LENGTH));

                final byte[] selectedMessageID = consumer.select(new MessageSelector() {
                    @Override
                    public void setup(MQGetMessageOptions messageOptions, MQMessage message) {
                        messageOptions.matchOptions = MQMO_MATCH_MSG_ID;
                        message.messageId = putMessageID;
                    }
                }).messageId;

                assertThat(putMessageID, equalTo(selectedMessageID));
            }

        });
    }

    @Test(timeout = 5000)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void put100() throws Exception {
        communication(new QueueWork() {

            final int messageCount = 100;

            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                final MessageProducer producer = new MessageProducerImpl(QUEUE, context.getLink());
                int limit = messageCount;
                while (limit-->0)
                    producer.send("MSG");

                final QueueInspector inspector = new QueueInspectorImpl(QUEUE, context.getLink());
                assertThat(inspector.depth(), is(messageCount));
            }
        });
    }

    @After
    @Before
    public void cleanUp() throws Exception {
        cleanupQueue(QUEUE);
    }
}
