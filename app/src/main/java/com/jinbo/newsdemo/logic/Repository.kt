package com.jinbo.newsdemo.logic

import androidx.lifecycle.liveData
import com.jinbo.newsdemo.logic.network.DemoNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    fun getNew(channel: String, num: Int) = fire(Dispatchers.IO){
        val newsResponse = DemoNetwork.getNews(channel, num)
        if (newsResponse.msg == "ok"){
            val result = newsResponse.result
            Result.success(result)
        }else {
            Result.failure(RuntimeException("response status is ${newsResponse.status}"))
        }
    }

    fun getIsLand(sort: String, pagesize: Int) = fire(Dispatchers.IO){
        val islandResponse = DemoNetwork.getIsland(sort, pagesize)
        if (islandResponse.msg == "ok"){
            val result = islandResponse.result
            Result.success(result)
        }else {
            Result.failure(RuntimeException("response status is ${islandResponse.status}"))
        }

    }



    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }
}