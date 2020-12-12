package com.android.sdk.utils

abstract class Singleton<T> {
    @Volatile
    private var mInstance: T? = null
    protected abstract fun create(): T
    fun get(): T? {
        synchronized(this) {
            if (mInstance == null)
                mInstance = create()
            return mInstance
        }
    }
}