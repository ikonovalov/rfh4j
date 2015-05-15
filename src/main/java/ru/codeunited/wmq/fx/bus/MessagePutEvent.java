package ru.codeunited.wmq.fx.bus;

import com.google.common.base.Optional;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.fx.model.QueueBean;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 15.05.15.
 */
public class MessagePutEvent implements BusEvent {

    private Optional<Exception> problem = Optional.absent();

    private Optional<MQMessage> message = Optional.absent();

    private QueueBean queue;

    public MessagePutEvent(MQMessage mqMessage, QueueBean queueBean) {
        this.message = Optional.of(mqMessage);
        this.queue = queueBean;
    }

    public MessagePutEvent(Exception e, QueueBean queueBean) {
        this.problem = Optional.of(e);
        this.queue = queueBean;
    }

    public Optional<Exception> getProblem() {
        return problem;
    }

    public Optional<MQMessage> getMessage() {
        return message;
    }
}
