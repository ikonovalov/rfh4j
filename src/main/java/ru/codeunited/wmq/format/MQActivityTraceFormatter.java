package ru.codeunited.wmq.format;

import com.ibm.mq.MQMessage;
import com.ibm.mq.pcf.PCFMessage;
import ru.codeunited.wmq.messaging.pcf.ActivityTraceCommand;
import ru.codeunited.wmq.messaging.pcf.PCFUtilService;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 31.03.15.
 */
// @Singleton
public abstract class MQActivityTraceFormatter<T> extends MQPCFMessageAbstractFormatter<T> implements ActivityTraceFormatter<T> {

    @Override
    public T format(PCFMessage pcfMessage, MQMessage mqMessage) {
        final ActivityTraceCommand activityCommand = PCFUtilService.activityCommandFor(pcfMessage, mqMessage);
        return format(activityCommand);
    }
}
