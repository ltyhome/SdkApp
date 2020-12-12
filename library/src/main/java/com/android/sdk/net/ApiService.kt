package com.android.sdk.net

import com.android.sdk.data.string
import com.tencent.mmkv.MMKV
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
    private val sp: MMKV by lazy { MMKV.defaultMMKV() }
    var token by sp.string(key = "token", defaultValue = "")
    inline fun <reified T> create(): T = Retrofit.Builder().baseUrl("https://www.baidu.com/").client(
        OkHttpClient.Builder().connectTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(100L, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
        .addInterceptor(HeaderInterceptor(mapOf("Content-Type" to "application/json","X-Auth-Token" to token)))
        .addInterceptor(CacheInterceptor())
        .addInterceptor(LogInterceptor().apply {
            isDebug = false
            level = Level.BASIC
            type = Platform.INFO
        }).build())
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create(T::class.java)
}