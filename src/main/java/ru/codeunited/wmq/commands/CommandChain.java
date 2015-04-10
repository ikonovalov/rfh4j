package ru.codeunited.wmq.commands;

import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public interface CommandChain extends Command {
    CommandChainImpl addCommand(Command command);

    CommandChainImpl addCommand(int index, Command command);

    CommandChainImpl addAfter(Command newCommand, Command afterThat);

    List<Command> getCommandChain();
}
