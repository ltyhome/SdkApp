package com.android.sdk.wedgit

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.android.sdk.R
import com.just.agentweb.AgentWebView
import com.just.agentweb.IWebLayout

class WebLayout(context: Context): IWebLayout<AgentWebView, AgentWebView> {
    @SuppressLint("InflateParams")
    private var webView: AgentWebView =
        LayoutInflater.from(context).inflate(R.layout.web_layout,null) as AgentWebView

    override fun getLayout(): AgentWebView {
        return webView
    }

    override fun getWebView(): AgentWebView? {
        return webView
    }
}