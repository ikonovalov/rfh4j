package ru.codeunited.wmq.restapi;

import ru.codeunited.wmq.ExecutionContext;

import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 07.11.14.
 */
public class RestExecutionContext extends ExecutionContext {

    private UriInfo uriInfo;

    private Map<String, String> optionsMap = new HashMap<>();

    public RestExecutionContext() {

    }

    public RestExecutionContext(UriInfo uriInfo) {
        this.uriInfo = uriInfo;

    }

    public RestExecutionContext putOption(String key, String value) {
        optionsMap.put(key, value);
        return this;
    }

    @Override
    public boolean hasOption(String opt) {
        return optionsMap.containsKey(opt);
    }

    @Override
    public boolean hasOption(char opt) {
        return optionsMap.containsKey(opt);
    }

    @Override
    public String getOption(char option) {
        return optionsMap.get(option);
    }

    @Override
    public String getOption(String option) {
        return optionsMap.get(option);
    }
}
