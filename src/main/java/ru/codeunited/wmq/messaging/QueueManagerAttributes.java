package ru.codeunited.wmq.messaging;

import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.*;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 14.04.15.
 */
public final class QueueManagerAttributes implements Serializable {

    private static final int DEF_SIZE = 64;

    private final Map<Pair<Integer, String>, Pair<Object, String>> rawAttributes = new HashMap<>(DEF_SIZE);

    private final Map<Integer, Pair<Integer, String>> codeToKeyMap = new HashMap<>(DEF_SIZE);

    private final Map<String, Pair<Integer, String>> nameToKeyMap = new HashMap<>(DEF_SIZE);

    public QueueManagerAttributes() {

    }

    public int size() {
        return rawAttributes.size();
    }

    public Object put(Pair<Integer, String> keyPair, Pair<Object, String> valuePair) {
        codeToKeyMap.put(keyPair.getLeft(), keyPair);
        nameToKeyMap.put(keyPair.getRight(), keyPair);
        return rawAttributes.put(keyPair, valuePair);
    }

    /**
     * Get parameter value by full pair (Integer and String pair)
     * @param fullKey
     * @return
     */
    private Pair<Object, String> get(Pair<Integer, String> fullKey) {
        return rawAttributes.get(fullKey);
    }

    /**
     * Get parameter value by Integer code.
     * @param partIntKey key parameter code.
     * @return pair of raw value and it string representation.
     */
    public Pair<Object, String> get(Integer partIntKey) {
        return get(codeToKeyMap.get(partIntKey));
    }

    /**
     * Get parameter value by string name.
     * @param partStringKey key parameter name.
     * @return pair of raw value and it string representation.
     */
    public Pair<Object, String> get(String partStringKey) {
        return get(nameToKeyMap.get(partStringKey));
    }

    public String getString(Integer partIntKey) {
        return get(partIntKey).getRight();
    }

    public String getString(String partStringKey) {
        return get(partStringKey).getRight();
    }

    public Object getRaw(Integer partIntKey) {
        return get(partIntKey).getLeft();
    }

    public Object getRaw(String partStringKey) {
        return get(partStringKey).getLeft();
    }

    public Set<Map.Entry<Pair<Integer, String>, Pair<Object, String>>> attributesSet() {
        return Collections.unmodifiableSet(rawAttributes.entrySet());
    }
}
