package com.jinbo.newsdemo.ui.island

import android.os.Bundle
import android.service.autofill.FillEventHistory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.ui.home.HomeViewModel

/***********小岛足迹页面**************/
class IslandFootPrintFragment: Fragment() {

    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    private lateinit var islandFootPrintAdapterToRecommend: IslandFootPrintRecommendAdapter
    private lateinit var islandFootPrintAdapterToHistory: IslandFootPrintAdapter

    var fragmentIsVisible = false
    var historyListSize = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_island_footprint, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //推荐部分
        val islandFootPrintLengthwaysLayoutManger: GridLayoutManager = GridLayoutManager(this.context, 2)
        val islandFootprintLengthwaysRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_footprint_lengthwaysRecyclerView)
        homeViewModel.getNews("科技")
        islandFootprintLengthwaysRecyclerView.layoutManager = islandFootPrintLengthwaysLayoutManger
        islandFootPrintAdapterToRecommend = IslandFootPrintRecommendAdapter(this, homeViewModel.newsList)
        islandFootprintLengthwaysRecyclerView.adapter = islandFootPrintAdapterToRecommend

        //浏览历史部分
        val islandFootprintHistoryLayoutManger: LinearLayoutManager = LinearLayoutManager(activity)
        val islandFootprintHistoryRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_footprint_crosswiseRecyclerView)
        var historyList = homeViewModel.getHistory()
        historyListSize = historyList.size
        islandFootprintHistoryRecyclerView.layoutManager = islandFootprintHistoryLayoutManger
        islandFootPrintAdapterToHistory = IslandFootPrintAdapter(this, historyList)
        islandFootprintHistoryRecyclerView.adapter = islandFootPrintAdapterToHistory

        homeViewModel.newsLiveData.observe(viewLifecycleOwner, { result ->
            val news = result.getOrNull()
            if (news != null){
                homeViewModel.newsList.addAll(news)
                islandFootPrintAdapterToRecommend.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"无推荐内容", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(userVisibleHint){
            fragmentIsVisible = true
            var historyList = homeViewModel.getHistory()
            if (historyList.size > historyListSize){
                islandFootPrintAdapterToRecommend.notifyDataSetChanged()
                historyListSize = historyList.size
            }
        }else{
            fragmentIsVisible = false
        }
    }
}