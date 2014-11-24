package ru.codeunited.wmq;

import org.junit.Test;
import ru.codeunited.wmq.cli.ConsoleTable;
import ru.codeunited.wmq.cli.ConsoleWriter;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 24.11.14.
 */
public class ConsoleWriterTest {

    @Test
    public void concatArraysTest() {
        class NCW extends ConsoleTable {


            protected NCW(ConsoleWriter console) {
                super(console);
            }

            @Override
            protected String[] concatArrays(String[] oldRow, String[] args) {
                return super.concatArrays(oldRow, args);
            }
        }
        final NCW cw = new NCW(new ConsoleWriter(System.out));
        final String[] array1 = {"A", "B"};
        final String[] array2 = {"C", "D", "E"};
        final String[] concatenatedArray = cw.concatArrays(array1, array2);
        assertThat(Arrays.asList(concatenatedArray), hasItems(new String[]{"A", "B", "C", "D", "E"}));

    }
}
