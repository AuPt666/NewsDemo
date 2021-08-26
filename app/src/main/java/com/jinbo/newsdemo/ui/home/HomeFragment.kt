package com.jinbo.newsdemo.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.databinding.FragmentHomeBinding
import com.jinbo.newsdemo.logic.model.NewsResponse

/***********主页**************/
class HomeFragment : Fragment() {
    val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    private val newChannelArray= arrayOf("头条","财经","体育","娱乐","军事","教育","科技","NBA","股票","星座","女性","健康","育儿")
    private var index = 0

    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManger = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = requireActivity().findViewById(R.id.home_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManger
        //初始化新闻列表
        homeViewModel.getNews("头条")
        homeAdapter = HomeAdapter(this, homeViewModel.newsList)
        recyclerView.adapter = homeAdapter

        //从搜索框获取对应分类的新闻
        val homeEditText: EditText = requireActivity().findViewById(R.id.home_searchEdit)
        homeEditText.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.length >= 2) {
                homeViewModel.getNews(content)
            }
        }


        //下拉刷新
        val homeSwipeRefresh: SwipeRefreshLayout = requireActivity().findViewById(R.id.home_swipeRefresh)
        homeSwipeRefresh.setOnRefreshListener {
            homeSwipeRefresh.isRefreshing = true
            homeViewModel.getNews(newChannelArray[index])
            index = (++index)%newChannelArray.size
        }

        //观察数据变化将其加入List
        homeViewModel.newsLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { result ->
            val news = result.getOrNull()
            if (news != null) {
                recyclerView.visibility = View.VISIBLE
                val size = if(homeViewModel.newsList.size > 20) 20 else homeViewModel.newsList.size
                val temporalList = ArrayList<NewsResponse.Detail>()
                temporalList.addAll(news)
                for (i in 0 until size){
                    temporalList.add(homeViewModel.newsList[i])
                }
                homeViewModel.newsList.clear()
                homeViewModel.newsList.addAll(temporalList)
                homeAdapter.notifyDataSetChanged()
            } else {
                android.widget.Toast.makeText(activity,
                    "没有对应分类的新闻，可输入头条,财经,体育,娱乐,军事,教育,科技,NBA,股票,星座,女性,健康,育儿",
                    android.widget.Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            homeSwipeRefresh.isRefreshing = false
        })
    }
}