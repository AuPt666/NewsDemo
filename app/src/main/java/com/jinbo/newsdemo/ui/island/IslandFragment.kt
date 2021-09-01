package com.jinbo.newsdemo.ui.island

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jinbo.newsdemo.R

/***********小岛页面**************/
class IslandFragment: Fragment() {

    private val fragmentList = ArrayList<Fragment>()
    private val tabList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_island, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initList()
        val islandViewPager: ViewPager = requireActivity().findViewById(R.id.island_viewPager)
        val islandFragmentAdapter = IslandFragmentAdapter(childFragmentManager, fragmentList, tabList)
        islandViewPager.adapter = islandFragmentAdapter
        val islandTabLayout: TabLayout= requireActivity().findViewById(R.id.island_tabLayout)
        islandTabLayout.setupWithViewPager(islandViewPager)
    }

    private fun initList(){
        fragmentList.add(IslandFindFragment())
        fragmentList.add(IslandFootPrintFragment())
        tabList.add("发现")
        tabList.add("足迹")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden){//离开最前端页面

        } else{//返回最前端页面
            (fragmentList[1] as IslandFootPrintFragment).refreshHistoryList()
        }
    }
}