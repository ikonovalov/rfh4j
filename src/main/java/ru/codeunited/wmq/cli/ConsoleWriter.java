package ru.codeunited.wmq.cli;

import com.ibm.mq.MQMessage;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.10.14.
 */
public class ConsoleWriter implements Closeable {

    private final PrintWriter normalWriter;

    private final PrintWriter errorWriter;

    private static final char NL = '\n'; // next line

    private static final char TAB = '\t';

    private static final String BORDER = "<--------------PAYLOAD-BOARDER-------------------->";

    private List<String[]> table = new ArrayList<>();

    private TableColumnName[] head;

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

    /**
     * Order of elements: OPERATION_NAME, QMANAGER_NAME, [Q_NAME], [MESSAGE_ID]
     * @param delimited
     * @return
     */
    public ConsoleWriter table(String... delimited) {
        table.add(delimited);
        /*for (int z = 0; z < delimited.length; z++) {
            printf("%-20s", delimited[z]);
            if (z < delimited.length)
                write(TAB);
        }
        end();*/
        return this;
    }

    public ConsoleWriter tableAppendToLastRow(String...args) {
        final int lastElementIndex = table.size() - 1;
        final String[] oldRow = table.get(lastElementIndex);
        String[] resultArray = concatArrays(oldRow, args);
        table.set(lastElementIndex, resultArray);
        return this;
    }

    protected String[] concatArrays(String[] oldRow, String[] args) {
        final int oldRowLen = oldRow.length;
        final String[] result = new String[oldRowLen + args.length];
        for (int z = 0; z < oldRowLen; z++) {
            result[z] = oldRow[z];
        }
        for (int z = oldRowLen; z < result.length; z++) {
            result[z] = args[z - oldRowLen];
        }
        return result;
    }

    public ConsoleWriter head(TableColumnName... head) {
        this.head = head;
        return this;
    }

    public ConsoleWriter printTable() {
        final List<String[]> printableTable = new ArrayList<>(table.size() + 1);

        // transform head to string array
        if (head != null) {
            String[] headNames = new String[head.length];
            for (int i = 0; i < head.length; i++) {
                TableColumnName tableColumnName = head[i];
                headNames[i] = tableColumnName.name();
            }
            printableTable.add(headNames);
        }

        // move all records to dedicated table
        printableTable.addAll(table);

        // determinate max width of a table
        int maxTabelWidth = 0;
        for (String[] row : printableTable) {
            if (row.length > maxTabelWidth)
                maxTabelWidth = row.length;
        }

        // arrange column width for max
        final int[] columnsMaxWidth = new int[maxTabelWidth];
        for (String[] row : printableTable) {
            for (int i = 0; i < row.length; i++) {
                String s = row[i];
                if (s.length() > columnsMaxWidth[i])
                    columnsMaxWidth[i] = s.length();
            }
        }

        // create horizontal boarder
        final String boarderH = createHorizontalBoarder(columnsMaxWidth, 2); // 2 means  ->|_string_|<-

        // print header
        //writef("+-----------------+------+%n");
        //writef("| Column name     | ID   |%n");
        //writef("+-----------------+------+%n");

        // create format
        String leftAlignFormat = "|";
        for (int z = 0; z < maxTabelWidth; z++) {
            leftAlignFormat += " %-" +(columnsMaxWidth[z])+ "s |";
        }
        leftAlignFormat += "%n";

        // write top boarder
        writef(boarderH);

        // refill and print
        boolean headerPrinted = false;
        for (ListIterator<String[]> iterator = printableTable.listIterator(); iterator.hasNext(); ) {
            final String[] row =  iterator.next();
            final String[] rowResize = copyOf(row, maxTabelWidth);
            fill(rowResize, row.length, maxTabelWidth, "");
            writef(leftAlignFormat, rowResize);
            if (!headerPrinted && head != null && iterator.nextIndex() == 1) {
                writef(boarderH);
                headerPrinted = true;
            }
        }

        writef(boarderH);

        // clear data
        table.clear();
        head = null;
        end();
        return this;
    }

    private String createHorizontalBoarder(int[] columnsMaxWidth, int additionalWidth) {
        String boarderH = "";
        for (int columnMaxWidth : columnsMaxWidth) {
            char[] clnmHorizont = new char[columnMaxWidth + additionalWidth];
            fill(clnmHorizont, '-');
            boarderH += "+" + new String(clnmHorizont);
        }
        boarderH += "+%n";
        return boarderH;
    }

    private void writef(String leftAlignFormat, String...rowResized) {
        write(String.format(leftAlignFormat, rowResized));
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
        end();
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

    @Override
    public void close() throws IOException {
        flush();
        this.errorWriter.close();
        this.normalWriter.close();
    }
}
