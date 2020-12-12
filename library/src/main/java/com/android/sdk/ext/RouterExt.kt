package com.android.sdk.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.sdk.core.IActivity
import com.android.sdk.core.IFragment
import com.android.sdk.data.ErrorToast
import com.android.sdk.exception.handleException
import com.github.florent37.inlineactivityresult.InlineActivityResult.startForResult
import com.github.florent37.inlineactivityresult.Result
import com.github.florent37.inlineactivityresult.callbacks.ActivityResultListener

inline fun <reified T> Activity.navigation() {
    startActivity(Intent(this,T::class.java))
}

inline fun <reified T> Fragment.navigation() {
    startActivity(Intent(context,T::class.java))
}

inline fun <reified T> Activity.navigation(key:String, value:Any) {
   startActivity(Intent(this,T::class.java).putExtra(key,value))
}

inline fun <reified T> Fragment.navigation(key:String, value:Any) {
    startActivity(Intent(context,T::class.java).putExtra(key,value))
}

inline fun <reified T> Activity.navigation(key:String, value:Any,flags:Int) {
    startActivity(Intent(this,T::class.java).putExtra(key,value).addFlags(flags))
}

inline fun <reified T> Fragment.navigation(key:String, value:Any,flags:Int) {
    startActivity(Intent(context,T::class.java).putExtra(key,value).addFlags(flags))
}

inline fun <reified T> IActivity.navigationForResult(noinline onSuccess:(data:Intent?)->Unit){
    startForResult(this,Intent(this,T::class.java),IResult(this,onSuccess))
}

inline fun <reified T> IActivity.navigationForResult(key:String, value:Any,noinline onSuccess:(data:Intent?)->Unit){
    startForResult(this,Intent(this,T::class.java).putExtra(key,value),IResult(this,onSuccess))
}

inline fun <reified T> IFragment.navigationForResult(noinline onSuccess:(data:Intent?)->Unit){
    startForResult(this,Intent(context,T::class.java),IResult(mActivity,onSuccess))
}

inline fun <reified T> IFragment.navigationForResult(key:String, value:Any, noinline onSuccess:(data:Intent?)->Unit){
    startForResult(this,Intent(context,T::class.java).putExtra(key,value),IResult(mActivity,onSuccess))
}

class IResult(var context: Context,var onSuccess:(data:Intent?)->Unit):ActivityResultListener{
    override fun onSuccess(result: Result?) {
        onSuccess.invoke(result?.data)
    }
    override fun onFailed(result: Result?) {
        result?.cause?.let {
            context.toast(ErrorToast(it.handleException().errMsg))
        }
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Intent.putExtra(key:String,value:T):Intent{
    when(value){
        is Byte -> putExtra(key,value)
        is Boolean -> putExtra(key,value)
        is Serializable -> putExtra(key,value)
        is Double -> putExtra(key,value)
        is String -> putExtra(key,value)
        is Bundle -> putExtra(key,value)
        is CharSequence -> putExtra(key,value)
        is Short -> putExtra(key,value)
        is Parcelable -> putExtra(key,value)
        is Long -> putExtra(key,value)
        is Int -> putExtra(key,value)
        is Float -> putExtra(key,value)
        is Char -> putExtra(key,value)
        is Array<*> -> putExtra(key,value)
        is ArrayList<*> -> {
            when(value.first()){
                is String -> putStringArrayListExtra(key,value as java.util.ArrayList<String>)
                is Int -> putIntegerArrayListExtra(key,value as java.util.ArrayList<Int>)
                is CharSequence -> putCharSequenceArrayListExtra(key,value as java.util.ArrayList<CharSequence>)
                is Parcelable -> putParcelableArrayListExtra(key,value as java.util.ArrayList<out Parcelable>)
            }
        }
    }
    return this
}