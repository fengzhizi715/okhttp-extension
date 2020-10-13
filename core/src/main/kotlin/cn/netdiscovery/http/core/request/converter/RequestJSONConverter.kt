package cn.netdiscovery.http.core.request.converter

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.converter.RequestJsonConverter
 * @author: Tony Shen
 * @date: 2020-10-08 01:11
 * @version: V1.0 <描述当前版本功能>
 */
interface RequestJSONConverter {

    fun convert(model: Any): String
}
