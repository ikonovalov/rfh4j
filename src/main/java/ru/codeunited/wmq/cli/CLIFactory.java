package ru.codeunited.wmq.cli;

import org.apache.commons.cli.*;

import java.io.PrintWriter;

/**
 * Created by ikonovalov on 21.10.14.
 */
public class CLIFactory {

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
                .withArgName("channel")
                .withDescription("WMQ SVRCON channel name")
                .withLongOpt("channel")
                .hasArg(true)
                .create('c');

        final Option queueManager = OptionBuilder
                .withArgName("qmanager")
                .withDescription("WMQ queue manager name")
                .withLongOpt("qmanager")
                .hasArg(true)
                .create('Q');

        final Option host = OptionBuilder
                .withArgName("hostname or IP")
                .withDescription("WMQ QM host name or ip address. localhost is default")
                .withLongOpt("host")
                .withType(String.class)
                .hasArg(true)
                .create('H');

        final Option port = OptionBuilder
                .withArgName("port")
                .withDescription("WMQ QM listener port. 1414 is default.")
                .withLongOpt("host")
                .withType(Integer.class)
                .hasArg(true)
                .create('P');

        final Option user = OptionBuilder
                .withArgName("user")
                .withDescription("WMQ QM user.")
                .withLongOpt("user")
                .withType(String.class)
                .hasArg(true)
                .create('u');

        final Option config = OptionBuilder
                .withArgName("config_file")
                .withDescription("Configuration file for WMQ connection (use it like c,H,P,Q,u)")
                .withLongOpt("config")
                .withType(String.class)
                .hasArg(true)
                .create();

        Option help = OptionBuilder.withLongOpt("help").withDescription("Help information").isRequired(false).create('h');

        options
                .addOption(help)
                .addOption(host)
                .addOption(port)
                .addOption(queueManager)
                .addOption(channel)
                .addOption(user)
                .addOption(config);

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
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.setDescPadding(5);
        formatter.setLeftPadding(5);
        formatter.printHelp("rfh4j", createOptions(), true);
    }

}
