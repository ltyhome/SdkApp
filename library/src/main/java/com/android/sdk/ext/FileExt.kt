package com.android.sdk.ext

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.multipartBody(): MultipartBody.Part{
    return MultipartBody.Part.createFormData(
        "file",
        this.name,
        this.asRequestBody("multipart/form-data".toMediaType())
    )
}