package com.xjh.xinwo.app

import android.app.Application
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.SDKOptions
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.tencent.mmkv.MMKV

class XinWoApp: Application() {
    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> MaterialHeader(context) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context).setDrawableSize(20f) }
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        NIMClient.init(this,null,SDKOptions())
    }
}