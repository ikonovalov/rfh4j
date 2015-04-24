package ru.codeunited.wmq.messaging.pcf;

import com.google.common.base.Optional;
import com.ibm.mq.headers.CCSID;
import com.ibm.mq.headers.MQHeader;
import com.ibm.mq.headers.MQHeaderIterator;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 15.04.15.
 */
public class TraceDataImpl implements TraceData {

    private final MQXFMessageMoveRecord record;

    private List<MQHeader> headerList;

    private Object body;

    private TraceDataImpl(MQXFMessageMoveRecord record) {
        this.record = record;
    }



    public static TraceData create(MQXFMessageMoveRecord record) {
        TraceDataImpl traceData = new TraceDataImpl(record);
        traceData.initialize();
        return traceData;
    }

    private void initialize() {

        final byte[] data = record.getDataRaw();

        if (data == null) // maybe trace data is disabled
            return;

        headerList = new ArrayList<>(2);

        try {
            MQHeaderIterator headerIterator = new MQHeaderIterator(
                    new DataInputStream(
                            new ByteArrayInputStream(data)
                    ),
                    record.getFormat(),
                    record.getEncoding(),
                    record.getCCSID()
            );

            // parse headers
            while (headerIterator.hasNext()) {
                headerList.add(headerIterator.nextHeader());
            }

            // parse body (type is depends of last header format field)
            body = headerIterator.getBody();

        } catch (Exception e) {
            throw new MQHeaderException(
                    String.format("Header parsing error. Passed format=[%s], encoding=[%d], ccsid=[%d]", record.getFormat(), record.getEncoding(), record.getCCSID())
            );
        }


    }

    @Override
    public Optional<List<MQHeader>> getHeaders(){
        return Optional.fromNullable(headerList);
    }

    @Override
    public  <T> Optional<T> getBody(){
        return Optional.fromNullable((T)body);
    }

    @Override
    public Optional<String> getBodyAsString() throws UnsupportedEncodingException {
        Optional<Object> body = getBody();
        if (body.isPresent()) {
            if (body.get() instanceof byte[]) {
                String bodyAsString = new String((byte[])body.get(), CCSID.getCodepage(record.getCCSID()));
                return Optional.of(bodyAsString);
            } else {
                return getBody();
            }
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<byte[]> getBodyAsBytes() {
        Optional<Object> body = getBody();
        if (body.isPresent() ) {
            if (body.get() instanceof String) {
                String bodyString = (String) body.get();
                return Optional.of(bodyString.getBytes());
            } else {
                return getBody();
            }
        }
        else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<String> getBodyFormat() {
        if (getBody().isPresent()) {
            if (getBody().get() instanceof String) {
                return Optional.of(MQFMT_STRING);
            } else {
                return Optional.of(MQFMT_NONE);
            }
        } else {
            return Optional.absent();
        }
    }

    @Override
    public boolean isEmpty() {
        return record.getTraceDataLength() == 0;
    }
}
