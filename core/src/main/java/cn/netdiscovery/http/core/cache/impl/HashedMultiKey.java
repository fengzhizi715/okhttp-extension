package cn.netdiscovery.http.core.cache.impl;

import cn.netdiscovery.http.core.cache.MultiKeyEntry;

import java.io.Serializable;

/**
 * @FileName: cn.netdiscovery.http.core.cache.impl.HashedMultiKey
 * @author: Tony Shen
 * @date: 2021-01-25 17:16
 * @version: V1.0 <描述当前版本功能>
 */
public class HashedMultiKey<T> implements MultiKeyEntry<T> {

    private Serializable[] keys;
    private T value;

    public HashedMultiKey(Serializable[] keys) {
        this.keys = keys;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T t) {
        this.value = t;
    }

    public Serializable[] getKeys() {
        return keys;
    }
}
