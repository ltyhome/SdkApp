package com.android.sdk.net

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val map: Map<String, String>?):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().run {
            map?.forEach { (t, u) ->
                addHeader(t,u).build()
            }
            build()
        })
    }
}