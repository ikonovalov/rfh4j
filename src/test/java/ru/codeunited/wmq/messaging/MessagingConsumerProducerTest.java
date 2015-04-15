package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.MQHeader;
import com.ibm.mq.headers.MQHeaderIterator;
import com.ibm.mq.headers.MQRFH2;
import org.apache.commons.lang3.tuple.Pair;
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
import ru.codeunited.wmq.messaging.impl.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.impl.MessageProducerImpl;
import ru.codeunited.wmq.messaging.pcf.*;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import static com.ibm.mq.constants.MQConstants.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 13.04.15.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class})
public class MessagingConsumerProducerTest extends QueueingCapability {

    private static final Logger LOG = Logger.getLogger(MessagingConsumerProducerTest.class.getName());

    private final String THE_QUEUE = "RFH.QTEST.QGENERAL1";

    private final String ACTIVITY_QUEUE = "SYSTEM.ADMIN.TRACE.ACTIVITY.QUEUE";

    @After @Before
    public void cleanUp() throws Exception {
        cleanupQueue(THE_QUEUE);
        //cleanupQueue(ACTIVITY_QUEUE);
    }

    /**
     * Really hard thing.
     * @throws Exception
     */
    @Test(timeout = 120000L)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN --transport=client") /* use CLIENT mode to prevent hiding trace */
    public void lookupActivity() throws Exception {

        /* check that activity trace enabled    */
        /* Skip if it's not                     */
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                QueueManager qm = context.getLink().getManager();
                Pair<Object, String> valuePair = qm.getAttributes().get("MQIA_ACTIVITY_TRACE");
                assumeTrue("Activity trace disabled for " + qm.getName(), valuePair.getLeft().equals(1));
            }
        });

        // put test messages and get activity
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                try (final MessageProducer producer = new MessageProducerImpl(THE_QUEUE, context.getLink());
                     final MessageConsumer consumer = new MessageConsumerImpl(ACTIVITY_QUEUE, context.getLink())) {

                    // we put message
                    final MQMessage sentMessage = producer.send(new CustomSendAdjuster() {
                        @Override
                        public void setup(MQMessage message) throws IOException, MQException {
                            message.setStringProperty("myVar", "S13");
                            message.writeString("I like WebSphere MQ");
                            message.format = MQFMT_NONE;
                            message.persistence = MQPER_NOT_PERSISTENT;
                        }

                        @Override
                        public void setup(MQPutMessageOptions options) {
                            options.options = MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT;
                        }
                    });

                    final String sendMessageId = MessageTools.bytesToHex(sentMessage.messageId);
                    System.out.println("Sent\n" + sendMessageId);

                    boolean dontStop = true;
                    while(dontStop) {
                        try {
                            MQMessage message = consumer.get();
                            ActivityTraceCommand traceCommand = PCFUtilService.activityCommandFor(message);
                            for(ActivityTraceRecord atr : traceCommand.getRecords()) {
                                if (atr.getOperation() == MQXFOperation.MQXF_PUT && atr.isSuccess()) {
                                    MQXFPutRecord putRecord = (MQXFPutRecord) atr;
                                    String recordMessageId = putRecord.getMessageId();
                                    boolean thisMessage = recordMessageId.equalsIgnoreCase(sendMessageId);
                                    LOG.info("Got\n" + putRecord.getMessageId() + (thisMessage ? "+" : "-"));
                                    if (thisMessage) {
                                        LOG.fine(putRecord.toString());
                                        byte[] body = putRecord.getDataRaw();

                                        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(body));
                                        try {
                                            String format = putRecord.getFormat();
                                            MQRFH2 mqrfh2 = new MQRFH2(dataInput);
                                            int stuctLen = mqrfh2.getStrucLength();
                                            System.out.println(Arrays.asList(mqrfh2.getFolderStrings()));
                                            System.out.println("struct len " + stuctLen + " total len " + body.length);
                                        } catch (MQDataException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (NoMessageAvailableException noMessage) {
                            dontStop = false;
                        }
                    }

                }

            }
        });
    }

    @Test(timeout = 5000L)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN --transport=client")
    public void testAdvancedProducer$fullyCustom() throws Exception {
        communication(new QueueWork() {

            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                try (
                        final MessageProducer producer = new MessageProducerImpl(THE_QUEUE, context.getLink());
                        final MessageConsumer consumer = new MessageConsumerImpl(THE_QUEUE, context.getLink())
                ) {
                    // sending message
                    final MQMessage sentMessage = producer.send(new CustomSendAdjuster() {
                        @Override
                        public void setup(MQMessage message) throws IOException, MQException {
                            message.setStringProperty("myVar", "S13");
                            message.writeString("OK");
                            message.persistence = MQPER_NOT_PERSISTENT;
                        }

                        @Override
                        public void setup(MQPutMessageOptions options) {
                            options.options = MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT;
                        }
                    });

                    final byte[] messageId = sentMessage.messageId;

                    // getting (selecting) message by ID
                    final MQMessage selectedMessage = consumer.select(new MessageSelector() {
                        @Override
                        public void setup(MQGetMessageOptions messageOptions, MQMessage message) {
                            messageOptions.options = MQGMO_FAIL_IF_QUIESCING | MQGMO_NO_SYNCPOINT | MQGMO_NO_WAIT | MQGMO_PROPERTIES_FORCE_MQRFH2;
                            messageOptions.matchOptions =  MQMO_MATCH_MSG_ID;
                            message.messageId = messageId;
                        }
                    });

                    MQHeaderIterator headerIterator = new MQHeaderIterator(selectedMessage);
                    int totalHeader = 0;
                    boolean hasRHF2Header = false;
                    while (headerIterator.hasNext ())
                    {
                        MQHeader header = headerIterator.nextHeader();
                        if (header instanceof MQRFH2) {
                            MQRFH2 mqrfh2 = (MQRFH2) header;
                            String passedValue = (String) mqrfh2.getFieldValue("usr", "myVar");
                            assertThat("Passed via RFH2 header usr properties not match", passedValue, equalTo("S13"));
                            hasRHF2Header = true;
                        }
                        totalHeader++;
                    }
                    assertThat("Where is not header or MQGMO_PROPERTIES_FORCE_MQRFH2 not set or PROPCTL not setup properly for a forcing RHF2", totalHeader > 0, is(true));
                    assertThat("Where is no RFH2 header", hasRHF2Header, is(true));
                    assertThat("Sent message id not equals selected message id", sentMessage.messageId, equalTo(selectedMessage.messageId));
                }
            }
        });
    }
}
