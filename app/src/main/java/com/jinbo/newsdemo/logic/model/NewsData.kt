package com.jinbo.newsdemo.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

/********新闻存储数据实体类**********/
@Entity
data class NewsData(val title: String, val time: String, val src: String, val category: String, val pic: String, var content: String, val url: String, val webUrl: String, val state: Boolean) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0//主键自增id
}