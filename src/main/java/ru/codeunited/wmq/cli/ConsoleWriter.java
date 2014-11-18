package ru.codeunited.wmq.cli;

import com.ibm.mq.MQMessage;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class ConsoleWriter {

    private final PrintWriter normalWriter;

    private final PrintWriter errorWriter;

    private static final char NL = '\n'; // next line

    private static final char TAB = '\t';

    private static final String BORDER = "<--------------PAYLOAD-BOARDER-------------------->";

    public ConsoleWriter(PrintStream printWriter, PrintStream errorWriter) {
        this.errorWriter = new PrintWriter(errorWriter);
        this.normalWriter = new PrintWriter(printWriter);
    }

    /**
     * Use same stream for normal output and errors.
     *
     * @param printWriter
     */
    public ConsoleWriter(PrintStream printWriter) {
        this(printWriter, printWriter);
    }

    public ConsoleWriter head() {
        table("[command]", "[queue manager]", "[queue]", "[messageId]");
        return this;
    }

    public ConsoleWriter table(String... delimited) {
        for (int z = 0; z < delimited.length; z++) {
            printf("%-20s", delimited[z]);
            if (z < delimited.length)
                write(TAB);
        }
        end();
        return this;
    }

    @Deprecated
    public ConsoleWriter table(String string) {
        return table(string.split("|"));
    }

    public ConsoleWriter printf(String format, String string) {
        normalWriter.printf(format, string);
        return this;
    }

    public ConsoleWriter write(String string) {
        normalWriter.write(string);
        return this;
    }

    public ConsoleWriter end() {
        write(NL);
        return this;
    }

    public ConsoleWriter write(char ch) {
        normalWriter.write(ch);
        return this;
    }

    public ConsoleWriter writeln(String string) {
        return write(string).end();
    }

    public ConsoleWriter write(MQMessage message) throws IOException {
        writeln(BORDER);
        writeln(message.readStringOfByteLength(message.getDataLength()));
        writeln(BORDER);
        return this;
    }

    public ConsoleWriter error(String string) {
        errorWriter.write(string);
        return this;
    }

    public ConsoleWriter flush() {
        this.errorWriter.flush();
        this.normalWriter.flush();
        return this;
    }

    public ConsoleWriter errorln(String message) {
        return error(message).end();
    }
}
