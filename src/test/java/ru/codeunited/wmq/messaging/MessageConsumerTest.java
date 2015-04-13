package ru.codeunited.wmq.messaging;

import com.google.inject.Key;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.QueueingCapability;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.handler.NestedHandlerException;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.QueueInspector;
import ru.codeunited.wmq.messaging.MessageSelector;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import java.io.IOException;
import java.util.logging.Logger;

import static com.ibm.mq.constants.CMQC.MQMO_MATCH_MSG_ID;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static ru.codeunited.wmq.frame.CLITestSupport.getCommandLine_With_Qc;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 17.11.14.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class})
public class MessageConsumerTest extends QueueingCapability {

    private static final String MESSAGE = "WebSphere MQ " + System.currentTimeMillis();

    private static final Logger LOG = Logger.getLogger(MessageConsumer.class.getName());

    private static final String QUEUE = "RFH.QTEST.GET1";



    @Before @After
    public void cleanupQueue() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IncompatibleOptionsException, NestedHandlerException {
        cleanupQueue(QUEUE);
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void accessQueueWithDefOpenOptions() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException, IncompatibleOptionsException, NestedHandlerException {

        connectCommand.execute();

        putMessages(QUEUE, MESSAGE);

        MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        MQMessage message = consumer.get();
        assertThat(message, notNullValue());
        assertThat(message.messageId.length, is(24));
        String payload = message.readStringOfByteLength(message.getDataLength());
        LOG.fine("Recieved payload [" + payload + "]");
        assertThat(payload, notNullValue());
        assertThat(payload.length(), equalTo(MESSAGE.length()));
        assertThat(payload, equalTo(MESSAGE));

        disconnectCommand.execute();
    }

    @Test(timeout = 1000)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void accessQueueWithWait1000() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException, IncompatibleOptionsException, NestedHandlerException {
        connectCommand.execute();

        putMessages(QUEUE, MESSAGE);

        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        final MQMessage message = consumer.get(1000);
        assertThat(message, notNullValue());
        assertThat(message.messageId.length, is(24));

        disconnectCommand.execute();
    }

    @Test(expected = NoMessageAvailableException.class)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void accessQueueWithNoMessagesException() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException, IncompatibleOptionsException, NestedHandlerException {

        connectCommand.execute();
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        try {
            consumer.get(10);
        } finally {
            disconnectCommand.execute();
        }
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void selectMessageByMessageID() throws MissedParameterException, CommandGeneralException, MQException, NoMessageAvailableException, ParseException, IOException, IncompatibleOptionsException, NestedHandlerException {
        connectCommand.execute();

        final byte[] messageID = putMessages(QUEUE, MESSAGE).messageId;

        final ReturnCode connectReturn = connectCommand.execute();
        assertThat(connectReturn, sameInstance(ReturnCode.SUCCESS));
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        try {
            final MQMessage selectedMessage = consumer.select(new MessageSelector() {
                @Override
                public void setup(MQGetMessageOptions messageOptions, MQMessage message) {
                    messageOptions.matchOptions = MQMO_MATCH_MSG_ID;
                    message.messageId = messageID;
                }
            });
            assertThat(selectedMessage.messageId, equalTo(messageID));

        } finally {
            final ReturnCode disconnectReturn = disconnectCommand.execute();
            assertThat(disconnectReturn, sameInstance(ReturnCode.SUCCESS));
        }
    }


    @Test(expected = NoMessageAvailableException.class)
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void selectMessageByMessageIDFail() throws MissedParameterException, CommandGeneralException, MQException, NoMessageAvailableException, ParseException, IOException, IncompatibleOptionsException, NestedHandlerException {
        connectCommand.execute();

        final byte[] messageID = putMessages(QUEUE, MESSAGE).messageId;
        messageID[10] = 0;

        connectCommand.execute();
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        try {
            consumer.select(new MessageSelector() {
                @Override
                public void setup(MQGetMessageOptions messageOptions, MQMessage message) {
                    messageOptions.matchOptions = MQMO_MATCH_MSG_ID;
                    message.messageId = messageID;
                }
            });

        } finally {
            disconnectCommand.execute();
        }
    }

    @Test
    public void discoverDepth() throws MissedParameterException, CommandGeneralException, MQException, ParseException, IOException, IncompatibleOptionsException, NestedHandlerException {

        ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());
        setup(context);

        Command connectCmd = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command disconnectCmd = injector.getInstance(Key.get(Command.class, ConnectCommand.class));

        connectCmd.execute();
        final QueueInspector consumer = getMessageInspector(QUEUE, context);
        try {
            assertThat(consumer.depth(), is(0));
            putMessages(QUEUE, "Some text here");
            assertThat(consumer.depth(), is(1));
            cleanupQueue(QUEUE);
            assertThat(consumer.depth(), is(0));
        } finally {
            disconnectCmd.execute();
        }
    }

    @Test
    public void discoverMaxDepth() throws MissedParameterException, CommandGeneralException, MQException, ParseException, IOException, IncompatibleOptionsException, NestedHandlerException {
        ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());
        setup(context);

        Command connectCmd = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command disconnectCmd = injector.getInstance(Key.get(Command.class, ConnectCommand.class));

        connectCmd.execute();
        final QueueInspector consumer = getMessageInspector(QUEUE, context);
        try {
            assertThat(consumer.maxDepth(), is(5000));
        } finally {
            disconnectCmd.execute();
        }
    }
}
