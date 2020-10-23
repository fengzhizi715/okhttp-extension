package cn.netdiscovery.http.core.request.converter

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.converter.RequestJSONConverter
 * @author: Tony Shen
 * @date: 2020-10-08 01:11
 * @version: V1.0  将 request body 中的 json 内容转换成 String 字符串
 */
interface RequestJSONConverter {

    fun convert(model: Any): String
}
