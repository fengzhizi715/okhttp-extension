package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.RequestMethodModel
import okhttp3.Request

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.TypeAliases
 * @author: Tony Shen
 * @date: 2020-10-10 13:18
 * @version: V1.0 <描述当前版本功能>
 */
typealias requestBlock = (Params, RequestMethodModel) -> RequestMethodModel

typealias RequestProcessor = (HttpClient, Request.Builder) -> Request.Builder