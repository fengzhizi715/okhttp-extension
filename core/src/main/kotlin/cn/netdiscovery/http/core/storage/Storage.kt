package cn.netdiscovery.http.core.storage

import java.io.File

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.storage.Storage
 * @author: Tony Shen
 * @date: 2020-09-20 01:04
 * @version: V1.0 <描述当前版本功能>
 */
interface Storage {

    val cacheDir: File  // 缓存的目录

    val cookieDir: File // cookie 存放的目录
}