package com.jinbo.newsdemo.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jinbo.newsdemo.DetailsActivity
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.model.NewsResponse
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class HomeAdapter(private val homeFragment: HomeFragment, private val homeMsgList: List<NewsResponse.Detail>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = when (viewType){
            HomeMsg.TYPE_HASIMAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_hasimage, parent, false)
                HomeHasImageViewHolder(view)
            }
            HomeMsg.TYPE_ONLYTEXT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_onlytext, parent, false)
                HomeOnlyTextViewHolder(view)
            }
            HomeMsg.TYPE_ONLYIMAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_onlyimage, parent, false)
                HomeOnlyImageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_hasimage, parent, false)
                HomeHasImageViewHolder(view)
            }
        }

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build())

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val homeMsg = homeMsgList[position]
            val intent = Intent(parent.context, DetailsActivity::class.java).apply {
                putExtra("DetailsUrl", homeMsg.url.toString())
            }
            //启动详情页面
            homeFragment.startActivity(intent)
            homeFragment.activity?.finish()
            //将浏览记录保存至历史列表
            homeFragment.homeViewModel.saveHistory(homeMsg)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val homeMsgDetail = homeMsgList[position]
        when {
            homeMsgDetail.pic == null -> {
                val homeOnlyTextViewHolder: HomeOnlyTextViewHolder = holder as HomeOnlyTextViewHolder
                homeOnlyTextViewHolder.apply {
                    category.text = homeMsgDetail.category
                    src.text = homeMsgDetail.src
                    title.text = homeMsgDetail.title
                    content.text = homeMsgDetail.content
                }
            }
            homeMsgDetail.content == null -> {
                val homeOnlyImageViewHolder: HomeOnlyImageViewHolder = holder as HomeOnlyImageViewHolder
                homeOnlyImageViewHolder.apply {
                    title.text = homeMsgDetail.title
                    src.text = homeMsgDetail.src
                    imageVessel.setImageBitmap(getBitmap(homeMsgDetail.pic))
                }
            }else -> {
                val homeHasImageViewHolder: HomeHasImageViewHolder = holder as HomeHasImageViewHolder
                homeHasImageViewHolder.apply {
                    category.text = homeMsgDetail.category
                    src.text = homeMsgDetail.src
                    title.text = homeMsgDetail.title
                    content.text = homeMsgDetail.content
                    pic.setImageBitmap(getBitmap(homeMsgDetail.pic))
                }
            }
        }
    }

    override fun getItemCount() = homeMsgList.size

    override fun getItemViewType(position: Int): Int {
        val homeMsgDetail = homeMsgList[position]
        return when {
            homeMsgDetail.pic == null -> {//加载只有文字的布局
                HomeMsg.TYPE_ONLYTEXT
            }
            homeMsgDetail.content == null -> {//加载只有图片的布局
                HomeMsg.TYPE_ONLYIMAGE
            }
            else -> {//加载既有图片又有文字的布局
                HomeMsg.TYPE_HASIMAGE
            }
        }
    }

    //通过URL获取图片资源
    private fun getBitmap(url: URL): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            conn.requestMethod = "GET"
            if (conn.responseCode == 200) {
                val inputStream: InputStream = conn.inputStream
                bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        return bitmap
    }

    inner class HomeOnlyImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.homeItemOnlyImage_titleTextView)
        val src: TextView = view.findViewById(R.id.homeItemOnlyImage_srcTextView)
        val imageVessel: ImageView = view.findViewById(R.id.homeItemOnlyImage_imageVessel)
    }

    inner class HomeOnlyTextViewHolder(view: View): RecyclerView.ViewHolder(view){
        val category: TextView = view.findViewById(R.id.homeItemOnlyText_categoryTextView)
        val src: TextView = view.findViewById(R.id.homeItemOnlyText_srcTextView)
        val title: TextView = view.findViewById(R.id.homeItemOnlyText_titleTextView)
        val content: TextView = view.findViewById(R.id.homeItemOnlyText_contentTextView)
    }

    inner class HomeHasImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val category: TextView = view.findViewById(R.id.homeItemWithImage_categoryTextView)
        val src: TextView = view.findViewById(R.id.homeItemWithImage_srcTextView)
        val title: TextView = view.findViewById(R.id.homeItemWithImage_titleTextView)
        val content: TextView = view.findViewById(R.id.homeItemWithImage_contentTextView)
        val pic: ImageView = view.findViewById(R.id.homeItemWithImage_picImageView)
    }


}