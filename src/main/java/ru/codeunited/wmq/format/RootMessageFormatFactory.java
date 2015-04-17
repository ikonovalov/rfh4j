package ru.codeunited.wmq.format;

import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.RFHConstants;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibm.mq.constants.MQConstants.*;

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

    @Inject @PassedFormatter
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
            injectorProvider.get().injectMembers(formatter);
        }
        return formatter;
    }

    protected MessageFormatter loadMessageDrivenFormatter(MQMessage message) throws MQException, IOException {
        MessageFormatter formatter;
        final String format = message.format;
        switch (format) {
            case MQFMT_STRING:
                formatter = new MQFMTStringFormatter();
                break;
            case MQFMT_ADMIN:
                formatter = adminFormatFactory.formatterFor(message);
                break;
            default:
                formatter = new MQFMTStringFormatter();
        }
        return formatter;
    }
}
