package cn.netdiscovery.http.core.cache;

import java.io.Serializable;

/**
 * @FileName: cn.netdiscovery.http.core.cache.MultiKeyMap
 * @author: Tony Shen
 * @date: 2021-01-25 17:15
 * @version: V1.0 <描述当前版本功能>
 */
public interface MultiKeyMap<T> {

    MultiKeyEntry<T> get(Serializable... keys);
}
