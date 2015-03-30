package ru.codeunited.wmq.pcf;

import org.junit.Test;
import ru.codeunited.wmq.messaging.pcf.MQXFOperation;

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
            MQXFOperation operations = MQXFOperation.lookup(z);
            assertThat(
                    String.format("MQOperation code lookup failed. Query for %d but got %d", z, operations.code()),
                            z, equalTo(operations.code())
            );
        }
    }

    @Test
    public void lookupByConstants() {
        assertThat(MQXFOperation.lookup(1), is(MQXFOperation.MQXF_INIT));
        assertThat(MQXFOperation.lookup(-1), is(MQXFOperation.MQXF_UNKNOWN));
        assertThat(MQXFOperation.lookup(36), is(MQXFOperation.MQXF_UNKNOWN));
        assertThat(MQXFOperation.lookup(10), is(MQXFOperation.MQXF_GET));
        assertThat(MQXFOperation.lookup(35), is(MQXFOperation.MQXF_AXUNREG));
    }

    @Test(expected = NullPointerException.class)
    public void lookupNullAutoBoxed() {
        final Integer nulled = null;
        //noinspection ConstantConditions
        assertThat(MQXFOperation.lookup(nulled), is(MQXFOperation.MQXF_UNKNOWN));
    }
}
