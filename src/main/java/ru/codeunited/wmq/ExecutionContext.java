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

    public boolean hasntOption(String...opts) {
       return !hasAnyOption(opts);
    }

    public boolean hasAnyOption(String...opts) {
        for (String s : opts) {
            if (hasOption(s))
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
     * Return option with default. If option exists in a context it returns value, if value is not present - return default.
     * @param option option key.
     * @param defaultValue default value if option doesn't exists.
     * @return option value or default.
     */
    public String getOption(char option, String defaultValue) {
        final String retValue = getOption(option);
        return retValue == null ? defaultValue : retValue;
    }

    /**
     * Get value of long named parameter argument.
     *
     * @param option long option name.
     * @return String value of option if passed, null otherwise.
     */
    public abstract String getOption(String option);

    /**
     * Return option with default. If option exists in a context it returns value, if value is not present - return default.
     * @param option option key.
     * @param defaultValue default value if option doesn't exists or passed without argument.
     * @return option value or default.
     */
    public String getOption(String option, String defaultValue) {
        final String retValue = getOption(option);
        return retValue == null ? defaultValue : retValue;
    }

    /**
     * Monster construction method.
     * @param option
     * @param defaultValue
     * @return
     */
    public Object getOption(String option, Object defaultValue) {
        final String retValue = getOption(option);
        return retValue == null ? defaultValue : retValue;
    }

    public abstract ExecutionContext putOption(String key, String value);
}
