package ru.codeunited.wmq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQTopic;
import ru.codeunited.wmq.messaging.MessageTools;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import java.io.IOException;
import java.util.Date;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 11.02.15.
 */
public class TopicPub extends QueueingCapability {

    public static void main(String[] args) throws Exception {
        new TopicPub().pub();
    }

    void pub() throws Exception {
        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws MQException, IOException, NoMessageAvailableException {
                final MQQueueManager queueManager = context.getLink().getManager().get();


                //MQTopic subscriber = queueManager.accessTopic("LXFT/GLOBAL/+", null, MQTOPIC_OPEN_AS_SUBSCRIPTION, MQSO_CREATE  );


                //MQTopic publisher = queueManager.accessTopic("ITCI", "LXFT.GLOBAL.NEWS.TOPIC", MQTOPIC_OPEN_AS_PUBLICATION, MQOO_OUTPUT | MQOO_FAIL_IF_QUIESCING);
                MQTopic publisher = queueManager.accessTopic("/LXFT/GLOBAL", null, MQTOPIC_OPEN_AS_PUBLICATION, MQOO_OUTPUT | MQOO_FAIL_IF_QUIESCING);

                MQMessage message = MessageTools.createUTFMessage();
                message.write(String.format("Hello LXFT Omsk: %s", new Date().toString()).getBytes());
                publisher.put(message);
                publisher.close();


                /*final MQMessage subMessage = new MQMessage();
                subscriber.get(subMessage);

                System.out.println("SUBSCRIBTION: " + subMessage.readStringOfByteLength(subMessage.getDataLength()));*/

                /*if (subscriber.isSubscribed()) {
                    subscriber.getSubscriptionReference().setCloseOptions(MQCO_REMOVE_SUB);
                    subscriber.getSubscriptionReference().close();
                    subscriber.close();
                }*/

            }
        });
    }
}
