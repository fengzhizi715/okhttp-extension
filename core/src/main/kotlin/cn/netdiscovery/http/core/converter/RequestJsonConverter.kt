package cn.netdiscovery.http.core.converter

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.converter.RequestJsonConverter
 * @author: Tony Shen
 * @date: 2020-10-08 01:11
 * @version: V1.0 <描述当前版本功能>
 */
interface RequestJsonConverter {

    fun convert(model: Any): String
}
