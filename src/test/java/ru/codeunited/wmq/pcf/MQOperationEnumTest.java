package ru.codeunited.wmq.pcf;

import org.junit.Test;
import ru.codeunited.wmq.messaging.pcf.MQXFOperation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static ru.codeunited.wmq.messaging.pcf.MQXFOperation.*;
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
        assertThat(lookup(1), is(MQXF_INIT));
        assertThat(lookup(-1), is(MQXF_UNKNOWN));
        assertThat(lookup(36), is(MQXF_UNKNOWN));
        assertThat(lookup(10), is(MQXF_GET));
        assertThat(lookup(35), is(MQXF_AXUNREG));
    }

    @Test(expected = NullPointerException.class)
    public void lookupNullAutoBoxed() {
        final Integer nulled = null;
        //noinspection ConstantConditions
        assertThat(lookup(nulled), is(MQXF_UNKNOWN));
    }

    @Test
    public void anyOfSucess() {
        assertTrue(MQXF_BEGIN.anyOf(MQXF_INIT, MQXF_AXREG, MQXF_BEGIN));
    }

    @Test
    public void anyOfFailure() {
        assertFalse(MQXF_BEGIN.anyOf(MQXF_INIT, MQXF_AXREG, MQXF_BACK));
    }
}
