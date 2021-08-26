package com.jinbo.newsdemo.ui.island

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jinbo.newsdemo.DetailsActivity
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.model.NewsResponse
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/***********小岛足迹页面推荐适配器**************/
class IslandFootPrintRecommendAdapter(private val islandFootPrintFragment: IslandFootPrintFragment, private val islandFootPrintImgList: List<NewsResponse.Detail>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.island_item_footprint_recommend, parent, false)
        val holder = IslandFootPrintRecommendViewHolder(view)

        //将线程策略检测模式设置为网络策略
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build())
        //开启虚拟机策略检测未关闭的Closable对象泄露
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build())

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val islandFootPrintMsg = islandFootPrintImgList[position]
            val intent = Intent(parent.context, DetailsActivity::class.java).apply {
                putExtra("DetailsUrl", islandFootPrintMsg.url.toString())
            }
            //启动详情页面
            islandFootPrintFragment.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val islandFootPrintDetail = islandFootPrintImgList[position]
        if (islandFootPrintDetail != null){
            val islandFootPrintRecommendViewHolder = holder as IslandFootPrintRecommendViewHolder
            Glide.with(islandFootPrintFragment.requireContext()).load(islandFootPrintDetail.pic).into(islandFootPrintRecommendViewHolder.imageView);
        }
    }

    override fun getItemCount() = islandFootPrintImgList.size

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

    inner class IslandFootPrintRecommendViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.island_item_footprint_recommend_imageView)
    }

}