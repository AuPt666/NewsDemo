package com.jinbo.newsdemo.logic.dao

import androidx.room.*
import com.jinbo.newsdemo.logic.model.NewsData

/**********数据库操作类**********/
@Dao
interface NewsDataDao {

    @Insert
    fun insertNewsData(newsData: NewsData): Long

    @Update
    fun upDataNewsData(newsData: NewsData)

    @Query("select * from NewsData")
    fun loadAll(): List<NewsData>

    @Query("select * from NewsData where title = :title")
    fun isRead(title: String): NewsData?

    @Delete
    fun deleteNewsData(newsData: NewsData)

    @Query("delete from NewsData")
    fun deleteAll()

}