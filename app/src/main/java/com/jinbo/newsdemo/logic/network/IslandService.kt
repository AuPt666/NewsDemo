package com.jinbo.newsdemo.logic.network

import com.jinbo.newsdemo.logic.model.IslandResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/***********发送笑话大全API网络请求的接口**************/
interface IslandService {

    //sort = addtime 按加入时间获取 sort=rand 随机获取
    @GET("/xiaohua/text/")
    fun getIsland(@Query("pagenum") pagenum: Int, @Query("pagesize") pagesize: Int, @Query("sort") sort: String, @Query("appkey") appkey: String): Call<IslandResponse>

}