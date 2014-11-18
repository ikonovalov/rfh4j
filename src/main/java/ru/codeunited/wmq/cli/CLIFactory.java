package ru.codeunited.wmq.cli;

import org.apache.commons.cli.*;

import java.io.PrintWriter;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 21.10.14.
 */
public class CLIFactory {

    private static final boolean YES = true;

    private static final boolean NO = false;

    private CLIFactory() {

    }

    /**
     * Create CLI options parameters.
     *
     * @return set of options.
     */
    public static Options createOptions() {
        final Options options = new Options();

        final Option channel = OptionBuilder
                .withLongOpt("channel")
                .withArgName("channel")
                .withDescription("WMQ SVRCON channel name")
                .hasArg(YES)
                .isRequired(NO)
                .create('c');

        final Option queueManager = OptionBuilder
                .withArgName("qmanager")
                .withDescription("WMQ queue manager name")
                .withLongOpt("qmanager")
                .hasArg(YES)
                .isRequired(NO)
                .create('Q');

        final Option host = OptionBuilder
                .withArgName("hostname or IP")
                .withDescription("WMQ QM host name or ip address (localhost is default).")
                .withLongOpt("host")
                .withType(String.class)
                .hasArg(YES)
                .create('H');

        final Option port = OptionBuilder
                .withArgName("port")
                .withDescription("WMQ QM listener port (1414 is default).")
                .withLongOpt("port")
                .withType(Integer.class)
                .hasArg(YES)
                .create('P');

        final Option user = OptionBuilder
                .withArgName("user")
                .withDescription("WMQ QM user.")
                .withLongOpt("user")
                .withType(String.class)
                .hasArg(YES)
                .create('u');

        final Option destQueue = OptionBuilder
                .withArgName("queue")
                .withDescription("Destination queue")
                .withLongOpt("dstq")
                .withType(String.class)
                .hasArg(YES)
                .create();

        final Option srcQueue = OptionBuilder
                .withLongOpt("srcq")
                .withArgName("queue")
                .withDescription("Source queue")
                .withType(String.class)
                .hasArg(YES)
                .create();

        final Option wait = OptionBuilder
                .withLongOpt("wait")
                .withArgName("milliseconds")
                .withDescription("Wait specified amount of time.")
                .withType(Integer.class)
                .hasOptionalArg()
                .create('w');

        final Option config = OptionBuilder
                .withArgName("config_file")
                .withDescription("Configuration file for WMQ connection (use it like c,H,P,Q,u)")
                .withLongOpt("config")
                .withType(String.class)
                .hasArg(YES)
                .create();

        // message payload group
        final OptionGroup messagePayload = new OptionGroup();
        final Option filePayload = OptionBuilder
                .withArgName("file")
                .withDescription("File to send.")
                .withLongOpt("payload")
                .hasArg(YES)
                .create('p');

        final Option textMessage = OptionBuilder
                .withArgName("text")
                .withDescription("Text for message.")
                .withLongOpt("text")
                .hasArg(YES)
                .create('t');

        final Option redirectedStream = OptionBuilder
                .withArgName("stream")
                .withDescription("Stream for message (std in/out).")
                .withLongOpt("stream")
                .hasArg(NO)
                .create('s');

        messagePayload.addOption(textMessage).addOption(filePayload).addOption(redirectedStream);

        Option help = OptionBuilder.withLongOpt("help").withDescription("Help information").isRequired(false).create('h');

        options
                .addOption(help)
                .addOption(host)
                .addOption(port)
                .addOption(queueManager)
                .addOption(channel)
                .addOption(user)
                .addOption(config)
                .addOption(destQueue)
                .addOption(srcQueue)
                .addOption(wait)
                .addOptionGroup(messagePayload);

        return options;
    }

    /**
     * Create command line parser.
     *
     * @return command line parser implementation.
     */
    public static CommandLineParser createParser() {
        return new PosixParser();
    }

    /**
     * Show help information.
     */
    public static void showHelp() {
        final PrintWriter systemOut = new PrintWriter(System.out);
        showHelp(systemOut);
        systemOut.flush();
    }

    /**
     * Show help information.
     */
    public static void showHelp(PrintWriter printWriter) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                printWriter,
                300,
                "rfh4j", /** header **/
                "Option description",
                createOptions(),
                5,
                5,
                commandExamples(),
                true);
    }

    public static String commandExamples() {
        return  "Usage examples:\n"
                + "1) Send text message to queue (host, port, channel are default)\n"
                + "rfh4j.sh -Q DEFQM --dstq RFH.QTEST.QGENERAL1 -t hello!";
    }

}
