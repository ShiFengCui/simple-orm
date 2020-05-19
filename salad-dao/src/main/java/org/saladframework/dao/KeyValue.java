package org.saladframework.dao;

/**
 * KeyValue
 *
 * @author CuiShiFeng
 * @date
 */
public class KeyValue<K, V> {

    private K key;

    private V value;


    public KeyValue(K key, V value) {
        super();
        this.key = key;
        this.value = value;
    }

    public KeyValue() {

    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
