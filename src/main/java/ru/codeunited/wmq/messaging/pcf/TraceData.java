package ru.codeunited.wmq.messaging.pcf;

import com.google.common.base.Optional;
import com.ibm.mq.headers.MQHeader;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 15.04.15.
 */
public interface TraceData {

    Optional<List<MQHeader>> getHeaders();

    /**
     * Get body of the trace activity record (applicable for MQXF_PUT/MQXF_GET operations)
     * @param <T>
     * @return body of the captured message. Class of the body may be byte[] or String. It depends of MQFMT_NONE or MQFMT_STRING respectively.
     */
    <T> Optional<T> getBody();

    Optional<String> getBodyAsString() throws UnsupportedEncodingException;

    Optional<byte[]> getBodyAsBytes();

    Optional<String> getBodyFormat();

}
