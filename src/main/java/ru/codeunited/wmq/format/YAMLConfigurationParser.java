package ru.codeunited.wmq.format;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.05.15.
 */
public class YAMLConfigurationParser implements ConfigurationParser<Map> {

    private Yaml yaml = new Yaml();

    private Map root = null;

    @Override
    public void load(String str) {
        root = (Map) yaml.load(str);
    }

    @Override
    public void load(InputStream stream) {
        root = (Map) yaml.load(stream);
    }

    @Override
    public Map getRoot() {
        return root;
    }


}
