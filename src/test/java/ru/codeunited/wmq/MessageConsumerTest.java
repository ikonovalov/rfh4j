package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.messaging.MessageConsumer;
import ru.codeunited.wmq.messaging.MessageSelector;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import java.io.IOException;
import java.util.logging.Logger;

import static com.ibm.mq.constants.CMQC.MQMO_MATCH_MSG_ID;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

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
    public void cleanupQueue() throws ParseException, MissedParameterException, CommandGeneralException, MQException {
        cleanupQueue(QUEUE);
    }

    @Test
    public void accessQueueWithDefOpenOptions() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException {

        putMessages(QUEUE, MESSAGE);

        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());

        final Command cmd1 = new ConnectCommand().setContext(context);
        final Command cmd2 = new DisconnectCommand().setContext(context);

        cmd1.execute();
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        final MQMessage message = consumer.get();
        assertThat(message, notNullValue());
        assertThat(message.messageId.length, is(24));
        final String payload = message.readStringOfByteLength(message.getDataLength());
        LOG.fine("Recieved payload [" + payload + "]");
        assertThat(payload, notNullValue());
        assertThat(payload.length(), equalTo(MESSAGE.length()));
        assertThat(payload, equalTo(MESSAGE));
        cmd2.execute();
    }

    @Test(timeout = 1000)
    public void accessQueueWithWait1000() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException {
        putMessages(QUEUE, MESSAGE);

        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());

        final Command cmd1 = new ConnectCommand().setContext(context);
        final Command cmd2 = new DisconnectCommand().setContext(context);

        cmd1.execute();
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        final MQMessage message = consumer.get(1000);
        assertThat(message, notNullValue());
        assertThat(message.messageId.length, is(24));
        cmd2.execute();
    }

    @Test(expected = NoMessageAvailableException.class)
    public void accessQueueWithNoMessagesException() throws ParseException, MissedParameterException, CommandGeneralException, MQException, IOException, NoMessageAvailableException {

        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());

        final Command connectCmd = new ConnectCommand().setContext(context);
        final Command disconnectCmd = new DisconnectCommand().setContext(context);

        connectCmd.execute();
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        try {
            consumer.get(10);
        } finally {
            disconnectCmd.execute();
        }
    }

    @Test
    public void selectMessageByMessageID() throws MissedParameterException, CommandGeneralException, MQException, NoMessageAvailableException, ParseException, IOException {

        final byte[] messageID = putMessages(QUEUE, MESSAGE);

        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());

        final Command connectCommand = new ConnectCommand().setContext(context);
        final Command disconnectCommand = new DisconnectCommand().setContext(context);

        final ReturnCode connectReturn = connectCommand.execute();
        assertThat(connectReturn, sameInstance(ReturnCode.SUCCESS));
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        try {
            final MQMessage selectedMessage = consumer.select(new MessageSelector() {
                @Override
                public MessageSelector setup(MQGetMessageOptions messageOptions, MQMessage message) {
                    messageOptions.matchOptions = MQMO_MATCH_MSG_ID;
                    message.messageId = messageID;
                    return this;
                }
            });
            assertThat(selectedMessage.messageId, equalTo(messageID));

        } catch (NoMessageAvailableException noMessages) {
            throw  noMessages;
        } finally {
            final ReturnCode disconnectReturn = disconnectCommand.execute();
            assertThat(disconnectReturn, sameInstance(ReturnCode.SUCCESS));
        }
    }


    @Test(expected = NoMessageAvailableException.class)
    public void selectMessageByMessageIDFail() throws MissedParameterException, CommandGeneralException, MQException, NoMessageAvailableException, ParseException, IOException {

        final byte[] messageID = putMessages(QUEUE, MESSAGE);
        messageID[10] = 0;

        final ExecutionContext context = new CLIExecutionContext(getCommandLine_With_Qc());

        final Command cmd1 = new ConnectCommand().setContext(context);
        final Command cmd2 = new DisconnectCommand().setContext(context);

        cmd1.execute();
        final MessageConsumer consumer = getMessageConsumer(QUEUE, context);
        try {
            consumer.select(new MessageSelector() {
                @Override
                public MessageSelector setup(MQGetMessageOptions messageOptions, MQMessage message) {
                    messageOptions.matchOptions = MQMO_MATCH_MSG_ID;
                    message.messageId = messageID;
                    return this;
                }
            });

        } finally {
            cmd2.execute();
            // remove putted message
            cleanupQueue(QUEUE);
        }
    }


}
