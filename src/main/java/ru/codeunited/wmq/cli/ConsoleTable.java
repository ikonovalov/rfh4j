package ru.codeunited.wmq.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.11.14.
 */
public class ConsoleTable {

    private final List<String[]> table = new ArrayList<>();

    private TableColumnName[] head;

    private final ConsoleWriter console;

    protected ConsoleTable(final ConsoleWriter console) {
        this.console = console;
    }

    /**
     * Add new row to table.
     * @param delimited
     * @return
     */
    public ConsoleTable append(String... delimited) {
        table.add(delimited);
        return this;
    }

    public ConsoleTable appendToLastRow(String... args) {
        final int lastElementIndex = table.size() - 1;
        final String[] oldRow = table.get(lastElementIndex);
        String[] resultArray = concatArrays(oldRow, args);
        table.set(lastElementIndex, resultArray);
        return this;
    }

    protected String[] concatArrays(String[] oldRow, String[] args) {
        final int oldRowLen = oldRow.length;
        final String[] result = new String[oldRowLen + args.length];
        System.arraycopy(oldRow, 0, result, 0, oldRowLen);
        System.arraycopy(args, 0, result, oldRowLen, result.length - oldRowLen);
        return result;
    }

    public ConsoleTable head(TableColumnName... head) {
        if (this.head != null)
            throw new IllegalStateException("Head is already set. Use clear() first to reset table.");
        this.head = head;
        return this;
    }

    private String createHorizontalBoarder(int[] columnsMaxWidth) {
        String boarderH = "";
        for (int columnMaxWidth : columnsMaxWidth) {
            char[] clnmHorizont = new char[columnMaxWidth + 2];
            fill(clnmHorizont, '-');
            boarderH += "+" + new String(clnmHorizont);
        }
        boarderH += "+%n";
        return boarderH;
    }

    public void flash() {
        if (table.isEmpty())
            return;

        final List<String[]> printableTable = new ArrayList<>(table.size() + 1);

        // transform head to string array
        if (head != null) {
            printableTable.add(headAsStrings());
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
        final String boarderH = createHorizontalBoarder(columnsMaxWidth); // 2 means  ->|_string_|<-

        // create format
        String rowFormat = "|";
        for (int z = 0; z < maxTabelWidth; z++) {
            rowFormat += " %-" +(columnsMaxWidth[z])+ "s |";
        }
        rowFormat += "%n";

        // write top boarder
        console.writef(boarderH);

        // refill and print
        boolean headerPrinted = false;
        for (ListIterator<String[]> iterator = printableTable.listIterator(); iterator.hasNext(); ) {
            final String[] row =  iterator.next();
            final String[] rowResize = copyOf(row, maxTabelWidth);
            fill(rowResize, row.length, maxTabelWidth, "");
            console.writef(rowFormat, rowResize);
            if (!headerPrinted && head != null && iterator.nextIndex() == 1) {
                console.writef(boarderH);
                headerPrinted = true;
            }
        }

        console.writef(boarderH);

        // flash
        console.flush();

        // clear data
        clear();
    }

    public void clear() {
        table.clear();
        head = null;
    }

    private String[] headAsStrings() {
        final String[] headNames = new String[head.length];
        for (int i = 0; i < head.length; i++) {
            TableColumnName tableColumnName = head[i];
            headNames[i] = tableColumnName.name();
        }
        return headNames;
    }
}
