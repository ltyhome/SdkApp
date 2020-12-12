package com.xjh.xinwo.core

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.android.sdk.core.IActivity
import com.android.sdk.ext.toast
import com.android.sdk.net.ApiService
import com.xjh.xinwo.R
import com.xjh.xinwo.ui.XwViewModel

abstract class VMActivity(@LayoutRes contentLayoutId:Int): IActivity(contentLayoutId) {
    val viewModel by lazy { XwViewModel(ApiService.create()) }
    private val loading by lazy {
        AlertDialog.Builder(this, R.style.loadingStyle).setView(
            R.layout.loading).create()
    }

    override fun initActivity() {
        viewModel.defUI.showLoading.observe(this, Observer {
            if(it) loading.show() else loading.dismiss()
        })
        viewModel.defUI.showToast.observe(this, Observer {
            toast(it)
        })
        viewModel.defUI.tokenInvalid.observe(this, Observer {
            if(it){
               //TODO tokenInvalid
            }
        })
    }

    abstract fun requestData()
}