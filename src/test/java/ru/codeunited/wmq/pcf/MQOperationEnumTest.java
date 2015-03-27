package ru.codeunited.wmq.pcf;

import org.junit.Test;
import ru.codeunited.wmq.messaging.pcf.MQXFOperations;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.03.15.
 */
public class MQOperationEnumTest {

    @Test
    public void lookupCycling() {
        for (int z = 1; z <=35; z++) {
            MQXFOperations operations = MQXFOperations.lookup(z);
            assertThat(
                    String.format("MQOperation code lookup failed. Query for %d but got %d", z, operations.code()),
                            z, equalTo(operations.code())
            );
        }
    }

    @Test
    public void lookupByConstants() {
        assertThat(MQXFOperations.lookup(1), is(MQXFOperations.MQXF_INIT));
        assertThat(MQXFOperations.lookup(-1), is(MQXFOperations.MQXF_UNKNOWN));
        assertThat(MQXFOperations.lookup(36), is(MQXFOperations.MQXF_UNKNOWN));
        assertThat(MQXFOperations.lookup(10), is(MQXFOperations.MQXF_GET));
        assertThat(MQXFOperations.lookup(35), is(MQXFOperations.MQXF_AXUNREG));
    }

    @Test(expected = NullPointerException.class)
    public void lookupNullAutoBoxed() {
        final Integer nulled = null;
        //noinspection ConstantConditions
        assertThat(MQXFOperations.lookup(nulled), is(MQXFOperations.MQXF_UNKNOWN));
    }
}
