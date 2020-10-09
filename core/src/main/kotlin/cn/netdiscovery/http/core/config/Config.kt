package cn.netdiscovery.http.core.config

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