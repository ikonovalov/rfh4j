package ru.codeunited.wmq;

import org.junit.Test;
import ru.codeunited.wmq.commands.ReturnCode;

import static org.junit.Assert.*;

/**
 * Created by ikonovalov on 23.10.14.
 */
public class CheckReturnCodes {


    @Test
    public void checkCodes() {
        assertTrue(ReturnCode.READY.code() == 10);
        assertTrue(ReturnCode.EXECUTING.code() == 5);
        assertTrue(ReturnCode.SKIPPED.code() == 1);
        assertTrue(ReturnCode.SUCCESS.code() == 0);
        assertTrue(ReturnCode.FAILED.code() == -1);
    }
}
