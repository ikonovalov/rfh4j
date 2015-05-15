package ru.codeunited.wmq.format;

import com.ibm.mq.MQMessage;
import com.ibm.mq.pcf.PCFMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 30.03.15.
 */
public interface PCFMessageFormatter<T> {

    T format(PCFMessage pcfMessage, MQMessage mqMessage);

}
