package ru.codeunited.wmq.format;

import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.RFHConstants;
import ru.codeunited.wmq.messaging.pcf.ActivityTraceCommand;
import ru.codeunited.wmq.messaging.pcf.ActivityTraceRecord;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
@Singleton
public class MQFMTAdminActivityTraceFormatterCSV extends MQActivityTraceFormatter<String> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final static int BUFFER_2Kb = 2048;

    private ConfigurationParser parser;

    MQFMTAdminActivityTraceFormatterCSV() {

    }

    @Inject @Override
    public void attach(ExecutionContext context) {
        super.attach(context);
        if (context.hasntOption(RFHConstants.OPT_FORMATTER_CONFIG)) {
            throw new CustomFormatterException(
                    String.format("Formatter %s require additional configuration. Option %s didn't passed",
                            MQFMTAdminActivityTraceFormatterCSV.class.getName(), RFHConstants.OPT_FORMATTER_CONFIG)
            );
        }
    }

    @Override
    public String format(final ActivityTraceCommand activityCommand) {
        try {
            if (lock.readLock().tryLock(1, TimeUnit.SECONDS)) {

                final StringBuffer buffer = new StringBuffer(BUFFER_2Kb);

                List<ActivityTraceRecord> records = activityCommand.getRecords();
                for (ActivityTraceRecord record : records) {

                }

                return buffer.toString();
            } else {
                throw new TimeoutException();
            }
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }



}
