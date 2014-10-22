package ru.codeunited.wmq.commands;

import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.cli.ConsoleWriter;

/**
 * Created by ikonovalov on 22.10.14.
 */
public class ExecutionContext {

    /**
     * Default ConsoleWriter use System.out for normal write and for errors.
     */
    private ConsoleWriter consoleWriter = new ConsoleWriter(System.out);

    private MQQueueManager queueManager;

    public void setConsoleWriter(ConsoleWriter consoleWriter) {
        this.consoleWriter = consoleWriter;
    }

    public ConsoleWriter getConsoleWriter() {
        return consoleWriter;
    }

    public void setQueueManager(MQQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    /**
     * Get current active WebSpehere MQ queue manager.
     * @return
     */
    public MQQueueManager getQueueManager() {
        return queueManager;
    }
}
