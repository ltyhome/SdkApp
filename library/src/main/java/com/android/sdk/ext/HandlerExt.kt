package com.android.sdk.ext

import android.os.Handler
import android.os.Looper

private val handler = Handler(Looper.getMainLooper())

fun <T> T.ktxRunOnUi(block: T.() -> Unit) {
    handler.post {
        block()
    }
}

fun <T> T.ktxRunOnUiDelay(delayMillis: Long, block: T.() -> Unit) {
    handler.postDelayed({
        block()
    }, delayMillis)
}