package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import org.apache.commons.lang3.StringUtils;
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
import ru.codeunited.wmq.messaging.impl.MessageConsumerImpl;
import ru.codeunited.wmq.messaging.impl.MessageProducerImpl;
import ru.codeunited.wmq.messaging.pcf.ActivityTraceCommand;
import ru.codeunited.wmq.messaging.pcf.MQHeaderException;
import ru.codeunited.wmq.messaging.pcf.PCFUtilService;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.logging.Logger;

import static com.ibm.mq.constants.MQConstants.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 16.04.15.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class, FormatterModule.class, MessagingModule.class})
public class DebugDepFinFormatter extends QueueingCapability {

    private static final Logger LOG = Logger.getLogger(DebugDepFinFormatter.class.getName());

    private final String THE_QUEUE = "RFH.QTEST.QGENERAL1";

    private final String ACTIVITY_QUEUE = "SYSTEM.ADMIN.TRACE.ACTIVITY.QUEUE";

    @Inject
    private FormatterFactory formatterFactory;

    @After
    @Before
    public void cleanUp() throws Exception {
        cleanupQueue(THE_QUEUE);
        cleanupQueue(ACTIVITY_QUEUE);
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN --transport=client --formatter=ru.codeunited.wmq.format.MQFMTAdminActivityTraceFormatterDepFin")
    public void spawnRFH2Actitvity() throws Exception {

        assumeActivityLogEnable();

        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws Exception {
                try (
                        final MessageProducer producer = new MessageProducerImpl(THE_QUEUE, context.getLink());
                        final MessageConsumer consumer = new MessageConsumerImpl(ACTIVITY_QUEUE, context.getLink());
                ) {
                    final MQMessage sentMessage = producer.send(new CustomSendAdjuster() {
                        @Override
                        public void setup(MQMessage message) throws IOException, MQException {
                            message.setStringProperty("id", "i15");
                            message.setStringProperty("type", "t15");
                            message.setStringProperty("status", "s15");
                            message.writeString("and some data here");
                            message.format = MQFMT_NONE;
                            message.persistence = MQPER_NOT_PERSISTENT;
                        }

                        @Override
                        public void setup(MQPutMessageOptions options) {
                            options.options = MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT;
                        }
                    });

                    final String sendMessageId = MessageTools.bytesToHex(sentMessage.messageId);
                    LOG.info("Sent message with " + sendMessageId);
                    boolean dontStop = true;
                    while (dontStop) {
                        try {
                            MQMessage message = consumer.get();
                            MessageFormatter<String> formatter = formatterFactory.formatterFor(message);
                            String out = formatter.format(message);
                            if (StringUtils.isNotEmpty(out)) {
                                LOG.info("\n" + out);
                            }
                            if (StringUtils.isNotEmpty(out) && out.contains(sendMessageId.toLowerCase())) { // skip empty (restricted rows)
                                assertThat(out, containsString(";[bytes]"));

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

    @Test
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN --transport=client --formatter=ru.codeunited.wmq.format.MQFMTAdminActivityTraceFormatterDepFin[usr.id;usr.type;usr.status]")
    public void spawnRFH2ActitvityConfiguredFields() throws Exception {

        assumeActivityLogEnable();

        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws Exception {
                try (
                        final MessageProducer producer = new MessageProducerImpl(THE_QUEUE, context.getLink());
                        final MessageConsumer consumer = new MessageConsumerImpl(ACTIVITY_QUEUE, context.getLink());
                ) {
                    final MQMessage sentMessage = producer.send(new CustomSendAdjuster() {
                        @Override
                        public void setup(MQMessage message) throws IOException, MQException {
                            message.setStringProperty("id", "i15");
                            message.setStringProperty("type", "t15");
                            message.setStringProperty("status", "s15");
                            message.writeString("and some data here");
                            message.format = MQFMT_NONE;
                            message.persistence = MQPER_NOT_PERSISTENT;
                        }

                        @Override
                        public void setup(MQPutMessageOptions options) {
                            options.options = MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT;
                        }
                    });

                    final String sendMessageId = MessageTools.bytesToHex(sentMessage.messageId);
                    LOG.info("Sent message with " + sendMessageId);
                    boolean dontStop = true;
                    while (dontStop) {
                        try {
                            MQMessage message = consumer.get();
                            MessageFormatter<String> formatter = formatterFactory.formatterFor(message);
                            String out = formatter.format(message);
                            if (StringUtils.isNotEmpty(out)) {
                                LOG.info("\n" + out);
                            }
                            if (StringUtils.isNotEmpty(out) && out.contains(sendMessageId.toLowerCase())) { // skip empty (restricted rows)
                                assertThat(out, containsString(";i15;t15;s15;[bytes]"));

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
}

