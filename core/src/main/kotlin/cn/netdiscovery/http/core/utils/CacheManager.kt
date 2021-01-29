package cn.netdiscovery.http.core.utils

import java.util.concurrent.ConcurrentHashMap


/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.utils.extension.CacheManager
 * @author: Tony Shen
 * @date: 2021-01-25 19:40
 * @version: V1.0 存储 KClass、RequestJSONConverter 键值对或者 KClass、ResponseMapper 键值对
 */
val cache: MutableMap<Any, Any> = ConcurrentHashMap()