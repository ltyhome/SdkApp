package com.android.sdk.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import com.android.sdk.GlideApp
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.IOException
import java.io.InputStream
import kotlin.math.round

fun ImageView.load(res: Int){
    GlideApp.with(this).load(res).into(this)
}


fun ImageView.load(url: String?){
    GlideApp.with(this).load(url).into(this)
}

fun ImageView.load(bitmap: Bitmap?){
    GlideApp.with(this).load(bitmap).into(this)
}

fun ImageView.loadCircle(url: String?){
    GlideApp.with(this).load(url).apply(RequestOptions.bitmapTransform(CircleCrop())).into(this)
}

fun ImageView.loadRound(url: String?,defRadius:Int = 10){
    GlideApp.with(this).load(url).apply(
        RequestOptions.skipMemoryCacheOf(true)
            .priority(Priority.HIGH).transform(RoundedCornersTransformation(defRadius, 0))
    ).into(this)
}

fun ImageView.load(uri: Uri, call: ((Bitmap?) -> Unit)){
    val options = BitmapFactory.Options()
    var input: InputStream? = null
    try {
        input = context.contentResolver.openInputStream(uri)
        options.inJustDecodeBounds = true
        options.inDither = true
        options.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        BitmapFactory.decodeStream(input, null, options)
        val height = options.outHeight
        val width = options.outWidth
        options.inSampleSize = options.calculateInSampleSize(width, height)
        options.inJustDecodeBounds = false
        input = context.contentResolver.openInputStream(uri)
        call.invoke(BitmapFactory.decodeStream(input, null, options))
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            input?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun BitmapFactory.Options.calculateInSampleSize(reqWidth: Int, reqHeight: Int):Int{
    var inSampleSize  = 1
    if(this.outHeight > reqHeight || this.outWidth > reqWidth){
        val heightRatio = round(this.outHeight / reqHeight.toFloat())
        val widthRatio = round(this.outWidth / reqWidth.toFloat())
        inSampleSize = if(heightRatio < widthRatio) heightRatio.toInt() else widthRatio.toInt()
    }
    return inSampleSize
}