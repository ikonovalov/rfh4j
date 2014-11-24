package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.codeunited.wmq.commands.CommandGeneralException;
import ru.codeunited.wmq.commands.MissedParameterException;
import ru.codeunited.wmq.messaging.*;

import java.io.IOException;

import static com.ibm.mq.constants.CMQC.MQMO_MATCH_MSG_ID;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
public class MessageProducerTest extends QueueingCapability {

    private static final String QUEUE = "RFH.QTEST.PUT1";

    @Test
    public void singlePut() throws Exception {
        communication(new QueueWork() {

            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                final MessageProducer producer = new MessageProducerImpl(QUEUE, context.getQueueManager());
                final QueueInspector inspector = new QueueInspectorImpl(QUEUE, context.getQueueManager());
                final MessageConsumer consumer = new MessageConsumerImpl(QUEUE, context.getQueueManager());

                final byte[] putMessageID = producer.send("Hello " + System.currentTimeMillis()).messageId;
                assertThat(inspector.depth(), is(1));
                assertThat(putMessageID, notNullValue());
                assertThat(putMessageID.length, is(MESSAGE_ID_LENGTH));

                final byte[] selectedMessageID = consumer.select(new MessageSelector() {
                    @Override
                    public MessageSelector setup(MQGetMessageOptions messageOptions, MQMessage message) {
                        messageOptions.matchOptions = MQMO_MATCH_MSG_ID;
                        message.messageId = putMessageID;
                        return this;
                    }
                }).messageId;

                assertThat(putMessageID, equalTo(selectedMessageID));
            }

        });
    }

    @Test(timeout = 5000)
    public void put100() throws Exception {
        communication(new QueueWork() {

            final int messageCount = 100;

            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                final MessageProducer producer = new MessageProducerImpl(QUEUE, context.getQueueManager());
                int limit = messageCount;
                while (limit-->0)
                    producer.send("MSG");

                final QueueInspector inspector = new QueueInspectorImpl(QUEUE, context.getQueueManager());
                assertThat(inspector.depth(), is(messageCount));
            }
        });

        cleanUp();
    }

    @After
    @Before
    public void cleanUp() throws MissedParameterException, CommandGeneralException, MQException, ParseException {
        cleanupQueue(QUEUE);
    }
}
