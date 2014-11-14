package ru.codeunited.wmq;

import org.junit.Test;
import ru.codeunited.wmq.cli.CLIExecutionContext;
import ru.codeunited.wmq.cli.ConsoleWriter;
import ru.codeunited.wmq.restapi.RestExecutionContext;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 15.11.14.
 */
public class ContextTest {

    @Test
    public void shouldHaveDefaultConsoleWriter() {
        final ExecutionContext ctx = new ExecutionContext() {
            @Override
            public boolean hasOption(String opt) {
                return false;
            }

            @Override
            public boolean hasOption(char opt) {
                return false;
            }

            @Override
            public String getOption(char option) {
                return null;
            }

            @Override
            public String getOption(String option) {
                return null;
            }

            @Override
            public ExecutionContext putOption(String key, String value) {
                return null;
            }
        };

        assertThat(ctx.getConsoleWriter(), notNullValue());
    }

    @Test
    public void setConsoleWriterInCLIContext() {
        final ExecutionContext ctx = new CLIExecutionContext(null);
        final ConsoleWriter cw = new ConsoleWriter(System.out);
        validateConsoleWriter(ctx, cw);
    }

    @Test
    public void setConsoleWriterInRestContext() {
        final ExecutionContext ctx = new RestExecutionContext(null);
        final ConsoleWriter cw = new ConsoleWriter(System.err);
        validateConsoleWriter(ctx, cw);
    }

    private void validateConsoleWriter(ExecutionContext ctx, ConsoleWriter cw) {
        ctx.setConsoleWriter(cw);
        assertThat(ctx.getConsoleWriter(), notNullValue());
        assertThat(ctx.getConsoleWriter(), equalTo(cw));
    }
}
