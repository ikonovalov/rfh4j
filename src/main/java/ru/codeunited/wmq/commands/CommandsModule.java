package ru.codeunited.wmq.commands;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.codeunited.wmq.DefaultExecutionPlanBuilder;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.ExecutionPlanBuilder;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public class CommandsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExecutionPlanBuilder.class).to(DefaultExecutionPlanBuilder.class);

        // commands
        bind(CommandChain.class).to(CommandChainImpl.class);
        bind(Command.class).annotatedWith(ConnectCommand.class).to(MQConnectCommand.class);
        bind(Command.class).annotatedWith(DisconnectCommand.class).to(MQDisconnectCommand.class);
        bind(Command.class).annotatedWith(PutCommand.class).to(MQPutCommand.class);
        bind(Command.class).annotatedWith(GetCommand.class).to(MQGetCommand.class);
        bind(Command.class).annotatedWith(InspectCommand.class).to(MQInspectCommand.class);

    }
}
