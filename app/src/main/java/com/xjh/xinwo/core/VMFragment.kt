package com.xjh.xinwo.core

import android.Manifest
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.android.sdk.core.IFragment
import com.android.sdk.data.ErrorToast
import com.android.sdk.ext.checkPermissions
import com.android.sdk.ext.toast
import com.android.sdk.net.ApiService
import com.xjh.xinwo.R
import com.xjh.xinwo.ui.XwViewModel

abstract class VMFragment(@LayoutRes contentLayoutId:Int) : IFragment(contentLayoutId){
    private val loading by lazy {
        AlertDialog.Builder(mActivity, R.style.loadingStyle).setView(
            R.layout.loading).create()
    }
    val viewModel by lazy { XwViewModel(ApiService.create()) }
    override fun initFragment() {
        initImmersionBar()
        viewModel.defUI.showLoading.observe(this, Observer {
            if(it) loading.show() else loading.dismiss()
        })
        viewModel.defUI.showToast.observe(this, Observer {
            mActivity.toast(it)
        })
        viewModel.defUI.tokenInvalid.observe(this, Observer {
            if(it){
                //TODO tokenInvalid
            }
        })
    }
    abstract fun requestData()
    fun setTitle(title:String){
        mView.findViewById<TextView>(R.id.toolbar_title)?.let { it.text = title }
        mView.findViewById<ImageView>(R.id.toolbar_back)?.let { it.visibility = View.GONE }
    }
    fun checkCameraPermission(error:String,onGranted:()->Unit){
        mActivity.checkPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,onGranted = onGranted,onDenied = { mActivity.toast(ErrorToast(error)) })
    }
}