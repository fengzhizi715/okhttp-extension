package cn.netdiscovery.http.core.request.converter

import cn.netdiscovery.http.core.converter.ConverterManager

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.converter.GlobalRequestJSONConverter
 * @author: Tony Shen
 * @date: 2020-10-13 15:33
 * @version: V1.0 默认全局的 request json 转换器
 */
class GlobalRequestJSONConverter : RequestJSONConverter {

    override fun convert(model: Any): String {
        return ConverterManager.toJson(model) ?: ""
    }
}