import cn.netdiscovery.http.core.converter.RequestJsonConverter

/**
 *
 * @FileName:
 *          .RequestBody
 * @author: Tony Shen
 * @date: 2020-10-12 23:22
 * @version: V1.0 <描述当前版本功能>
 */
data class RequestBody(
        var a:Int = 0,
        var b:String = "test",
        var c:Double = 0.0
)

class RequestBodyConverter: RequestJsonConverter {

    override fun convert(model: Any): String {
        return gson.toJson(model)
    }
}