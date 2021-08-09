package com.jinbo.newsdemo.logic.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

/************解析笑话大全API信息**********************/
data class IslandResponse(val status: Int,val msg: String, val result: Result) {

    data class Result(val total: String, val pagenum: String, val pagesize: String, val list: List<Detail>)

    data class Detail(@SerializedName("island_content") val content: String,val pic: Uri, val addtime: String, val url: Uri)

}
