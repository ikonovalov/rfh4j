package ru.codeunited.wmq.format;

import com.google.inject.*;
import com.google.inject.name.Names;
import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.RFHConstants;

import javax.inject.Named;
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

        bind(FormatterFactory.class).to(Key.get(FormatterFactory.class, RootFormatFactory.class));
        bind(FormatterFactory.class).annotatedWith(RootFormatFactory.class).to(RootMessageFormatFactory.class);
        bind(FormatterFactory.class).annotatedWith(AdminMessageFormatFactory.class).to(MQFMTAdminFormatFactory.class);

    }

    @Provides @PassedFormatter @Singleton
    public MessageFormatter passedFormatter(ExecutionContext context, Injector injector) {
        String className = mattchedGroup(context, 1);
        try {
            Class formatterClass = Class.forName(className);
            MessageFormatter formatter = (MessageFormatter) formatterClass.newInstance();
            injector.injectMembers(formatter);
            return formatter;
        } catch (Exception  e) {
            throw new CustomFormatterException(e);
        }
    }

    @Provides @PassedFormatterOptions @Singleton
    public String passedFormatterOptions(ExecutionContext context) {
        return mattchedGroup(context, 2);
    }

    private static String mattchedGroup(ExecutionContext context, int group) {
        Matcher matcher = FORMATTER_OPT_PATTERN.matcher(context.getOption(RFHConstants.OPT_FORMATTER));
        matcher.matches();
        return matcher.group(group);
    }
}
