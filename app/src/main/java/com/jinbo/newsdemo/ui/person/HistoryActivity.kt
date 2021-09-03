package com.jinbo.newsdemo.ui.person

import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinbo.newsdemo.BaseApplication
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.Repository
import com.jinbo.newsdemo.logic.model.NewsResponse
import com.jinbo.newsdemo.ui.home.HomeViewModel
import com.jinbo.newsdemo.ui.island.IslandFootPrintAdapter


/*************浏览历史活动界面*******************/
class HistoryActivity: AppCompatActivity() {

    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    private lateinit var historyAdapter: HistoryAdapter

    private var historyList: List<NewsResponse.Detail>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //设置toolBar相关功能
        val toolbar: Toolbar = findViewById(R.id.history_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //加载浏览历史
        val queryHistoryTask = QueryHistoryTask()
        queryHistoryTask.execute()
        val historyLayoutManger = LinearLayoutManager(this)
        val historyRecyclerView: RecyclerView = findViewById(R.id.history_recyclerView)
        historyRecyclerView.layoutManager = historyLayoutManger
        historyAdapter = HistoryAdapter(this, historyList!!)
        historyRecyclerView.adapter = historyAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toorbar_historyactivity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            //删除历史信息
            R.id.toolbarMenu_deleteHistory ->{
                val deleteHistoryTask = DeleteHistoryTask()
                deleteHistoryTask.execute()
            }
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //开启线程读取数据库数据并返回查询结果
    private inner class QueryHistoryTask: AsyncTask<Void, Void, List<NewsResponse.Detail>>() {
        override fun doInBackground(vararg params: Void?): List<NewsResponse.Detail> {
            historyList = homeViewModel.getHistory(BaseApplication.context)
            return historyList as List<NewsResponse.Detail>
        }

        //查询完成，刷新浏览历史列表
        override fun onPostExecute(result: List<NewsResponse.Detail>?) {
            super.onPostExecute(result)
            val historyBackground: ImageView = findViewById(R.id.history_background_imageView)
            historyList?.let {
                if (historyList!!.isNotEmpty()){
                    historyAdapter.setHistoryList(it)
                    historyBackground.visibility = View.INVISIBLE
                }else{
                    historyBackground.visibility = View.VISIBLE
                }
            }
            historyAdapter.notifyDataSetChanged()
        }
    }

    private inner class DeleteHistoryTask: AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            Repository.deleteDataBase(BaseApplication.context)
            return null
        }

        //浏览历史删除完成，刷新列表，设置背景图案
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Toast.makeText(this@HistoryActivity, "浏览历史已删除", Toast.LENGTH_SHORT).show()
            historyList = ArrayList<NewsResponse.Detail>()
            historyAdapter.setHistoryList(historyList as ArrayList<NewsResponse.Detail>)
            historyAdapter.notifyDataSetChanged()
            val historyBackground: ImageView = findViewById(R.id.history_background_imageView)
            historyBackground.visibility = View.VISIBLE
        }
    }
}