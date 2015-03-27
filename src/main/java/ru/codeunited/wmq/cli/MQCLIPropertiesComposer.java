package ru.codeunited.wmq.cli;

import com.ibm.mq.constants.CMQC;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.MQFilePropertiesComposer;

import java.util.Properties;
import java.util.logging.Logger;

import static com.ibm.mq.constants.MQConstants.*;
import static ru.codeunited.wmq.RFHConstants.OPT_QMANAGER;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 18.02.15.
 */
public class MQCLIPropertiesComposer extends MQFilePropertiesComposer {

    private static final Logger LOG = Logger.getLogger(MQCLIPropertiesComposer.class.getName());

    public static final String HOST_PROPERTY = "host";

    public static final String USER_PROPERTY = "user";

    public MQCLIPropertiesComposer(ExecutionContext context) {
        super(context);
    }

    Properties passedArgumentsAsProperties() {
        final Properties passedProperties = new Properties();
        if (context.hasOption(CHANNEL_PROPERTY))
            passedProperties.put(CMQC.CHANNEL_PROPERTY, context.getOption(CHANNEL_PROPERTY));
        if (context.hasOption(OPT_QMANAGER))
            passedProperties.put(OPT_QMANAGER, context.getOption(OPT_QMANAGER));
        if (context.hasOption(HOST_PROPERTY))
            passedProperties.put(HOST_NAME_PROPERTY, context.getOption(HOST_PROPERTY));
        if (context.hasOption(PORT_PROPERTY))
            passedProperties.put(PORT_PROPERTY, Integer.valueOf(context.getOption(PORT_PROPERTY)));
        if (context.hasOption(USER_PROPERTY))
            passedProperties.put(USER_ID_PROPERTY, context.getOption(USER_PROPERTY));
        if(context.hasOption(TRANSPORT_PROPERTY)) {
            final String transportAlias = context.getOption(TRANSPORT_PROPERTY).toUpperCase();
            String decodedTransport;
            switch (transportAlias) {
                case "BINDING":
                    decodedTransport = TRANSPORT_MQSERIES_BINDINGS;
                    break;
                case "CLIENT":
                    decodedTransport = TRANSPORT_MQSERIES_CLIENT;
                    break;
                default:
                    LOG.warning(String.format("%s unrecognized with passed value %s. Switched to %s", TRANSPORT_PROPERTY, transportAlias, TRANSPORT_MQSERIES_BINDINGS));
                    decodedTransport = TRANSPORT_MQSERIES_BINDINGS;

            }
            passedProperties.put(TRANSPORT_PROPERTY, decodedTransport);
        }
        return passedProperties;
    }

    @Override
    public Properties compose() {
        final Properties mergedProperties = super.compose();
        // Override config with CLI passed arguments
        mergedProperties.putAll(passedArgumentsAsProperties());
        return checkCompatibility(mergedProperties);
    }
}
