package ru.codeunited.wmq.format;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeunited.wmq.ContextModule;
import ru.codeunited.wmq.commands.CommandsModule;
import ru.codeunited.wmq.frame.ContextInjection;
import ru.codeunited.wmq.frame.GuiceContextTestRunner;
import ru.codeunited.wmq.frame.GuiceModules;
import ru.codeunited.wmq.messaging.MessagingModule;

import javax.inject.Inject;

import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.05.15.
 */
@RunWith(GuiceContextTestRunner.class)
@GuiceModules({ContextModule.class, CommandsModule.class, FormatterModule.class, MessagingModule.class})
public class CSVFormatterTest {

    @Inject
    private Provider<Injector> injectorProvider;

    @Test(expected = CustomFormatterException.class)
    @ContextInjection(cli = "-Q DEFQM --stream --all --formatter=ru.codeunited.wmq.format.MQFMTAdminActivityTraceFormatterCSV")
    public void configNotSpecified() throws Throwable {
        Provider<MessageFormatter> formatterProvider = injectorProvider.get().getProvider(Key.get(MessageFormatter.class, PassedFormatter.class));
        try {
            formatterProvider.get();
        } catch (ProvisionException pe) {
            throw pe.getCause();
        }
    }

    @Test
    @ContextInjection
    public void loadYamlConfig() {
        ConfigurationParser parser = new YAMLConfigurationParser();

        String data = "invoice: 34843\n" +
                "date   : 2001-01-23\n" +
                "bill-to: &id001\n" +
                "    given  : Chris\n" +
                "    family : Dumars\n" +
                "    address:\n" +
                "        lines: |\n" +
                "            458 Walkman Dr.\n" +
                "            Suite #292\n" +
                "        city    : Royal Oak\n" +
                "        state   : MI\n" +
                "        postal  : 48046\n" +
                "ship-to: *id001\n" +
                "product:\n" +
                "    - sku         : BL394D\n" +
                "      quantity    : 4\n" +
                "      description : Basketball\n" +
                "      price       : 450.00\n" +
                "    - sku         : BL4438H\n" +
                "      quantity    : 1\n" +
                "      description : Super Hoop\n" +
                "      price       : 2392.00\n" +
                "tax  : 251.42\n" +
                "total: 4443.52\n" +
                "comments: >\n" +
                "    Late afternoon is best.\n" +
                "    Backup contact is Nancy\n" +
                "    Billsmer @ 338-4338.";

        parser.load(data);
        assertThat(parser.getRoot(), instanceOf(Map.class));
    }

    @Test
    @ContextInjection
    public void loadYamlConfigReal() {
        InputStream is = getClass().getResourceAsStream("/ru/codeunited/wmq/format/config.yaml");
        ConfigurationParser<Map> parser = new YAMLConfigurationParser();
        parser.load(is);
        Map params = parser.getRoot();
        System.out.println(params);
    }

}
