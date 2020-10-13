package cn.netdiscovery.http.core.request.converter

import cn.netdiscovery.http.core.converter.ConverterManager

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.converter.GlobalRequestJSONConverter
 * @author: Tony Shen
 * @date: 2020-10-13 15:33
 * @version: V1.0 <描述当前版本功能>
 */
class GlobalRequestJSONConverter : RequestJSONConverter {

    override fun convert(model: Any): String {
        return ConverterManager.toJson(model)?:""
    }
}