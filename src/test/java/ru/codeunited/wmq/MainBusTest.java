package ru.codeunited.wmq;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.bus.MainBus;
import ru.codeunited.wmq.bus.QMCommunicationEvent;
import ru.codeunited.wmq.bus.QMConnectedEvent;
import ru.codeunited.wmq.bus.QMDisconnectedEvent;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.handler.NestedHandlerException;
import ru.codeunited.wmq.messaging.MessagingModule;

import javax.inject.Provider;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 18.05.15.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class, MessagingModule.class})
public class MainBusTest {

    @Inject @MainBus
    private EventBus eventBus;

    @Inject @MainBus
    private Provider<EventBus> eventBusProvider;

    @javax.inject.Inject
    private ExecutionPlanBuilder executionPlanBuilder;

    @Test @ContextInjection
    public void eventBusNotNull() {
        assertThat("Main bus is null", eventBus, notNullValue());
        assertThat("Main bus provider is null", eventBusProvider, notNullValue());
        assertThat("Main bus provider value is null", eventBusProvider.get(), notNullValue());
    }

    @Test @ContextInjection
    public void setEventBusSameInstanece() {
        assertThat("Main event bus has different instances", eventBus, sameInstance(eventBusProvider.get()));
    }

    @Test @ContextInjection(cli = "--qmanager DEFQM --srcq RFH.QTEST.QGENERAL1 --stream --limit -1")
    public void mainBusSet() throws MissedParameterException {
        CommandChain chain = executionPlanBuilder.buildChain();
        List<Command> commands = chain.getCommandChain();
        for(Command command : commands) {
            assertThat(command, instanceOf(AbstractCommand.class));
            assertThat(String.format("Event bus for command %s not setup", command.getClass().getName()),((AbstractCommand)command).getEventBus(), notNullValue());
        }
    }

    static class ConnectHandler {

        private boolean connected = false;

        @Subscribe @AllowConcurrentEvents public void onEvent(QMConnectedEvent event) {
            connected = true;
        }

        public boolean isConnected() {
            return connected;
        }
    }

    static class DisconnectHandler {
        private boolean disconnected = false;

        @Subscribe @AllowConcurrentEvents public void onEvent(QMDisconnectedEvent event) {
            disconnected = true;
        }

        public boolean isDisconnected() {
            return disconnected;
        }
    }

    static class CommunicationHandler {
        private final CountDownLatch latch;
        private final AtomicInteger changeCounter = new AtomicInteger(0);

        public CommunicationHandler(CountDownLatch latch) {
            this.latch = latch;
        }

        @Subscribe @AllowConcurrentEvents public void onEvent(QMCommunicationEvent event) {
            changeCounter.incrementAndGet();
            latch.countDown();
        }

        private int getChangeCounter() {
            return changeCounter.get();
        }
    }

    @Test(timeout = 5000L) @ContextInjection(cli = "-Q DEFQM -c JVM.DEF.SVRCONN --srcq RFH.QTEST.QGENERAL1 --stream")
    public void handleConnectDisconnectEvents() throws MissedParameterException, IncompatibleOptionsException, NestedHandlerException, CommandGeneralException, InterruptedException {

        CountDownLatch latch = new CountDownLatch(2);

        ConnectHandler connectHandler = new ConnectHandler();
        DisconnectHandler disconnectHandler = new DisconnectHandler();
        CommunicationHandler communicationHandler = new CommunicationHandler(latch);

        eventBus.register(connectHandler);
        eventBus.register(disconnectHandler);
        eventBus.register(communicationHandler);

        CommandChain chain = executionPlanBuilder.buildChain();
        chain.execute();

        assertThat("Connection event not handled", connectHandler.isConnected(), is(true));
        assertThat("Disconnect event not handled", disconnectHandler.isDisconnected(), is(true));

        latch.await(1, TimeUnit.SECONDS);
        assertThat("Communication change counter doesn't work properly", communicationHandler.getChangeCounter(), is(2));
    }

}
