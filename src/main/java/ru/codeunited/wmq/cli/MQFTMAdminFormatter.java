package ru.codeunited.wmq.cli;

import com.ibm.mq.MQMessage;

import java.io.IOException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
public class MQFTMAdminFormatter implements MessageConsoleFormatter {

    @Override
    public String format(MQMessage message) throws IOException {
        return ">>>";
    }
}
