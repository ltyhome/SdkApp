package com.android.sdk.core

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import com.android.sdk.R

abstract class IDialog(context: Context, theme:Int): Dialog(context,theme) {
    constructor(context: Context):this(context, R.style.dialogStyle)
    @JvmOverloads
    fun setWindow(
        gravity: Int,
        width: Int,
        height: Int,
        cancel: Boolean = true
    ) {
        setCancelable(cancel)
        setCanceledOnTouchOutside(cancel)
        window?.setLayout(width,height)
        window?.setGravity(gravity)
    }

    abstract fun getLayoutId():Int
    abstract fun initDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initDialog()
    }

    fun backNoDismiss() {
        setOnKeyListener{ _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
    }
}