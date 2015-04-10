package ru.codeunited.wmq;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.codeunited.wmq.cli.CLIFactory;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static ru.codeunited.wmq.CLITestSupport.prepareCommandLine;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 23.10.14.
 */
public class CLIFactoryTest {

    @Test
    public void optionsTest() {
        final Options options = CLIFactory.createOptions();
        assertTrue("CLI factory produce 0 options", options.getOptions().size() > 0);
    }

    @Test
    public void printHelp() {
        // not real test but should run without exception
        CLIFactory.showHelp();
    }

    @Test
    public void identifyOptions() throws ParseException {
        final CommandLine commandLine = prepareCommandLine("-Q DEFQM -c JVM.DEF.SVRCONN");
        final Option[] options = commandLine.getOptions();
        assertThat(options.length, is(2));
    }
}
