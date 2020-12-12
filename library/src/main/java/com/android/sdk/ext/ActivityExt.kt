package com.android.sdk.ext

import android.app.Activity
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import com.android.sdk.wedgit.WebLayout
import com.just.agentweb.*

fun Activity.loadUrl(container: ViewGroup,url: String, onReceiveTitle: (String) -> Unit = {}): AgentWeb{
   return AgentWeb.with(this).setAgentWebParent(container, LinearLayout.LayoutParams(-1, -1))
        .useDefaultIndicator(Color.TRANSPARENT, 1)
        .setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(p0: WebView?, p1: Int) {
                super.onProgressChanged(p0, p1)
            }
        }).interceptUnkownUrl().setWebLayout(WebLayout(this))
        .useMiddlewareWebChrome(object : MiddlewareWebChromeBase() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                onReceiveTitle(title.orEmpty())
            }
        }).useMiddlewareWebClient(object : MiddlewareWebClientBase() {})
        .setAgentWebWebSettings(AgentWebSettingsImpl.getInstance())
        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
        .createAgentWeb().ready().go(url)
}