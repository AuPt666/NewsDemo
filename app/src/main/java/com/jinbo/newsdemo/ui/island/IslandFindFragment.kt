package com.jinbo.newsdemo.ui.island

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
import com.jinbo.newsdemo.ui.home.HomeViewModel


class IslandFindFragment: Fragment() {
    private val islandViewModel by lazy { ViewModelProvider(this).get(IslandViewModel::class.java) }
    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    private lateinit var islandFindAdapter: IslandFindAdapter
    private lateinit var islandFootPrintAdapter: IslandFootPrintAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_island, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //上半部横向滑动的RecyclerView
        val crosswiseLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        val islandFindCrosswiseRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_find_crosswiseRecyclerView)
        homeViewModel.getNews("头条")
        islandFindCrosswiseRecyclerView.layoutManager = crosswiseLayoutManager
        islandFootPrintAdapter = IslandFootPrintAdapter(this, homeViewModel.newsList)
        islandFindCrosswiseRecyclerView.adapter = islandFootPrintAdapter

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
                islandViewModel.islandList.addAll(island)
                islandFindAdapter.notifyDataSetChanged()
            } else {
                android.widget.Toast.makeText(activity,
                    "没有对应分类的新闻，可尝试输入头条,财经,体育,娱乐,军事,教育,科技,NBA,股票,星座,女性,健康,育儿",
                    android.widget.Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}