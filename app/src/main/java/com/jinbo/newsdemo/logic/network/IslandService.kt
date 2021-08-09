package com.jinbo.newsdemo.logic.network

import com.jinbo.newsdemo.BaseActivity
import com.jinbo.newsdemo.logic.model.IslandResponse
import com.jinbo.newsdemo.logic.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IslandService {

    //sort = addtime 按加入时间获取 sort=rand 随机获取
    @GET("xiaohua/all?pagenum=1&pagesize={pagesize}&sort={sort}&appkey=${BaseActivity.APPKEY}")
    fun getIsland(@Path("sort") sort: String, @Path("pagesize") pagesize: Int): Call<IslandResponse>

}