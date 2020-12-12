package com.android.sdk

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions


/**
 * Glide全局生成GlideApp类
 */
@GlideModule
class IGlideModule: AppGlideModule() {
    override fun applyOptions(@NonNull context: Context, @NonNull builder: GlideBuilder) {
        builder.setLogLevel(Log.ERROR)
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, (1024 * 1024 * 100).toLong()))
        builder.setMemoryCache(LruResourceCache(0))
        builder.setBitmapPool(LruBitmapPool(0))
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888).disallowHardwareConfig())
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}

