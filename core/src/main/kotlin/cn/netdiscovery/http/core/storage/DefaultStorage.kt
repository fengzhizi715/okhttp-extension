package cn.netdiscovery.http.core.storage

import java.io.File

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.storage.DefaultStorage
 * @author: Tony Shen
 * @date: 2020-09-20 01:28
 * @version: V1.0 <描述当前版本功能>
 */
class DefaultStorage (path:String = System.getProperty("user.home")) :Storage {

    override val cacheDir: File by lazy {
        val dir = File(path, ".cookie")
        if (!dir.exists())
            dir.mkdirs()
        dir
    }

    override val cookieDir: File by lazy {
        val dir = File(path, ".cache")
        if (!dir.exists())
            dir.mkdirs()
        dir
    }

}