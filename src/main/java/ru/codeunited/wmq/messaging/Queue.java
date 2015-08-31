package ru.codeunited.wmq.messaging;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 20.11.14.
 */
public class Queue {

    private final String name;

    private int depth = -1; // STATUS

    private int maxDepth = -1; // DEF

    private int inputCount = 0; // STATUS

    private int outputCount = 0; // STATUS

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

        return name.equals(queue.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Queue{");
        sb.append("name='").append(name).append('\'');
        sb.append(", depth=").append(depth);
        sb.append(", maxDepth=").append(maxDepth);
        sb.append(", inputCount=").append(inputCount);
        sb.append(", outputCount=").append(outputCount);
        sb.append('}');
        return sb.toString();
    }
}
