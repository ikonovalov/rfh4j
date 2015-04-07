package ru.codeunited.wmq.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.util.Objects;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public final class QueueBean {

    private final StringProperty name;

    private Queue queue;

    private QueueManagerBean parent;

    public QueueBean() {
        super();
        name = new SimpleStringProperty();
    }

    public void initFromRealQueue(Queue queue) {
        this.queue = Objects.requireNonNull(queue);
        this.name.set(Objects.requireNonNull(queue.getName()));
    }

    public void setParent(QueueManagerBean parent) {
        this.parent = Objects.requireNonNull(parent);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String info() {
        return String.format("Cap [%d/%d], In/Out [%d, %d]",
                queue.getDepth(),
                queue.getMaxDepth(),
                queue.getInputCount(),
                queue.getOutputCount()
        );
    }

    public int getDepth() {
        return queue.getDepth();
    }

    public int getInputCount() {
        return queue.getInputCount();
    }

    public int getOutputCount() {
        return queue.getOutputCount();
    }

    public int getMaxDepth() {
        return queue.getMaxDepth();
    }
}
