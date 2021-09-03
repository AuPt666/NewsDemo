package com.jinbo.newsdemo.logic

import android.content.Context
import androidx.room.*
import com.jinbo.newsdemo.logic.dao.NewsDataDao
import com.jinbo.newsdemo.logic.model.NewsData


/**********数据库**********/
@Database(version = 1, entities = [NewsData::class])
abstract class NewsDataBase: RoomDatabase() {
    abstract fun newsDataDao(): NewsDataDao

    companion object{
        private var instance: NewsDataBase? = null

        @Synchronized
        fun getDatabase(context: Context): NewsDataBase {
            instance?.let {
                return it
            }
            //构建数据库实体
            return Room.databaseBuilder(context.applicationContext, NewsDataBase::class.java, "news_database").build().apply {
                instance = this
            }
        }
    }
}