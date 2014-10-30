package ru.codeunited.wmq.cli;

import ru.codeunited.wmq.commands.ReturnCode;

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

    private String NL = "\n"; // next line

    public ConsoleWriter(PrintStream printWriter, PrintStream errorWriter) {
        this.errorWriter = new PrintWriter(errorWriter);
        this.normalWriter = new PrintWriter(printWriter);
    }

    public ConsoleWriter(PrintWriter printWriter, PrintWriter errorWriter) {
        this.normalWriter = printWriter;
        this.errorWriter = errorWriter;
    }

    public String getNextLineMarker() {
        return NL;
    }

    public void setNextLineMarkerL(String NL) {
        this.NL = NL;
    }

    /**
     * Use same stream for normal output and errors.
     * @param printWriter
     */
    public ConsoleWriter(PrintStream printWriter) {
        this(printWriter, printWriter);
    }

    public ConsoleWriter(PrintWriter printWriter) {
        this(printWriter, printWriter);
    }

    public ConsoleWriter write(String string) {
        normalWriter.write(string);
        return this;
    }

    public ConsoleWriter write(char ch) {
        normalWriter.write(ch);
        return this;
    }

    public ConsoleWriter writeln(String string) {
        return write(string).write(NL);
    }

    public ConsoleWriter write(ReturnCode code) {
        return write(code.name());
    }

    public ConsoleWriter writeln(ReturnCode code) {
        return writeln(code.name());
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
        return error(message).write(NL);
    }
}
