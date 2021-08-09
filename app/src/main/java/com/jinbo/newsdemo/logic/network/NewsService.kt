package com.jinbo.newsdemo.logic.network

import com.jinbo.newsdemo.BaseActivity
import com.jinbo.newsdemo.logic.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.net.URLEncoder

interface NewsService {

    @GET("news/get?channel={channel}&num={num}&appkey=${BaseActivity.APPKEY}")
    fun getNews(@Path("channel") channel: String, @Path("num") num: Int): Call<NewsResponse>

}