package com.android.sdk.net

import com.android.sdk.exception.ERROR
import com.android.sdk.exception.ResponseThrowable
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

/**
 * @author ltyhome
 * @date 2020/6/1-15:56
 * @email ltyhome@yahoo.com.hk
 * @description
 */
class CacheInterceptor(private val isCache:Boolean = false,private val connected:Boolean = true): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response = chain.proceed(request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build())
        try {
            if(isCache){
                if(!connected)
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
                val originalResponse = chain.proceed(request)
                response = if(connected){  // 有网络时 设置缓存为默认值
                    originalResponse.newBuilder().header("Cache-Control", request.cacheControl.toString()).removeHeader("pragma").build()
                }else // 无网络时 设置超时为1周
                    originalResponse.newBuilder().removeHeader("pragma")
                        .header("Cache-Control","public, only-if-cached, max-stale=${60 * 60 * 24 * 7}")
                        .build()
            }
            if(!response.isSuccessful){
                val code = response.code
                if (code in 500..599)
                    throw ResponseThrowable(ERROR.SERVER_ERROR)
                else
                    throw ResponseThrowable(ERROR.HTTP_ERROR)
            }
            return response
        }catch (e:Exception){
            e.printStackTrace()
            when {
                e.message?.contains("502") == true -> ResponseThrowable(ERROR.SERVER_ERROR,e)
                e is UnknownHostException -> throw ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
                else -> throw ResponseThrowable(ERROR.UNKNOWN,e)
            }
        }
        return response
    }
}