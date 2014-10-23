package ru.codeunited.wmq;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class CreateMQMessageTest {

    @Test
    public void createMessage() {
        assertTrue("Can't create message. NULL", MessageTools.createMessage(1208) != null);
        assertTrue("Wrong charSet", MessageTools.createMessage(1208).characterSet == 1208);
    }

    @Test
    public void createUTFMessage() {
        assertTrue("Created message with wrong charSet", MessageTools.createUTFMessage().characterSet == 1208);
    }
}
