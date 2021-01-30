package cn.netdiscovery.http.core.test.domain

import cn.netdiscovery.http.core.serializer.SerializerManager

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.domain.RequestModel
 * @author: Tony Shen
 * @date: 2020-10-12 23:22
 * @version: V1.0 <描述当前版本功能>
 */
data class RequestModel(
        var a:Int = 0,
        var b:String = "test",
        var c:Double = 0.0
) {
        override fun toString() = SerializerManager.toJson(this)!!
}