package com.android.sdk.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.android.sdk.GlideApp
import com.android.sdk.R
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.ImageViewState
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import java.io.File


class GlideEngine private constructor(): ImageEngine {
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
      GlideApp.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade(
          DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build())).into(imageView)
    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: OnImageCompleteCallback?
    ) {
        GlideApp.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade(
            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build())).into(imageView)
        if(null!=longImageView){
            callback?.onShowLoading()
            longImageView.isZoomEnabled = false
            GlideApp.with(context).asFile().load(url).into(object : SimpleTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    callback?.onHideLoading()
                    longImageView.setImage(ImageSource.uri(resource.absolutePath), ImageViewState(1.0f, PointF(0f, 0f), 0))
                }
            })
        }
    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?
    ) {
        GlideApp.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade(
            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build())).into(imageView)
        if(null!=longImageView){
            longImageView.isZoomEnabled = false
            GlideApp.with(context).asFile().load(url).into(object : SimpleTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    longImageView.setImage(ImageSource.uri(resource.absolutePath), ImageViewState(1.0f, PointF(0f, 0f), 0))
                }
            })
        }
    }

    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        GlideApp.with(context).asGif().load(url).into(imageView)
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        GlideApp.with(context).load(url).override(200,200).centerCrop().apply(
            RequestOptions().placeholder(R.drawable.picture_image_placeholder)
        ).transition(DrawableTransitionOptions.withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build())).into(imageView)
    }

    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        GlideApp.with(context).asBitmap().load(url).override(180,180).centerCrop().sizeMultiplier(0.5f)
            .apply(RequestOptions().placeholder(R.drawable.picture_image_placeholder)) .transition(
                BitmapTransitionOptions.withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
            .into(object :BitmapImageViewTarget(imageView){
                override fun setResource(resource: Bitmap?) {
                    val drawable = RoundedBitmapDrawableFactory.create(context.resources,resource)
                    drawable.cornerRadius = 8f
                    imageView.setImageDrawable(drawable)
                }
            })
    }
    companion object{
        private var instance: GlideEngine? = null
        fun createGlideEngine(): GlideEngine? {
            if (null == instance) {
                synchronized(GlideEngine::class.java) {
                    if (null == instance) {
                        instance = GlideEngine()
                    }
                }
            }
            return instance
        }
    }
}