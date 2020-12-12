package com.android.sdk.data


/**
 * @author ltyhome
 * @date 2020/5/26-15:42
 * @email ltyhome@yahoo.com.hk
 * @description 基础返回接口
 */
interface IResponse<T> {
    fun code(): String
    fun message(): String
    fun data(): T?
    fun page(): Page?
    fun timestamp(): Long
    fun isSuccess(): Boolean
    fun isEmpty(): Boolean
    fun hasNext(): Boolean
}
data class Page(var index: Int, var size: Int, var total: Int, var pages: Int)
data class IData<T>(var code:String, var message: String,var data: T?,var timestamp:Long,var page: Page?,var success:Boolean):
    IResponse<T> {
    override fun code() = code
    override fun message() = message
    override fun data() = data
    override fun timestamp() = timestamp
    override fun page() = page
    override fun isSuccess() = success
    override fun isEmpty(): Boolean {
        if(null==data)
            return true
        return when (data) {
            is String -> (data as String).isBlank()
            is List<*> -> (data as List<*>).isNullOrEmpty()
            is Map<*,*> -> (data as Map<*,*>).isNullOrEmpty()
            else -> false
        }
    }
    override fun hasNext(): Boolean {
        if(isEmpty()||null==page)
            return false
        return page!!.index < page!!.pages
    }
}