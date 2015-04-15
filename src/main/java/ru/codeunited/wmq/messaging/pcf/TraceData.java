package ru.codeunited.wmq.messaging.pcf;

import com.ibm.mq.headers.MQHeader;

import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 15.04.15.
 */
public interface TraceData {

    List<MQHeader> getHeaders() throws MQHeaderException;

    <T> T getBody() throws MQHeaderException;
}
