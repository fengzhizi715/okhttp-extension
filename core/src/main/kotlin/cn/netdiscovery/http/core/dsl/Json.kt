package cn.netdiscovery.http.core.dsl

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.Json
 * @author: Tony Shen
 * @date: 2021/11/16 7:25 下午
 * @version: V1.0 <描述当前版本功能>
 */
class Json {
    private val elements = mutableListOf<Pair<String, String>>()

    fun json(init: Json.() -> Unit) = Json().also(init)

    infix fun String.to(obj: List<*>) {
        val v = obj.joinToString(separator = ",", prefix = "[", postfix = "]") {
            when (it) {
                null -> "null"
                is Number, is Json, is Boolean -> it.toString()
                else -> """"$it""""
            }
        }
        elements += Pair(this, v)
    }

    infix fun String.to(str: String?) {
        elements += if (str == null) Pair(this, "null") else Pair(this, """"$str"""")
    }

    infix fun String.to(num: Number?) {
        elements += Pair(this, num.toString())
    }

    infix fun String.to(json: Json?) {
        elements += Pair(this, json.toString())
    }

    infix fun String.to(bool: Boolean?) {
        elements += Pair(this, bool.toString())
    }

    override fun toString(): String =
        elements.joinToString(separator = ",", prefix = "{", postfix = "}") { (k, v) ->
            """"$k":$v"""
        }
}
