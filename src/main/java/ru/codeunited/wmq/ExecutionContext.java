package ru.codeunited.wmq;

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

    /**
     * Set active queue manager.
     * @param queueManager
     */
    public final void setQueueManager(MQQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    /**
     * Get current active WebSpehere MQ queue manager.
     * @return
     */
    public final MQQueueManager getQueueManager() {
        return queueManager;
    }

    /**
     * Check if context has specified option.
     * @param opt option name (long name)
     * @return
     */
    public abstract boolean hasOption(String opt);

    public abstract boolean hasOption(char opt);

    public boolean hasAnyOption(char...opts) {
        for (char c : opts) {
            if (hasOption(c))
                return true;
        }
        return false;
    }

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

    public abstract ExecutionContext putOption(String key, String value);
}
