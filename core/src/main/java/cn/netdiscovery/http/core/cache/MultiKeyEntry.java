package cn.netdiscovery.http.core.cache;

/**
 * @FileName: cn.netdiscovery.http.core.cache.MultiKeyEntry
 * @author: Tony Shen
 * @date: 2021-01-25 17:14
 * @version: V1.0 <描述当前版本功能>
 */
public interface MultiKeyEntry<T> {

    T getValue();

    void setValue(T t);
}
