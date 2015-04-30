package ru.codeunited.wmq.format;

import com.google.inject.ProvisionException;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.constants.MQConstants;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.messaging.MessagingModule;
import ru.codeunited.wmq.mock.MQMessageMock;

import javax.inject.Inject;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

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

    @Test(expected = ProvisionException.class) // (expected = CustomFormatterException.class) it was before that we move passed formatter under the guice
    @ContextInjection(cli = "-Q DEFQM --stream --all --formatter=ru.codeunited.wmq.format.MQFMTStringFormatterBad")
    public void loadFailureWithBuildin() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();
        factory.formatterFor(message);
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all --formatter=ru.codeunited.wmq.format.MQFMTAdminActivityTraceFormatter")
    public void loadSucessSameInstancePassed() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();

        MessageFormatter inst1 = factory.formatterFor(message);
        MessageFormatter inst2 = factory.formatterFor(message);
        assertThat(inst1, sameInstance(inst2));
        assertThat(inst1, instanceOf(MQFMTAdminActivityTraceFormatter.class));
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all")
    public void loadSucessSameInstanceMessageDrivenMQFMTAdmin() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTAdminMessage();

        MessageFormatter inst1 = factory.formatterFor(message);
        MessageFormatter inst2 = factory.formatterFor(message);
        assertThat(inst1, sameInstance(inst2));
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all")
    public void loadSucessSameInstanceMessageDrivenMQFMTNone() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTNoneMessage();

        MessageFormatter inst1 = factory.formatterFor(message);
        MessageFormatter inst2 = factory.formatterFor(message);
        assertThat(inst1, sameInstance(inst2));

        // now it is true, may be in future we would use another formatter for binary
        assertThat(inst1, instanceOf(MQFMTStringFormatter.class));
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all")
    public void loadSucessSameInstanceMessageDrivenMQFMTString() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTStringMessage();

        MessageFormatter inst1 = factory.formatterFor(message);
        MessageFormatter inst2 = factory.formatterFor(message);
        assertThat(inst1, sameInstance(inst2));

        assertThat(inst1, instanceOf(MQFMTStringFormatter.class));
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all")
    public void loadSucessSameInstanceMessageDrivenDLHFormat() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.createMQFMTDLHMessage();

        MessageFormatter inst1 = factory.formatterFor(message);
        MessageFormatter inst2 = factory.formatterFor(message);
        assertThat(inst1, sameInstance(inst2));
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --stream --all")
    public void loadSucessSameInstanceMessageDrivenCICS() throws ParseException, MQException, IOException {
        final MQMessage message = MQMessageMock.makeNew(MQConstants.MQFMT_CICS);

        MessageFormatter inst1 = factory.formatterFor(message);
        MessageFormatter inst2 = factory.formatterFor(message);
        assertThat(inst1, sameInstance(inst2));

        // it can be changed in a next versions
        assertThat(inst1, instanceOf(MQFMTStringFormatter.class));
    }

}
