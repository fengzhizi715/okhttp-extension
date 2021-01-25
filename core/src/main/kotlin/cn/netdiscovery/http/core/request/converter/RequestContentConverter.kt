package cn.netdiscovery.http.core.request.converter

import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.content.Content

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.converter.RequestContentConverter
 * @author: Tony Shen
 * @date: 2020-10-08 01:09
 * @version: V1.0 将 content 转换成 Params
 */
interface RequestContentConverter {

    fun convert(content: Content): Params
}