package org.saladframework.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * KeyValueParis
 *
 * @author CuiShiFeng
 * @date
 */
public class KeyValueParis {

    private Map<String, Object> kvPairs = new HashMap<String, Object>();

    public KeyValueParis add(String key, Object value) {
        kvPairs.put(key, value);

        return this;
    }

    public Map<String, Object> getKvPairs() {
        return kvPairs;
    }

}
