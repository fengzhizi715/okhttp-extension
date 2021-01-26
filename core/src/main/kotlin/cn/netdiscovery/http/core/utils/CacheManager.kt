package cn.netdiscovery.http.core.utils

import java.util.concurrent.ConcurrentHashMap


/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.utils.extension.CacheManager
 * @author: Tony Shen
 * @date: 2021-01-25 19:40
 * @version: V1.0 <描述当前版本功能>
 */
val cache: MutableMap<Any, Any> = ConcurrentHashMap()