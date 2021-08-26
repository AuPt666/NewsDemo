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

/***********小岛发现页面推荐适配器**************/
class IslandFindImageAdapter(private val islandFindFragment: IslandFindFragment, private val islandFindImgList: List<NewsResponse.Detail>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.island_item_find_onlyimage, parent, false)
        val holder = IslandFindLengthWaysViewHolder(view)

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build())

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val islandFindMsg = islandFindImgList[position]
            val intent = Intent(parent.context, DetailsActivity::class.java).apply {
                putExtra("DetailsUrl", islandFindMsg.url.toString())
            }
            //启动详情页面
            islandFindFragment.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val islandFindImageDetail = islandFindImgList[position]
        if (islandFindImageDetail != null){
            val islandFootPrintLengthWaysViewHolder = holder as IslandFindLengthWaysViewHolder
            Glide.with(islandFindFragment.requireContext()).asBitmap()
                .placeholder(R.drawable.loading).load(islandFindImageDetail.pic).into(islandFootPrintLengthWaysViewHolder.imageView);
        }
    }

    override fun getItemCount() = islandFindImgList.size

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

    inner class IslandFindLengthWaysViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.island_item_findOnlyImage_imageView)
    }
}