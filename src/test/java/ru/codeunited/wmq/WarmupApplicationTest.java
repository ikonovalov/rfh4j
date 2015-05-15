package ru.codeunited.wmq;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.ibm.mq.MQException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.QueueingCapability;
import ru.codeunited.wmq.RFH4J;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.format.FormatterModule;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.handler.HandlerModule;
import ru.codeunited.wmq.messaging.ManagerInspector;
import ru.codeunited.wmq.messaging.MessagingModule;
import ru.codeunited.wmq.messaging.Queue;
import ru.codeunited.wmq.messaging.impl.ManagerInspectorImpl;

import javax.inject.Inject;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.04.15.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class, MessagingModule.class, FormatterModule.class, HandlerModule.class})
public class WarmupApplicationTest extends QueueingCapability {

    private final static String QUEUE = "RFH.QTEST.QGENERAL1";

    @Inject
    private ExecutionContext conetext;


    @Before
    @After
    public void cleanUp() throws Exception {
        cleanupQueue(QUEUE);
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN")
    public void putNmessage() throws Exception {
        final int N = 31;
        RFH4J application = new RFH4J();
        application.start(
                Lists.newArrayList(Splitter.on(' ').split("-Q DEFQM -c JVM.DEF.SVRCONN --dstq RFH.QTEST.QGENERAL1 -t BOOM --times " + N))
                        .toArray(new String[0])
        );

        communication(new QueueWork() {
            @Override
            public void work(ExecutionContext context) throws Exception {
                try (ManagerInspector inspector = new ManagerInspectorImpl(context.getLink())) {
                    Queue qGeneral = inspector.selectLocalQueues("RFH.QTEST.QGENERAL1").get(0);
                    assertThat("Queue is not loaded", qGeneral.getDepth(), is(N));
                }
            }
        });

    }
}
