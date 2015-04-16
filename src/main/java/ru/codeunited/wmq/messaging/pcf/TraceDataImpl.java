package ru.codeunited.wmq.messaging.pcf;

import com.google.common.base.Optional;
import com.ibm.mq.headers.MQHeader;
import com.ibm.mq.headers.MQHeaderIterator;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

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
}
