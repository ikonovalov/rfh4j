package ru.codeunited.wmq;

import ru.codeunited.wmq.commands.CommandChain;
import ru.codeunited.wmq.commands.MissedParameterException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 27.10.14.
 */
public interface ExecutionPlanBuilder {

    public CommandChain buildChain() throws MissedParameterException;
}
