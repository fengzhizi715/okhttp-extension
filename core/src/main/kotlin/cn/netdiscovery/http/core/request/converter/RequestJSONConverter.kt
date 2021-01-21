package cn.netdiscovery.http.core.request.converter

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.converter.RequestJSONConverter
 * @author: Tony Shen
 * @date: 2020-10-08 01:11
 * @version: V1.0  将 request body 中的对象转换成 json 格式的字符串
 */
interface RequestJSONConverter {

    fun convert(model: Any): String
}
