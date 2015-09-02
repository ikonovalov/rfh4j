package ru.codeunited.wmq.messaging;

import com.ibm.mq.MQException;
import ru.codeunited.wmq.messaging.pcf.QueueType;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.11.14.
 */
public interface ManagerInspector extends Closeable {

    List<Queue> listLocalQueues() throws MQException, IOException;

    QueueManagerAttributes managerAttributes() throws MQException, IOException;

    /**
     * Appect full queue name, * or "QNAM*" pattern
     * @param queueNameFilter
     * @return
     * @throws MQException
     * @throws IOException
     */
    List<Queue> selectLocalQueues(String queueNameFilter) throws MQException, IOException;

    List<String> inquireQueueNames(String queueNameFilter, QueueType queueType) throws MQException, IOException;

    List<QueueStatus> inquireQueueStatus(String queueNameFilter) throws MQException, IOException;
}
