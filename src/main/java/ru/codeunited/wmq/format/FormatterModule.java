package ru.codeunited.wmq.format;

import com.google.inject.*;
import com.google.inject.name.Names;
import static com.ibm.mq.constants.MQConstants.*;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.RFHConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 16.04.15.
 */
public class FormatterModule extends AbstractModule {

    protected static final Pattern FORMATTER_OPT_PATTERN = Pattern.compile("^([\\w\\d.]+)(\\[[\\w.;]+\\])?$");

    @Override
    protected void configure() {

        // formatter factories
        bind(FormatterFactory.class).to(Key.get(FormatterFactory.class, RootFormatFactory.class));
        bind(FormatterFactory.class).annotatedWith(RootFormatFactory.class).to(RootMessageFormatFactory.class);
        bind(FormatterFactory.class).annotatedWith(AdminMessageFormatFactory.class).to(MQFMTAdminFormatFactory.class);

        // annotated formatter
        bind(MessageFormatter.class).annotatedWith(MQFMTNone.class).to(MQFMTStringFormatter.class);
        bind(MessageFormatter.class).annotatedWith(MQFMTString.class).to(MQFMTStringFormatter.class);
        bind(MessageFormatter.class).annotatedWith(MQFMTAdmin.class).to(MQFMTAdminCommonFormatter.class);
        bind(MessageFormatter.class).annotatedWith(MQFMTDead.class).to(MQFMTStringFormatter.class);
        bind(MessageFormatter.class).annotatedWith(MQCMDActivityTrace.class).to(MQFMTAdminActivityTraceFormatter.class);

        // named formatter
        bind(MessageFormatter.class).annotatedWith(Names.named(MQFMT_NONE)).to(Key.get(MessageFormatter.class, MQFMTNone.class));
        bind(MessageFormatter.class).annotatedWith(Names.named(MQFMT_STRING)).to(Key.get(MessageFormatter.class, MQFMTString.class));
        bind(MessageFormatter.class).annotatedWith(Names.named(MQFMT_ADMIN)).to(Key.get(MessageFormatter.class, MQFMTAdmin.class));
        bind(MessageFormatter.class).annotatedWith(Names.named(MQFMT_DEAD_LETTER_HEADER)).to(Key.get(MessageFormatter.class, MQFMTDead.class));

        bind(MessageFormatter.class).annotatedWith(Names.named(str(MQCMD_ACTIVITY_TRACE))).to(Key.get(MessageFormatter.class, MQCMDActivityTrace.class));

    }

    @Provides @PassedFormatter @Singleton /* use only with provider */
    public MessageFormatter passedFormatter(ExecutionContext context, Injector injector) {
        String className = matchedGroup(context, 1);
        try {
            Class formatterClass = Class.forName(className);
            MessageFormatter formatter = (MessageFormatter) formatterClass.newInstance();
            injector.injectMembers(formatter);
            return formatter;
        } catch (CustomFormatterException cfe) {
            throw cfe;
        } catch (Exception  e) {
            throw new CustomFormatterException(String.format("Can't load class [%s]", className, e));
        }
    }

    @Provides @PassedFormatterOptions @Singleton
    public String passedFormatterOptions(ExecutionContext context) {
        return matchedGroup(context, 2);
    }

    private static String matchedGroup(ExecutionContext context, int group) {
        if (context.hasOption(RFHConstants.OPT_FORMATTER)) {
            Matcher matcher = FORMATTER_OPT_PATTERN.matcher(context.getOption(RFHConstants.OPT_FORMATTER));
            matcher.matches();
            return matcher.group(group);
        } else {
            return null;
        }
    }

    private String str(int i) {
        return String.valueOf(i);
    }
}
