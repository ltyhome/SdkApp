package com.xjh.xinwo.data

import com.flyco.tablayout.listener.CustomTabEntity

data class TabEntity(val title: String, val selectedIcon: Int, val unSelectedIcon: Int):
    CustomTabEntity {
    override fun getTabTitle() = title
    override fun getTabSelectedIcon() = selectedIcon
    override fun getTabUnselectedIcon() = unSelectedIcon
}