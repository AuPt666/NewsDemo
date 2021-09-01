package com.jinbo.newsdemo.ui.island

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.model.IslandResponse
import com.jinbo.newsdemo.ui.home.HomeViewModel

/***********小岛发现页面**************/
class IslandFindFragment: Fragment() {
    private val islandViewModel by lazy { ViewModelProvider(this).get(IslandViewModel::class.java) }
    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    private lateinit var islandFindAdapter: IslandFindAdapter
    private lateinit var islandFindImageAdapter: IslandFindImageAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_island_find, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //上半部横向滑动的RecyclerView
        val crosswiseLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        val islandFindCrosswiseRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_find_crosswiseRecyclerView)
        islandFindCrosswiseRecyclerView.layoutManager = crosswiseLayoutManager
        homeViewModel.getNews("娱乐")
        islandFindImageAdapter = IslandFindImageAdapter(this, homeViewModel.newsList)
        islandFindCrosswiseRecyclerView.adapter = islandFindImageAdapter

        //下半部纵向滑动的RecyclerView
        val lengthwaysLayoutManager = LinearLayoutManager(activity)
        val islandFindLengthwaysRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_find_lengthwaysRecyclerView)
        islandViewModel.getIsland("rand")
        islandFindLengthwaysRecyclerView.layoutManager = lengthwaysLayoutManager
        islandFindAdapter = IslandFindAdapter(this, islandViewModel.islandList)
        islandFindLengthwaysRecyclerView.adapter = islandFindAdapter

        //下拉刷新
        val islandFindLengthwaysSwipeRefreshLayout: SwipeRefreshLayout = requireActivity().findViewById(R.id.island_find_lengthwaysSwipeRefresh)
        islandFindLengthwaysSwipeRefreshLayout.setOnRefreshListener {
            islandViewModel.getIsland("rand")
            islandFindLengthwaysSwipeRefreshLayout.isRefreshing = true
        }

        //观察数据变化将其加入List
        islandViewModel.islandLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { result ->
            val island = result.getOrNull()
            if (island != null) {
                val size = if(islandViewModel.islandList.size > 30) 30 else islandViewModel.islandList.size
                val temporalList = ArrayList<IslandResponse.Detail>()
                temporalList.addAll(island)
                for (i in 0 until size){
                    temporalList.add(islandViewModel.islandList[i])
                }
                islandViewModel.islandList.clear()
                islandViewModel.islandList.addAll(island)
                islandFindAdapter.notifyDataSetChanged()
            } else {
                android.widget.Toast.makeText(activity,
                    "未获取新闻",
                    android.widget.Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            islandFindLengthwaysSwipeRefreshLayout.isRefreshing = false
        })

        homeViewModel.newsLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { result ->
            val news = result.getOrNull()
            if (news != null) {
                homeViewModel.newsList.addAll(news)
                islandFindImageAdapter.notifyDataSetChanged()
            } else {
                android.widget.Toast.makeText(activity,
                    "未获取新闻",
                    android.widget.Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        islandFindAdapter.notifyDataSetChanged()
    }
}