package com.android.sdk.core

import androidx.lifecycle.*
import com.android.sdk.data.ErrorToast
import com.android.sdk.data.IData
import com.android.sdk.data.SingleLiveEvent
import com.android.sdk.data.TypeToast
import com.android.sdk.exception.ResponseThrowable
import com.android.sdk.exception.handleException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class IViewModel:ViewModel(), LifecycleObserver {
     val defUI: UIChange by lazy { UIChange() }
    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }
    /**
     * 用流的方式进行网络请求(外层套launchUI方法)
     */
    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }
    /**
     * 通用处理
     */
    fun <T> Flow<IData<T>>.common(): Flow<IData<T>> {
        return this.flowOn(Dispatchers.IO)
            .onEach { data ->
                if (!data.success)
                    throw ResponseThrowable(data)
            }
    }
    /**
     * 加载 loading
     */
    fun <T> Flow<T>.loading(): Flow<T> {
        return this.onStart {
            defUI.showLoading.postValue(true)
        }.onCompletion {
            defUI.showLoading.postValue(false)
        }
    }
    /**
     * 错误处理
     */
    fun <T> Flow<T>.onError(block: () -> Unit = {}): Flow<T>{
        block()
        return this.catch { e ->
            if(e is ResponseThrowable){
                defUI.showToast.postValue(ErrorToast(e.errMsg))
                if(e.code==402)
                    defUI.tokenInvalid.postValue(true)
            }else
                defUI.showToast.postValue(ErrorToast(e.handleException().errMsg))
            e.printStackTrace()
        }
    }
     class UIChange{
        val showLoading by lazy { SingleLiveEvent<Boolean>() }
        val showToast by lazy { SingleLiveEvent<TypeToast>() }
        val tokenInvalid by lazy { SingleLiveEvent<Boolean>() }
    }
}