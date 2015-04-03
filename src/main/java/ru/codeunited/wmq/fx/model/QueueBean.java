package ru.codeunited.wmq.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.messaging.pcf.Queue;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.04.15.
 */
public class QueueBean {

    private final StringProperty name;

    private final Queue queue;

    private final QueueManagerBean parent;

    public QueueBean(QueueManagerBean parent, Queue queue) {
        this.name = new SimpleStringProperty(queue.getName());
        this.queue = queue;
        this.parent = parent;
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
