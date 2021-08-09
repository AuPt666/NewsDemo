package com.jinbo.newsdemo.logic.model

import android.net.Uri
import com.google.gson.annotations.SerializedName


/************解析新闻API信息**********************/
data class NewsResponse(val status: Int,val msg: String, val result: Result) {

    data class Result(val channel: String, val num: String, val list: List<Detail>)

    data class Detail(val title: String, val time: String, val src: String, val category: String, val pic: Uri, @SerializedName("news_content") val content: String, val url: Uri, val webUrl: Uri)

}
