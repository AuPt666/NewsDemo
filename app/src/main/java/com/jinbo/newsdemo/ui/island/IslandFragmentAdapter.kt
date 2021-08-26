package com.jinbo.newsdemo.ui.island

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**********小岛Fragment适配器**************/
class IslandFragmentAdapter(private val fragmentManager: FragmentManager, private val fragmentList: List<Fragment>, private val tabList: List<String>): FragmentPagerAdapter(fragmentManager) {
    override fun getCount() = fragmentList.size

    override fun getItem(position: Int) = fragmentList[position]

    override fun getPageTitle(position: Int) = tabList[position]
}