package com.android.sdk.data

sealed class TypeToast(var msg: String)
data class SuccessToast(val message: String):TypeToast(message)
data class RemindToast(val message: String):TypeToast(message)
data class ErrorToast(val message: String):TypeToast(message)