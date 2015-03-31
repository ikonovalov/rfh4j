package ru.codeunited.wmq.cli;

import org.apache.commons.cli.*;

import java.io.PrintWriter;

import static com.ibm.mq.constants.MQConstants.*;
import static ru.codeunited.wmq.RFHConstants.*;
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
                .withLongOpt(CHANNEL_PROPERTY)
                .withArgName("channel")
                .withDescription("WMQ SVRCON channel name")
                .hasArg(YES)
                .isRequired(NO)
                .create('c');

        final Option transport = OptionBuilder
                .withLongOpt(TRANSPORT_PROPERTY)
                .withArgName("binding|client")
                .withDescription("WMQ transport type")
                .hasArg(YES)
                .isRequired(NO)
                .create();

        final Option queueManager = OptionBuilder
                .withLongOpt(OPT_QMANAGER)
                .withArgName("queue manager")
                .withDescription("WMQ queue manager name")
                .hasArg(YES)
                .isRequired(NO)
                .create('Q');

        final Option host = OptionBuilder
                .withLongOpt(HOST_NAME_PROPERTY)
                .withArgName("hostname or IP")
                .withDescription("WMQ QM host name or ip address (localhost is default).")
                .withType(String.class)
                .hasArg(YES)
                .create('H');

        final Option port = OptionBuilder
                .withLongOpt("port")
                .withArgName("port")
                .withDescription("WMQ QM listener port (1414 is default).")
                .withType(Integer.class)
                .hasArg(YES)
                .create('P');

        final Option user = OptionBuilder
                .withLongOpt(USER_ID_PROPERTY)
                .withArgName("user id")
                .withDescription("WMQ QM user id.")
                .withType(String.class)
                .hasArg(YES)
                .create('u');

        final Option password = OptionBuilder
                .withLongOpt(PASSWORD_PROPERTY)
                .withArgName("user password")
                .withDescription("WMQ QM user password.")
                .withType(String.class)
                .hasArg(YES)
                .create();

        final Option destQueue = OptionBuilder
                .withLongOpt("dstq")
                .withArgName("queue")
                .withDescription("Destination queue")
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

        final Option lsLocalQueues = OptionBuilder
                .withLongOpt("lslq")
                .withArgName("pattern")
                .withDescription("List localqueues with filter. Default value is * (means all).")
                .withType(String.class)
                .hasOptionalArg()
                .create();

        final Option wait = OptionBuilder
                .withLongOpt("wait")
                .withArgName("milliseconds")
                .withDescription("Wait specified amount of time.")
                .withType(Integer.class)
                .hasOptionalArg()
                .create('w');

        final Option config = OptionBuilder
                .withLongOpt(OPT_CONFIG)
                .withArgName("config_file")
                .withDescription("Configuration file for WMQ connection (use it like c,H,P,Q,u)")
                .withType(String.class)
                .hasArg(YES)
                .create();

        final Option verbose = OptionBuilder
                .withLongOpt("verbose")
                .withDescription("Print additional output")
                .hasOptionalArg()
                .create('v');

        // message payload group
        final OptionGroup messagePayload = new OptionGroup();
        final Option filePayload = OptionBuilder
                .withLongOpt(OPT_PAYLOAD)
                .withArgName("file")
                .withDescription("File to send.")
                .hasOptionalArg()
                .create('p');

        final Option textMessage = OptionBuilder
                .withLongOpt("text")
                .withArgName("text")
                .withDescription("Text for message.")
                .hasArg(YES)
                .create('t');

        final Option redirectedStream = OptionBuilder
                .withLongOpt(OPT_STREAM)
                .withArgName("stream")
                .withDescription("Stream for message (std in/out).")
                .hasArg(NO)
                .create('s');

        final Option customHandler = OptionBuilder
                .withLongOpt(OPT_HANDLER)
                .withArgName("handler.class")
                .hasArg(YES)
                .create();

        final Option all = OptionBuilder
                .withLongOpt("all")
                .withDescription("Applicable to GET command")
                .hasArg(NO)
                .create();

        final Option limit = OptionBuilder
                .withLongOpt("limit")
                .withDescription("Limit GET command. This is maximum messages or use negative value for infinity mode")
                .hasArg(YES)
                .create();

        final Option times = OptionBuilder
                .withLongOpt("times")
                .withDescription("Repeat count. Supported for MQPUT now.")
                .hasArg(YES)
                .create();

        final Option formatter = OptionBuilder
                .withLongOpt(OPT_FORMATTER)
                .withDescription("Special formatter class name")
                .hasArg(YES)
                .create();

        messagePayload
                .addOption(textMessage)
                .addOption(filePayload)
                .addOption(redirectedStream)
                .addOption(customHandler);

        final Option help = OptionBuilder.withLongOpt("help").withDescription("Help information").isRequired(false).create('h');

        options
                .addOption(help)
                .addOption(verbose)
                .addOption(host)
                .addOption(port)
                .addOption(queueManager)
                .addOption(channel)
                .addOption(user)
                .addOption(password)
                .addOption(config)
                .addOption(destQueue)
                .addOption(srcQueue)
                .addOption(wait)
                .addOption(lsLocalQueues)
                .addOption(all)
                .addOption(limit)
                .addOption(times)
                .addOption(transport)
                .addOption(formatter)
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

    private static final String exec = "rfh4j.sh";

    public static String commandExamples() {

        int index = 1;
        return  "Usage examples:\n"
                + commandUsage(index++,
                    "Send text message to a queue (host, port, channel are default)",
                        String.format("--dstq RFH.QTEST.QGENERAL1 -t hello!"))
                + commandUsage(index++,
                    "Get message from a queue and print to console",
                        String.format("--srcq RFH.QTEST.QGENERAL1 --%s", OPT_STREAM))
                + commandUsage(index++,
                    "Get message from a queue with timeout and put to file with default name",
                        String.format("--srcq RFH.QTEST.QGENERAL1 --wait 5000 --%s /tmp/", OPT_PAYLOAD))
                + commandUsage(index++,
                    "List all local queues",
                        String.format(" --lslq"))
                + commandUsage(index,
                    "List all local queues with filter",
                        String.format("--lslq MYQ*"));
    }

    private static String commandUsage(int index, String description, String example) {
        return index + ") " + description + '\n'
                + '\t' + exec + " " + example + '\n';
    }

}
