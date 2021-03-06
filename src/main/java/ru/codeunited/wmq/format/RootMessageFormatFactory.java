package ru.codeunited.wmq.format;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.google.inject.spi.Message;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.RFHConstants;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;

import static com.ibm.mq.constants.MQConstants.MQFMT_ADMIN;
import static com.ibm.mq.constants.MQConstants.MQFMT_STRING;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
@Singleton
public class RootMessageFormatFactory implements FormatterFactory {

    private ExecutionContext context;

    @Inject @AdminMessageFormatFactory
    private FormatterFactory adminFormatFactory;

    @Inject
    private Provider<Injector> injectorProvider;

    @Inject @PassedFormatter /* use only as lazy binding */
    private Provider<MessageFormatter> passedFormatterProvider;

    @Inject
    public RootMessageFormatFactory(ExecutionContext context) {
        this.context = context;
    }

    private boolean requeireSpecFromatter() {
        return context.hasOption(RFHConstants.OPT_FORMATTER);
    }

    @Override
    public MessageFormatter formatterFor(MQMessage message) throws MQException, IOException {
        MessageFormatter formatter;
        if (requeireSpecFromatter()) {
            formatter = passedFormatterProvider.get();
        } else { /* automatic mode */
            formatter = loadMessageDrivenFormatter(message);
        }
        return formatter;
    }

    /**
     * Load message formatter according message type. It use "named" binding.
     * @param message
     * @return
     * @throws MQException
     * @throws IOException
     */
    protected MessageFormatter loadMessageDrivenFormatter(MQMessage message) throws MQException, IOException {
        MessageFormatter formatter;
        final String format = message.format;
        switch (format) {
            case MQFMT_ADMIN:
                formatter = adminFormatFactory.formatterFor(message);
                break;
            default:
                try {
                    formatter = injectorProvider.get().getInstance(Key.get(MessageFormatter.class, Names.named(format)));
                } catch (ConfigurationException configException) {
                    /* try to handle unsupported message with default (binary handler) */
                    formatter = injectorProvider.get().getInstance(Key.get(MessageFormatter.class, MQFMTNone.class));
                }
        }

        return formatter;
    }
}
