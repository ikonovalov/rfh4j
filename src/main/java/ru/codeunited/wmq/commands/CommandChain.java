package ru.codeunited.wmq.commands;

import java.util.List;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 10.04.15.
 */
public interface CommandChain extends Command {

    CommandChain addCommand(Command command);

    CommandChain addCommand(int index, Command command);

    CommandChain addAfter(Command newCommand, Command afterThat);

    List<Command> getCommandChain();
}
