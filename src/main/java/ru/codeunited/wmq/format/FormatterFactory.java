package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 27.03.15.
 */
public interface FormatterFactory {

    MessageFormatter formatterFor(MQMessage message) throws MQException, IOException;

}
