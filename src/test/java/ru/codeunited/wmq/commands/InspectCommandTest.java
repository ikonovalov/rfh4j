package ru.codeunited.wmq.commands;

import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.*;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.handler.NestedHandlerException;
import ru.codeunited.wmq.messaging.MessagingModule;

import javax.inject.Inject;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class, MessagingModule.class})
public class InspectCommandTest {

    @Inject private ExecutionPlanBuilder executionPlanBuilder;

    @Inject private ExecutionContext context;

    @Inject private MQInspectCommand inspectCommand;

    @Test(expected = IncompatibleOptionsException.class)
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN")
    public void raiseInternalValidationBlow() throws MissedParameterException, IncompatibleOptionsException {
        inspectCommand.validateOptions();
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN --lslq")
    public void raiseInternalValidationPass() throws MissedParameterException, IncompatibleOptionsException {
        inspectCommand.validateOptions();
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN --lslq")
    public void parameterWithoutFilter() {
        assertThat(context.hasOption(RFHConstants.OPT_LIST_QLOCAL), is(true));
        assertThat(context.getOption(RFHConstants.OPT_LIST_QLOCAL), nullValue());
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN --lslq RFH*")
    public void parameterWithFilter() {
        assertThat(context.hasOption(RFHConstants.OPT_LIST_QLOCAL), is(true));
        assertThat(context.getOption(RFHConstants.OPT_LIST_QLOCAL), is("RFH*"));
    }

    @Test(timeout = 1000L)
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN --lslq --transport=binding")
    public void listAll() throws MissedParameterException, IncompatibleOptionsException, NestedHandlerException, CommandGeneralException {
        CommandChain chain = executionPlanBuilder.buildChain();
        ReturnCode rc = chain.execute();
        assertThat(rc, is(ReturnCode.SUCCESS));
    }

    @Test(timeout = 1000L)
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN --lslq RFH*")
    public void listWithFilter() throws MissedParameterException, IncompatibleOptionsException, NestedHandlerException, CommandGeneralException {
        CommandChain chain = executionPlanBuilder.buildChain();
        ReturnCode rc = chain.execute();
        assertThat(rc, is(ReturnCode.SUCCESS));
    }

    @Test(expected = MissedParameterException.class)
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN")
    public void missedParameter() throws MissedParameterException, IncompatibleOptionsException, NestedHandlerException, CommandGeneralException {
        executionPlanBuilder.buildChain();
    }

    @Test
    @ContextInjection(cli = "-Q DEFQM --channel JVM.DEF.SVRCONN --lslq")
    public void exectCommandList() throws MissedParameterException {
        CommandChain chain = executionPlanBuilder.buildChain();
        List<Command> commands = chain.getCommandChain();
        BuildExecutionChainTest.assertThatCommandInstanceOf(commands, MQConnectCommand.class, MQInspectCommand.class, MQDisconnectCommand.class);
    }
}
