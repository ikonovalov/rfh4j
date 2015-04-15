package ru.codeunited.wmq.messaging.pcf;

import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.MQHeader;
import com.ibm.mq.headers.MQHeaderIterator;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 15.04.15.
 */
public final class TraceDataImpl implements TraceData {

    private final MQXFMessageMoveRecord record;

    private List<MQHeader> headerList = Collections.emptyList();

    private Object body;

    private TraceDataImpl(MQXFMessageMoveRecord record) {
        this.record = record;
    }



    public static TraceData create(MQXFMessageMoveRecord record) {
        TraceData traceData = new TraceDataImpl(record);
        return traceData;
    }

    private boolean isArrangeRequired() {
        return headerList == null || headerList.size() == 0 || body == null;
    }

    private void arrange() throws MQHeaderException {

        if (!isArrangeRequired()) return;

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

            // parse body
            body = headerIterator.getBody();

        } catch (Exception e) {
            throw new MQHeaderException(
                    String.format("Header parsing error. Passed format=[%s], encoding=[%d], ccsid=[%d]", record.getFormat(), record.getEncoding(), record.getCCSID())
            );
        }


    }

    @Override
    public List<MQHeader> getHeaders() throws MQHeaderException {
        arrange();
        return headerList;
    }

    @Override
    public <T> T getBody() throws MQHeaderException {
        arrange();
        return (T) body;
    }
}
