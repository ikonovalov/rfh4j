package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;

import java.io.IOException;

/**
 * Formatter should be stateless and thread-safe!
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public interface MessageFormatter<T> {

    T format(MQMessage message) throws IOException, MQException;

}
