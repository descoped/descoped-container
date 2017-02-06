package io.descoped.container.info;

/**
 * Created by oranheim on 04/02/2017.
 */
public class KeyValue {

    private String key;
    private String value;

    public KeyValue(String key) {
        this.key = key;
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
