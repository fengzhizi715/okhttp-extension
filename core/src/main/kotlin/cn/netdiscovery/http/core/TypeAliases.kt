package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.RequestMethodModel
import okhttp3.Request
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.TypeAliases
 * @author: Tony Shen
 * @date: 2020-10-10 13:18
 * @version: V1.0 存储 core 模块下所使用的别名
 */
typealias RequestBlock = (Params, RequestMethodModel) -> RequestMethodModel

// a request interceptor
typealias RequestProcessor = (HttpClient, Request.Builder) -> Request.Builder

// a response interceptor
typealias ResponseProcessor = (Response) -> Unit