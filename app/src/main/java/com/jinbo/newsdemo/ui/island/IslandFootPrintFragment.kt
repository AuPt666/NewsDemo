package com.jinbo.newsdemo.ui.island

import android.os.Bundle
import android.service.autofill.FillEventHistory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.ui.home.HomeViewModel

class IslandFootPrintFragment: Fragment() {

    val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    private lateinit var islandFootPrintAdapterToRecommend: IslandFootPrintAdapter
    private lateinit var islandFootPrintAdapterToHistory: IslandFootPrintAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_island_footprint, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //推荐部分
        val islandFootPrintLengthwaysLayoutManger: LinearLayoutManager = LinearLayoutManager(activity)
        val islandFootprintLengthwaysRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_footprint_lengthwaysRecyclerView)
        homeViewModel.getNews("娱乐")
        islandFootprintLengthwaysRecyclerView.layoutManager = islandFootPrintLengthwaysLayoutManger
        islandFootPrintAdapterToRecommend = IslandFootPrintAdapter(this, homeViewModel.newsList)
        islandFootprintLengthwaysRecyclerView.adapter = islandFootPrintAdapterToRecommend


        //浏览历史部分
        val islandFootprintHistoryLayoutManger: LinearLayoutManager = LinearLayoutManager(activity)
        val islandFootprintHistoryRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_footprint_crosswiseRecyclerView)
        var historyList = homeViewModel.getHistory()
        islandFootprintHistoryRecyclerView.layoutManager = islandFootprintHistoryLayoutManger
        islandFootPrintAdapterToHistory = IslandFootPrintAdapter(this, historyList)


        homeViewModel.newsLiveData.observe(viewLifecycleOwner, { result ->
            val history = result.getOrNull()
            if (history != null && history.size > historyList.size){
                for(i in history.indices)
                homeViewModel.saveHistory(history[i])
                historyList = homeViewModel.getHistory()
                islandFootPrintAdapterToHistory.notifyDataSetChanged()
            }else if(history == null || history.isEmpty()){
                Toast.makeText(activity,"无历史记录", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

    }
}