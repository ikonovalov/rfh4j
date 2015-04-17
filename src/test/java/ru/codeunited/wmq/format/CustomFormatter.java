package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.messaging.MessagingModule;
import ru.codeunited.wmq.mock.MQMessageMock;

import javax.inject.Inject;
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
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class, FormatterModule.class, MessagingModule.class})
public class CustomFormatter {

    @Inject @RootFormatFactory
    protected  FormatterFactory factory;

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all --formatter=ru.codeunited.wmq.format.MQFMTAdminCommonFormatter")
    public void loadSucessWithBuildin$MQFMTAdminCommonFormatter() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();
        assertThat(factory.formatterFor(message), instanceOf(MQFMTAdminCommonFormatter.class));
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all --formatter=ru.codeunited.wmq.format.MQFMTStringFormatter")
    public void loadSucessWithBuildin$MQFMTStringFormatter() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();
        assertThat(factory.formatterFor(message), instanceOf(MQFMTStringFormatter .class));
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all --formatter=ru.codeunited.wmq.format.MQFMTAdminActivityTraceFormatterDepFin")
    public void loadSucessWithBuildin$MQFMTAdminActivityTraceFormatterDepFin() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();
        assertThat(factory.formatterFor(message), instanceOf(MQFMTAdminActivityTraceFormatterDepFin.class));
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all --formatter=ru.codeunited.wmq.format.MQFMTAdminActivityTraceFormatter")
    public void loadSucessWithBuildin$MQFMTAdminActivityTraceFormatter() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();
        assertThat(factory.formatterFor(message), instanceOf(MQFMTAdminActivityTraceFormatter.class));
    }

    @Test(expected = CustomFormatterException.class)
    @ContextInjection(cli = "-Q DEFQM --stream --all --formatter=ru.codeunited.wmq.format.MQFMTStringFormatterBad")
    public void loadFailureWithBuildin() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();
        factory.formatterFor(message);
    }

}
