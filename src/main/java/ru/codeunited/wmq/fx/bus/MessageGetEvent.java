package ru.codeunited.wmq.fx.bus;

import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.fx.model.QueueBean;
import ru.codeunited.wmq.messaging.NoMessageAvailableException;

import com.google.common.base.Optional;


/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 15.05.15.
 */
public class MessageGetEvent implements BusEvent {

    private Optional<MQMessage> message = Optional.absent();

    private Optional<NoMessageAvailableException> noMessages = Optional.absent();

    private Optional<Exception> otherProblem = Optional.absent();

    private final QueueBean queue;

    public MessageGetEvent(MQMessage message, QueueBean queue) {
        this.message = Optional.of(message);
        this.queue = queue;
    }

    public MessageGetEvent(NoMessageAvailableException e, QueueBean queue) {
        this.queue = queue;
        this.noMessages = Optional.of(e);
    }

    public MessageGetEvent(Exception e, QueueBean queue) {
        this.queue = queue;
        this.otherProblem = Optional.of(e);
    }

    public Optional<MQMessage> getMessage() {
        return message;
    }

    public Optional<NoMessageAvailableException> getNoMessages() {
        return noMessages;
    }

    public Optional<Exception> getOtherProblem() {
        return otherProblem;
    }
}
