package com.jinbo.newsdemo.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object DemoNetwork {

    //创建网络服务接口对象
    private val newsService = ServiceCreator.create(NewsService::class.java)
    private val islandService = ServiceCreator.create(IslandService::class.java)

    suspend fun getNews(channel: String, num: Int) = newsService.getNews(channel, num).await()
    suspend fun getIsland(sort: String, pagesize: Int) = islandService.getIsland(sort, pagesize).await()

    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}