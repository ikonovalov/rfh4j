package ru.codeunited.wmq.messaging.pcf;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 20.11.14.
 */
public class Queue {

    private final String name;

    private int depth = -1;

    private int maxDepth = -1;

    private int inputCount = 0;

    private int outputCount = 0;

    public Queue(String name) {
        this.name = name.trim();
    }

    public int getDepth() {
        return depth;
    }

    public int getInputCount() {
        return inputCount;
    }

    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public void setOutputCount(int outputCount) {
        this.outputCount = outputCount;
    }

    public String getName() {
        return name;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Queue queue = (Queue) o;

        if (!name.equals(queue.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Queue{" +
                "name='" + name + '\'' +
                ", depth=" + depth +
                ", maxDepth=" + maxDepth +
                '}';
    }
}
