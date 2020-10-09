package cn.netdiscovery.http.core.annotation

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.annotation.ParamName
 * @author: Tony Shen
 * @date: 2020-10-09 01:49
 * @version: V1.0 <描述当前版本功能>
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class ParamName(val name: String)