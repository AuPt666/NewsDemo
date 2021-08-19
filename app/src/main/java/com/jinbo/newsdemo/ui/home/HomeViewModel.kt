package com.jinbo.newsdemo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jinbo.newsdemo.logic.Repository
import com.jinbo.newsdemo.logic.model.IslandResponse
import com.jinbo.newsdemo.logic.model.NewsResponse

class HomeViewModel : ViewModel() {

    private val newsResponseLiveData = MutableLiveData<String>()

    val newsList = ArrayList<NewsResponse.Detail>()

    val newsLiveData =Transformations.switchMap(newsResponseLiveData) { channel ->
        Repository.getNews(channel, 10)
    }

    fun getNews(channel: String){
        newsResponseLiveData.value = channel
    }

    fun saveHistory(detail: NewsResponse.Detail) = Repository.saveHistory(detail)

    fun getHistory(): List<NewsResponse.Detail> = Repository.getHistory()

    fun isHistorySaved(): Boolean = Repository.isHistorySave()

}