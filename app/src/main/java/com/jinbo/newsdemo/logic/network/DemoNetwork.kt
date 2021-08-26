package com.jinbo.newsdemo.logic.network

import com.jinbo.newsdemo.BaseApplication
import com.jinbo.newsdemo.logic.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/***********发送和接受网络请求**************/
object DemoNetwork {

    //创建网络服务接口对象
    private val newsService = ServiceCreator.create(NewsService::class.java)
    private val islandService = ServiceCreator.create(IslandService::class.java)

    //异步获取新闻信息
    suspend fun getNews(channel: String, num: Int): NewsResponse {
        return newsService.getNews(channel, 0, num, BaseApplication.APPKEY).await()
    }

    //异步获取笑话信息
    suspend fun getIsland(sort: String, pagesize: Int) = islandService.getIsland(1, pagesize, sort, BaseApplication.APPKEY).await()

    //Call接口外挂函数await
    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("返回的消息为空"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}