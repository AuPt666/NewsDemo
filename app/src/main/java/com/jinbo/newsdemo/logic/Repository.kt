package com.jinbo.newsdemo.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.jinbo.newsdemo.logic.dao.NewsDao
import com.jinbo.newsdemo.logic.model.NewsResponse
import com.jinbo.newsdemo.logic.network.DemoNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.regex.Pattern

/***********数据仓库-存取和包装数据**************/
object Repository {

    //script标签的正则表达式
    private const val REGEX_SCRIPT: String = "<script[^>]*?>[\\s\\S]*?<\\/script>"
    //style标签的正则表达式
    private const val REGEX_STYLE: String = "<style[^>]*?>[\\s\\S]*?<\\/style>"
    //HTML标签的正则表达式
    private const val REGEX_HTML: String = "<[^>]+>"
    //空格回车换行符
    private const val REGEX_SPACE: String = "\\s*|\t|\r|\n"

    //获取新闻信息
    fun getNews(channel: String, num: Int) = liveData(Dispatchers.IO){
        val result = try {
            val newsResponse = DemoNetwork.getNews(channel, num)
            if (newsResponse.msg == "ok"){
                //把result拆成details
                val result = newsResponse.result.list
                //过滤HTML文件标签
                for (i in result.indices){
                    result[i].content = deleteHtmlTag(result[i].content)
                }
                Result.success(result)
            }else {
                Result.failure(RuntimeException("返回消息的状态错误，此消息的状态为${newsResponse.status}"))
            }
        } catch (e: Exception) {
            Log.e("Repository.getNews", e.toString())
            Result.failure(e)
        }
        emit(result)
    }

    //获取笑话信息
    fun getIsLand(sort: String, pagesize: Int) = liveData(Dispatchers.IO){
        val result = try {
            val islandResponse = DemoNetwork.getIsland(sort, pagesize)
            if (islandResponse.msg == "ok"){
                val result = islandResponse.result.list
                Result.success(result)
            }else {
                Result.failure(RuntimeException("返回消息的状态错误，次消息的状态为${islandResponse.status}"))
            }
        }catch (e: Exception){
            Log.e("Repository.getIsland", e.toString())
            Result.failure(e)
        }
        emit(result)
    }

    //获取访问历史
    fun getHistory(): List<NewsResponse.Detail>{
        return NewsDao.getNewsDao()
    }

    //存储访问历史
    fun saveHistory(detail: NewsResponse.Detail){
        NewsDao.saveNewsDao(detail)
    }

    fun isHistorySave(): Boolean = NewsDao.isNewsDaoSaved()

    //过滤文本内容中的HTML标签
    private fun deleteHtmlTag(htmlStr: String): String{
        var resultStr = StringBuilder(htmlStr)
        //过滤script标签
        val pScript = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE)
        val mScript = pScript.matcher(resultStr)
        resultStr = StringBuilder(mScript.replaceAll(""))
        //过滤style标签
        val pStyle = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE)
        val mStyle = pStyle.matcher(resultStr)
        resultStr = StringBuilder(mStyle.replaceAll(""))
        //过滤HTML标签
        val pHtml = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE)
        val mHtml = pHtml.matcher(resultStr)
        resultStr = StringBuilder(mHtml.replaceAll(""))
        //过滤空格换行符
        val pSpace = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE)
        val mSpace = pSpace.matcher(resultStr)
        resultStr = StringBuilder(mSpace.replaceAll(""))
        return resultStr.toString()
    }

}