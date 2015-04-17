package ru.codeunited.wmq.messaging;

import com.google.common.base.Optional;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
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

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import static com.ibm.mq.constants.MQConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 13.04.15.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class, MessagingModule.class})
public class MessagingConsumerProducerTest extends QueueingCapability {

    private static final Logger LOG = Logger.getLogger(MessagingConsumerProducerTest.class.getName());

    private final String THE_QUEUE = "RFH.QTEST.QGENERAL1";

    private final String ACTIVITY_QUEUE = "SYSTEM.ADMIN.TRACE.ACTIVITY.QUEUE";

    private final String CAPTURED_MESSAGE =
            "524648200000000200000054000000" +
            "01000004B82020202020202020000000" +
            "00000004B80000002C3C7573723E3C6D" +
            "795661722064743D22737472696E6722" +
            "3E5331333C2F6D795661723E3C2F7573" +
            "723E20202049206C696B652057656253" +
            "7068657265204D51";

    @After @Before
    public void cleanUp() throws Exception {
        cleanupQueue(THE_QUEUE);
        cleanupQueue(ACTIVITY_QUEUE);
    }

    @Test
    public void parseHeaders() throws MQHeaderException {

        byte[] payload = new BigInteger(CAPTURED_MESSAGE, 16).toByteArray();

        MQXFMessageMoveRecord record = mock(MQXFMessageMoveRecord.class);
        when(record.getFormat()).thenReturn(MQFMT_RF_HEADER_2);
        when(record.getEncoding()).thenReturn(1);
        when(record.getCCSID()).thenReturn(1208);
        when(record.getDataRaw()).thenReturn(payload);

        TraceData traceData = TraceDataImpl.create(record);
        List<MQHeader> headerList = traceData.getHeaders().get();
        assertThat("Wrong header list size", headerList.size(), is(1));
        assertThat("Wrong header type", headerList.get(0), instanceOf(MQRFH2.class));

        Optional<Object> bodyOpt = traceData.getBody();
        assertThat(bodyOpt.get(), instanceOf(byte[].class));
    }

    @Test(expected = MQHeaderException.class)
    public void corruptedMessage() throws MQHeaderException {
        byte[] payload = new BigInteger(CAPTURED_MESSAGE, 16).toByteArray();
        payload[10] = 100;
        MQXFMessageMoveRecord record = mock(MQXFMessageMoveRecord.class);
        when(record.getFormat()).thenReturn(MQFMT_RF_HEADER_2);
        when(record.getEncoding()).thenReturn(1);
        when(record.getCCSID()).thenReturn(1208);
        when(record.getDataRaw()).thenReturn(payload);

        TraceData traceData = TraceDataImpl.create(record);

        traceData.getHeaders();
    }

    @Test
    public void emptyDataRowAndParse() throws MQHeaderException {
        byte[] payload = null;
        MQXFMessageMoveRecord record = mock(MQXFMessageMoveRecord.class);
        when(record.getFormat()).thenReturn(MQFMT_RF_HEADER_2);
        when(record.getEncoding()).thenReturn(1);
        when(record.getCCSID()).thenReturn(1208);
        when(record.getDataRaw()).thenReturn(payload);

        TraceData traceData = TraceDataImpl.create(record);

        Optional<List<MQHeader>> headerListOpt = traceData.getHeaders();
        assertThat("Header list shoube be absent", headerListOpt.isPresent(), is(false));

        Optional<Object> body = traceData.getBody();
        assertThat(body.isPresent(), is(false));
    }

    /**
     * Really hard thing.
     * @throws Exception
     */
    @Test(timeout = 10000L)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN --transport=client") /* use CLIENT mode to prevent hiding trace */
    public void lookupActivity() throws Exception {

        /* check that activity trace enabled    */
        /* Skip if it's not                     */
        assumeActivityLogEnable();

        // put test messages and get activity
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws Exception {
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

                                        byte[] dataRaw = putRecord.getDataRaw();

                                        assertThat("Body is null", dataRaw, notNullValue());
                                        assertThat("Body is empty", dataRaw.length > 0, is(true));

                                        TraceData traceData = putRecord.getData();

                                        List<MQHeader> listOfHeaders = traceData.getHeaders().get();
                                        assertThat("Wrong header list size", listOfHeaders.size(), is(1));

                                        Object capturedBody = traceData.getBody().get();
                                        assertThat("Original message was MQFMT_NONE, so body should be threated as bytes", capturedBody, instanceOf(byte[].class));



                                        /*DataInput dataInput = new DataInputStream(new ByteArrayInputStream(body));
                                        try {
                                            String format = putRecord.getFormat();
                                            MQRFH2 mqrfh2 = new MQRFH2(dataInput);
                                            int stuctLen = mqrfh2.getStrucLength();
                                            System.out.println(Arrays.asList(mqrfh2.getFolderStrings()));
                                            System.out.println("struct len " + stuctLen + " total len " + body.length);
                                        } catch (MQDataException e) {
                                            e.printStackTrace();
                                        }*/
                                    }
                                }
                            }
                        } catch (NoMessageAvailableException noMessage) {
                            dontStop = false;
                        } catch (MQHeaderException e) {
                            throw new RuntimeException(e);
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
            public void work(ExecutionContext context) throws Exception {
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
