package com.cybersource;

public class KeyValueResponse {
    private String key;
    private Object value;

    public String getKey() {
        return key;
    }

    public KeyValueResponse setKey(String key) {
        this.key = key;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public  KeyValueResponse setValue(Object value) {
        this.value = value;
        return this;
    }
}
