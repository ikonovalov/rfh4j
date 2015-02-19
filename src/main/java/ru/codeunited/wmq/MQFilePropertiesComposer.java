package ru.codeunited.wmq;

import com.ibm.mq.constants.CMQC;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import static ru.codeunited.wmq.RFHConstants.OPT_CONFIG;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 20.02.15.
 */
public class MQFilePropertiesComposer extends MQPropertiesComposer {

    private static final Logger LOG = Logger.getLogger(MQFilePropertiesComposer.class.getName());

    public MQFilePropertiesComposer(ExecutionContext context) {
        super(context);
    }

    protected final Properties getConfigFileProperties() {
        String configPath = null;
        if (context.hasOption(OPT_CONFIG)) {
            configPath = context.getOption(OPT_CONFIG);
        } else if (isDefaultConfigAvailable()) {
            configPath = DEFAULT_CONFIG_PATH;
        }
        return loadFromFile(configPath);
    }

    public static boolean isDefaultConfigAvailable() {
        return new File(DEFAULT_CONFIG_PATH).exists();
    }

    private static Properties loadFromFile(String filePath) {
        final Properties fileProperties = new Properties();
        if (filePath == null)
            return fileProperties;

        try (final FileInputStream propertiesStream = new FileInputStream(filePath)) {
            fileProperties.load(propertiesStream);

            /* fix port issue (String -> Integer) */
            fileProperties.put(CMQC.PORT_PROPERTY, Integer.valueOf(fileProperties.getProperty(CMQC.PORT_PROPERTY)));
        } catch (IOException e) {
            LOG.severe("config parameter is passed but we got error [" + e.getMessage() + "]");
        }
        return fileProperties;
    }

    @Override
    public Properties compose() {
        final Properties properties = super.compose();
        properties.putAll(getConfigFileProperties());
        return checkCompatibility(properties);
    }
}
