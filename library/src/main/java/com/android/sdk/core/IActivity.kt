package com.android.sdk.core

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.android.sdk.R
import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.ktx.isSupportStatusBarDarkFont

abstract class IActivity(@LayoutRes contentLayoutId:Int): AppCompatActivity(contentLayoutId) {
    abstract fun initActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersionBar{
            transparentStatusBar()
            if(isSupportStatusBarDarkFont)
                statusBarDarkFont(darkBar())
            findViewById<View>(R.id.status_bar_view)?.let { statusBarView(it) }
        }
        initActivity()
    }
    open fun darkBar():Boolean{
        return true
    }
    fun setTitle(title:String){
        findViewById<TextView>(R.id.toolbar_title)?.let { it.text = title }
        findViewById<ImageView>(R.id.toolbar_back)?.let { it.setOnClickListener { finish() } }
    }
}