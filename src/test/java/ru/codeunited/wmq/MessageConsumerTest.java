package ru.codeunited.wmq;

import com.google.inject.Key;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;
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
public class MessageConsumerTest extends QueueingCapability {

    private static final String MESSAGE = "WebSphere MQ " + System.currentTimeMillis();

    private static final Logger LOG = Logger.getLogger(MessageConsumer.class.getName());

    private static final String QUEUE = "RFH.QTEST.GET1";



    @Before
    public void cleanupQueue() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IncompatibleOptionsException, NestedHandlerException {
        cleanupQueue(QUEUE);
    }

    @Test
    public void accessQueueWithDefOpenOptions() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException, IncompatibleOptionsException, NestedHandlerException {

        putMessages(QUEUE, MESSAGE);

        setup(getCommandLine_With_Qc());
        Command cmd1 = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command cmd2 = injector.getInstance(Key.get(Command.class, DisconnectCommand.class));

        cmd1.execute();
        MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        MQMessage message = consumer.get();
        assertThat(message, notNullValue());
        assertThat(message.messageId.length, is(24));
        String payload = message.readStringOfByteLength(message.getDataLength());
        LOG.fine("Recieved payload [" + payload + "]");
        assertThat(payload, notNullValue());
        assertThat(payload.length(), equalTo(MESSAGE.length()));
        assertThat(payload, equalTo(MESSAGE));
        cmd2.execute();
    }

    @Test(timeout = 1000)
    public void accessQueueWithWait1000() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException, IncompatibleOptionsException, NestedHandlerException {
        putMessages(QUEUE, MESSAGE);

        setup(getCommandLine_With_Qc());
        Command connectCmd = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command disconnectCmd = injector.getInstance(Key.get(Command.class, ConnectCommand.class));

        connectCmd.execute();
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        final MQMessage message = consumer.get(1000);
        assertThat(message, notNullValue());
        assertThat(message.messageId.length, is(24));
        disconnectCmd.execute();
    }

    @Test(expected = NoMessageAvailableException.class)
    public void accessQueueWithNoMessagesException() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException, IncompatibleOptionsException, NestedHandlerException {
        ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());
        setup(context);

        Command connectCmd = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command disconnectCmd = injector.getInstance(Key.get(Command.class, ConnectCommand.class));

        connectCmd.execute();
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        try {
            consumer.get(10);
        } finally {
            disconnectCmd.execute();
        }
    }

    @Test
    public void selectMessageByMessageID() throws MissedParameterException, CommandGeneralException, MQException, NoMessageAvailableException, ParseException, IOException, IncompatibleOptionsException, NestedHandlerException {

        final byte[] messageID = putMessages(QUEUE, MESSAGE).messageId;

        ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());
        setup(context);

        Command connectCommand = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command disconnectCommand = injector.getInstance(Key.get(Command.class, DisconnectCommand.class));

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
    public void selectMessageByMessageIDFail() throws MissedParameterException, CommandGeneralException, MQException, NoMessageAvailableException, ParseException, IOException, IncompatibleOptionsException, NestedHandlerException {

        final byte[] messageID = putMessages(QUEUE, MESSAGE).messageId;
        messageID[10] = 0;

        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());
        setup(context);

        Command connectCommand = injector.getInstance(Key.get(Command.class, ConnectCommand.class));
        Command disconnectCommand = injector.getInstance(Key.get(Command.class, DisconnectCommand.class));

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
            // remove putted message
            cleanupQueue(QUEUE);
        }
    }

    @Test
    public void discoverDepth() throws MissedParameterException, CommandGeneralException, MQException, ParseException, IOException, IncompatibleOptionsException, NestedHandlerException {
        cleanupQueue(QUEUE);

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
        cleanupQueue(QUEUE);

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
