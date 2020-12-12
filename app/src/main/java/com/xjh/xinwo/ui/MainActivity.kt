package com.xjh.xinwo.ui

import android.widget.Toast
import com.android.sdk.core.IActivity
import com.flyco.tablayout.listener.CustomTabEntity
import com.xjh.xinwo.R
import com.xjh.xinwo.data.TabEntity
import com.xjh.xinwo.ui.gchat.GChatFragment
import com.xjh.xinwo.ui.schat.SChatFragment
import com.xjh.xinwo.ui.svideo.SVideoFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity:IActivity(R.layout.activity_main) {
    private var firstTime: Long = 0

    override fun initActivity() {
        bottomLayout.setTabData(arrayListOf<CustomTabEntity>(TabEntity("",R.mipmap.schat_sel,R.mipmap.schat_sel),
            TabEntity("",R.mipmap.svideo_nor,R.mipmap.svideo_nor),TabEntity("",R.mipmap.gchat_nor,R.mipmap.gchat_nor)),
            this@MainActivity, R.id.contentLayout, arrayListOf(SChatFragment(), SVideoFragment(), GChatFragment()))
        val sa = arrayOf(12,24,35,26,121)
        var temp: Int
        for (i in sa.indices) {
            for (j in 0 until sa.size - i-1) {
                if (sa[j] > sa[j + 1]) {
                    temp = sa[j + 1]
                    sa[j + 1] = sa[j]
                    sa[j] = temp
                }
            }
        }
    }


    override fun onBackPressed() {
        val secondTime = System.currentTimeMillis()
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this,"再按一次程序切到后台", Toast.LENGTH_SHORT).show()
            firstTime = secondTime
        } else
           moveTaskToBack(true)
    }
}