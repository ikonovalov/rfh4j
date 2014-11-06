package ru.codeunited.wmq.commands;

import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.cli.ConsoleWriter;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public abstract class ExecutionContext {

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

    public abstract boolean hasOption(String opt);

    public abstract boolean hasOption(char opt);

    public abstract boolean hasAnyOption(char... opts);

    /**
     * Get single character parameter argument.
     *
     * @param option single character option.
     * @return String value of option if passed, null otherwise.
     */
    public abstract String getOption(char option);

    /**
     * Get value of long named parameter argument.
     *
     * @param option long option name.
     * @return String value of option if passed, null otherwise.
     */
    public abstract String getOption(String option);
}
