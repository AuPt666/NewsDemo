package com.jinbo.newsdemo.logic.network

import com.jinbo.newsdemo.logic.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/***********发送新闻网络请求的接口**************/
interface NewsService {
    //channel = 头条,财经,体育,娱乐,军事,教育,科技,NBA,股票,星座,女性,健康,育儿
    @GET("/news/get")
    fun getNews(@Query("channel") channel: String, @Query("start") start: Int, @Query("num") num: Int, @Query("appkey") appkey: String): Call<NewsResponse>

}