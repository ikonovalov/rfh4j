package ru.codeunited.wmq.messaging;

import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.QueueingCapability;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 13.04.15.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class})
public class MessagingProducer2Test extends QueueingCapability {



    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void doIt() {

    }
}
