import cn.netdiscovery.http.core.domain.response.ResponseMapper
import okhttp3.Response

/**
 *
 * @FileName:
 *          .Response
 * @author: Tony Shen
 * @date: 2020-10-12 23:47
 * @version: V1.0 <描述当前版本功能>
 */
data class WrapperResponse(
        val code:Int = 200,
        val message:String = "success",
        val data:Any?=null
)

data class ResponseData(val content: String)

class ResponseDataMapper: ResponseMapper<ResponseData> {

    override fun map(response: Response): ResponseData {
        return ResponseData(response.body!!.string())
    }
}