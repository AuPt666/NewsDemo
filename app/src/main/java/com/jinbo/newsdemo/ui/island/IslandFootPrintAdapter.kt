package com.jinbo.newsdemo.ui.island

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

class IslandFootPrintAdapter(private val islandFootPrintFragment: IslandFootPrintFragment, private val islandFootPrintMsgList: List<NewsResponse.Detail>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.island_item_footprint_history, parent, false)
        val holder = IslandFootPrintViewHolder(view)

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build())

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val islandFootPrintMsg = islandFootPrintMsgList[position]
            val intent = Intent(parent.context, DetailsActivity::class.java).apply {
                putExtra("DetailsUrl", islandFootPrintMsg.url.toString())
            }
            //启动详情页面
            islandFootPrintFragment.startActivity(intent)
            islandFootPrintFragment.activity?.finish()
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val islandFootPrintMsgDetail = islandFootPrintMsgList[position]
        val islandFootPrintViewHolder: IslandFootPrintViewHolder = holder as IslandFootPrintAdapter.IslandFootPrintViewHolder
        islandFootPrintViewHolder.apply {
            head.setImageBitmap(getBitmap(islandFootPrintMsgDetail.pic))
            title.text = islandFootPrintMsgDetail.title
            content.text = islandFootPrintMsgDetail.content
        }
    }

    override fun getItemCount() = islandFootPrintMsgList.size


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

    inner class IslandFootPrintViewHolder(view: View): RecyclerView.ViewHolder(view){
        val head: ImageView = view.findViewById(R.id.island_item_footprint_history_head_imageView)
        val title: TextView = view.findViewById(R.id.island_item_footprint_history_title_textView)
        val content: TextView = view.findViewById(R.id.island_item_footprint_history_content_textView)
    }
}