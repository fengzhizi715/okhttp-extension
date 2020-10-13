package cn.netdiscovery.http.core.config

import cn.netdiscovery.http.core.*
import cn.netdiscovery.http.core.request.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.config.Config
 * @author: Tony Shen
 * @date: 2020-10-09 12:12
 * @version: V1.0 <描述当前版本功能>
 */
val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

val ua = "User-Agent"

val resolvers: Map<String, requestBlock>  = mapOf(
        "queries" to ::processQuery,
        "path"    to ::processPath,
        "body"    to ::processBody,
        "headers" to ::processHeaders,
        "json"    to ::processJson
)