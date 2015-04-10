package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.mock.MQMessageMock;

import java.io.IOException;

import static ru.codeunited.wmq.frame.CLITestSupport.prepareCommandLine;
import static ru.codeunited.wmq.RFHConstants.OPT_STREAM;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 31.03.15.
 */
public class CustomFormatter {

    @Test
    public void loadSucessWithBuildin1() throws ParseException, MQException, IOException {
        final CommandLine cl = prepareCommandLine(
                String.format("-Q DEFQM --%s --all --formatter=ru.codeunited.wmq.format.MQFTMAdminCommonFormatter", OPT_STREAM)
        );
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MessageConsoleFormatFactory factory = new MessageConsoleFormatFactory(executionContext);

        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();

        assertThat(factory.formatterFor(message), instanceOf(ru.codeunited.wmq.format.MQFTMAdminCommonFormatter.class));
    }

    @Test
    public void loadSucessWithBuildin2() throws ParseException, MQException, IOException {
        final CommandLine cl = prepareCommandLine(
                String.format("-Q DEFQM --%s --all --formatter=ru.codeunited.wmq.format.MQFMTStringFormatter", OPT_STREAM)
        );
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MessageConsoleFormatFactory factory = new MessageConsoleFormatFactory(executionContext);

        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();

        assertThat(factory.formatterFor(message), instanceOf(MQFMTStringFormatter .class));
    }



    @Test(expected = CustomFormatterException.class)
    public void loadFailureWithBuildin() throws ParseException, MQException, IOException {
        final CommandLine cl = prepareCommandLine(
                String.format("-Q DEFQM --%s --all --formatter=ru.codeunited.wmq.format.MQFTMAdminCommonFormatter1", OPT_STREAM)
        );
        final ExecutionContext executionContext = new CLIExecutionContext(cl);
        final MessageConsoleFormatFactory factory = new MessageConsoleFormatFactory(executionContext);

        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();
        factory.formatterFor(message);
    }

}
