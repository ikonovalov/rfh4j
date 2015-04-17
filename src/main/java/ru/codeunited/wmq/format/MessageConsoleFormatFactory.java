package ru.codeunited.wmq.format;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.RFHConstants;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

import static com.ibm.mq.constants.MQConstants.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
@Singleton
public class MessageConsoleFormatFactory implements FormatterFactory {

    private ExecutionContext context;

    @Inject @AdminMessageFormatFactory
    private FormatterFactory adminFormatFactory;


    @Inject
    public MessageConsoleFormatFactory(ExecutionContext context) {
        this.context = context;
    }

    private boolean requeireSpecFromatter() {
        return context.hasOption(RFHConstants.OPT_FORMATTER);
    }

    protected static Class loadFormatterClass(String formatterClass) throws ClassNotFoundException {
        return Class.forName(formatterClass);
    }

    @Override
    public MessageFormatter formatterFor(MQMessage message) throws MQException, IOException {
        MessageFormatter formatter;

        if (requeireSpecFromatter()) {
            final String requiredFormatter = context.getOption(RFHConstants.OPT_FORMATTER);
            try {
                Class formatterClass = loadFormatterClass(requiredFormatter);
                formatter = (MessageFormatter) formatterClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new CustomFormatterException("Required formatter [" + requiredFormatter + "] not found", e);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CustomFormatterException("Required formatter [" + requiredFormatter + "] can't be instanced. " + e.getMessage(), e);
            }
        } else { /* automatic mode */
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
        }
        formatter.attach(context);
        return formatter;
    }

}
