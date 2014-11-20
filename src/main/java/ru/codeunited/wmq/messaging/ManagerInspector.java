package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.messaging.pcf.Queue;

import java.io.IOException;
import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
public interface ManagerInspector {

    List<Queue> listLocalQueues() throws MQException, IOException;

    List<Queue> selectLocalQueues(String filter) throws MQException, IOException;

}