package cn.netdiscovery.http.core.converter

import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.Content

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.converter.RequestModelConverter
 * @author: Tony Shen
 * @date: 2020-10-08 01:09
 * @version: V1.0 <描述当前版本功能>
 */
interface RequestModelConverter {

    fun convert(content: Content): Params
}