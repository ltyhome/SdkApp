package com.android.sdk.exception

import android.util.MalformedJsonException
import com.android.sdk.data.IData
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLException

inline class ERROR(val errorMessage: String){
    companion object{
        val UNKNOWN = ERROR("未知错误")
        val PARSE_ERROR = ERROR("解析错误")
        val NETWORK_ERROR = ERROR("网络错误")
        val HTTP_ERROR = ERROR("协议出错")
        val SSL_ERROR = ERROR("证书出错")
        val SERVER_ERROR = ERROR("服务器出错")
        val TIMEOUT_ERROR = ERROR("连接超时")
    }
}

class ResponseThrowable: Exception {
    var code: Int
    var errMsg: String
    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        code = 10000//通用错误码
        errMsg = error.errorMessage
    }
    constructor(code: Int, message: String, e: Throwable? = null) : super(e) {
        this.code = code
        this.errMsg = message
    }
    constructor(base: IData<*>, e: Throwable? = null) : super(e) {
        this.code = base.code().toInt()
        this.errMsg = base.message()
    }
}


fun Throwable.handleException(): ResponseThrowable {
    return when (this) {
        is HttpException -> ResponseThrowable(ERROR.HTTP_ERROR, this)
        is JSONException, is ParseException, is MalformedJsonException -> ResponseThrowable(
            ERROR.PARSE_ERROR, this
        )
        is ConnectException -> ResponseThrowable(ERROR.NETWORK_ERROR, this)
        is SSLException -> ResponseThrowable(ERROR.SSL_ERROR, this)
        is SocketTimeoutException -> ResponseThrowable(ERROR.TIMEOUT_ERROR, this)
        is UnknownHostException -> ResponseThrowable(ERROR.TIMEOUT_ERROR, this)
        else -> {
            if(!this.message.isNullOrBlank())
                ResponseThrowable(1000, this.message!!, this)
            else
                ResponseThrowable(ERROR.UNKNOWN, this)
        }
    }
}