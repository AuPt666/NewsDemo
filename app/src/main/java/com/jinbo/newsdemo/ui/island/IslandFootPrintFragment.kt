package com.jinbo.newsdemo.ui.island

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinbo.newsdemo.BaseApplication
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.model.NewsResponse
import com.jinbo.newsdemo.ui.home.HomeViewModel
import java.util.*
import kotlin.collections.ArrayList

/***********小岛足迹页面**************/
class IslandFootPrintFragment: Fragment() {

    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    private lateinit var islandFootPrintAdapterToRecommend: IslandFootPrintRecommendAdapter
    private lateinit var islandFootPrintAdapterToHistory: IslandFootPrintAdapter

    private var fragmentIsVisible = false
    private var historyList: List<NewsResponse.Detail>? = ArrayList<NewsResponse.Detail>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_island_footprint, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //推荐部分
        val islandFootPrintLengthwaysLayoutManger = GridLayoutManager(this.context, 2)
        val islandFootprintLengthwaysRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_footprint_lengthwaysRecyclerView)
        homeViewModel.getNews("科技")
        islandFootprintLengthwaysRecyclerView.layoutManager = islandFootPrintLengthwaysLayoutManger
        islandFootPrintAdapterToRecommend = IslandFootPrintRecommendAdapter(this, homeViewModel.newsList)
        islandFootprintLengthwaysRecyclerView.adapter = islandFootPrintAdapterToRecommend

        //浏览历史部分
        val islandFootprintHistoryLayoutManger = LinearLayoutManager(activity)
        val islandFootprintHistoryRecyclerView: RecyclerView = requireActivity().findViewById(R.id.island_footprint_crosswiseRecyclerView)
        islandFootprintHistoryRecyclerView.layoutManager = islandFootprintHistoryLayoutManger
        islandFootPrintAdapterToHistory = IslandFootPrintAdapter(this, historyList!!)
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
            refreshHistoryList()
        }else{
            fragmentIsVisible = false
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        refreshHistoryList()
        Log.e("onHiddenChange", "66666")
    }

    //刷新历史记录
    fun refreshHistoryList(){
        val findHistoryTask = FindHistoryTask()
        findHistoryTask.execute()
    }

    //开启线程读取数据库数据并返回查询结果
    private inner class FindHistoryTask: AsyncTask<Void, Void, List<NewsResponse.Detail>>() {
        override fun doInBackground(vararg params: Void?): List<NewsResponse.Detail> {
            historyList = homeViewModel.getHistory(BaseApplication.context)
            return historyList as List<NewsResponse.Detail>
        }

        //查询完成，刷新浏览历史列表
        override fun onPostExecute(result: List<NewsResponse.Detail>?) {
            super.onPostExecute(result)
            historyList?.let { islandFootPrintAdapterToHistory.setIslandFootPrintMsgList(it) }
            islandFootPrintAdapterToHistory.notifyDataSetChanged()
        }
    }
}
