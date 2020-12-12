package com.android.sdk.ext

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.android.sdk.R
import com.android.sdk.data.ErrorToast
import com.android.sdk.data.RemindToast
import com.android.sdk.data.SuccessToast
import com.android.sdk.data.TypeToast
import com.android.sdk.wedgit.IToast

private fun Context.layoutToast(@LayoutRes layoutRes: Int, init: View.() -> Unit, toastInit: IToast.() -> IToast = { this }) {
    val view = LayoutInflater.from(this).inflate(layoutRes, null)
    view.init()
    IToast(this, view).gravity(Gravity.CENTER).toastInit().show()
}

fun Context.toast(msg: TypeToast?){
    msg?.let {
        layoutToast(R.layout.toast_layout, {
            val image = ((this as ViewGroup).getChildAt(0) as ImageView)
            when (it) {
                is SuccessToast -> image.load(R.mipmap.success_tisi)
                is RemindToast -> image.load(R.mipmap.remind_tisi)
                is ErrorToast -> image.load(R.mipmap.error_tisi)
            }
            (this.getChildAt(1) as TextView).text = it.msg
        })
    }
}