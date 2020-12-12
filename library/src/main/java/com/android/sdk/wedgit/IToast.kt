package com.android.sdk.wedgit

import android.content.Context
import android.view.View
import android.widget.Toast

class IToast internal constructor(context: Context, view: View) {
    private val toast = Toast(context)

    init {
        toast.view = view
    }

    fun gravity(gravity: Int, xOffset: Int = 0, yOffset: Int = 0): IToast {
        toast.setGravity(gravity, xOffset, yOffset)
        return this
    }

    fun duration(duration: Int): IToast {
        if (duration != 0 && duration != 1) toast.duration = Toast.LENGTH_LONG
        else toast.duration = duration
        return this
    }

    fun show() {
        toast.show()
    }
}