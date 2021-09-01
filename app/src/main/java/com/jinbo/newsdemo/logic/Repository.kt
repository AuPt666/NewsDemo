package com.jinbo.newsdemo.logic

import android.content.Context
import android.util.Log
import androidx.lifecycle.liveData
import com.jinbo.newsdemo.BaseApplication
import com.jinbo.newsdemo.BaseApplication.Companion.context
import com.jinbo.newsdemo.logic.dao.IslandDao
import com.jinbo.newsdemo.logic.dao.NewsDao
import com.jinbo.newsdemo.logic.dao.NewsDataDao
import com.jinbo.newsdemo.logic.model.NewsData
import com.jinbo.newsdemo.logic.model.NewsResponse
import com.jinbo.newsdemo.logic.network.DemoNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.net.URL
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

    //从网络API获取新闻信息
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

    //从网络API获取笑话信息
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
    fun getHistory(context: Context): List<NewsResponse.Detail>{
        val newsList = ArrayList<NewsResponse.Detail>()
        val newDataList = getNewsDataDao(context).loadAll()
        for (i in newDataList.indices){
            newsList.add(changeToNewsResponseDetail(newDataList[i]))
            Log.e("getHistory", "${newDataList[i]}")
        }
        return newsList
    }

    //获取点赞信息
    fun getLike(): ArrayList<URL>{
        return IslandDao.getIslandDao()
    }

    //存储访问历史
    fun saveHistory(detail: NewsResponse.Detail, context: Context){
        Thread{
            Log.e("savaHistory:存储", detail.toString())
            if (getNewsDataDao(context).isRead(detail.title) == null){
                getNewsDataDao(context).insertNewsData(changeToNewsData(detail))
            }
        }.start()
    }

    //存储点赞信息
    fun saveLike(url: URL){
        IslandDao.likeIslandDao(url)
    }

    fun isLike(url: URL) = IslandDao.isLike(url)

    fun isRead(title: String) = NewsDao.isRead(title)

    fun isHistorySave(): Boolean = NewsDao.isNewsDaoSaved()

    fun deleteDataBase(context: Context) = getNewsDataDao(context).deleteAll()

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

    private fun getNewsDataDao(context: Context): NewsDataDao{
        return NewsDataBase.getDatabase(context).newsDataDao()
    }

    private fun changeToNewsData(detail: NewsResponse.Detail): NewsData{
        val pic: String = detail.pic.toString()
        val url = detail.url.toString()
        val webUrl = if (detail.webUrl != null) detail.webUrl.toString() else ""
        return NewsData(detail.title, detail.time, detail.src, detail.category, pic, detail.content, url, webUrl, true)
    }

    private fun changeToNewsResponseDetail(newsData: NewsData): NewsResponse.Detail{
        val pic = URL(newsData.pic)
        val url = URL(newsData.url)
        val webUrl = if (newsData.webUrl != "") URL(newsData.webUrl) else null
        return NewsResponse.Detail(newsData.title, newsData.time, newsData.src, newsData.category, pic, newsData.content, url, webUrl)
    }
}