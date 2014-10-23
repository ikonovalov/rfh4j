package ru.codeunited.wmq;

import org.apache.commons.cli.Options;
import org.junit.Test;
import ru.codeunited.wmq.cli.CLIFactory;
import static org.junit.Assert.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class CLIFactoryTest {

    @Test
    public void optionsTest() {
        final Options options = CLIFactory.createOptions();
        assertTrue("CLIfacory produce 0 options", options.getOptions().size() > 0);
    }

    @Test
    public void printHelp() {
        // not real test but should run without exception
        CLIFactory.showHelp();
    }
}
