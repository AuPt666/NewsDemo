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
import com.jinbo.newsdemo.logic.model.IslandResponse
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class IslandFindAdapter(private val islandFindFragment: IslandFindFragment, private val islandFindMsgList: List<IslandResponse.Detail>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = when(viewType){
            IslandFindMsg.TYPE_HASIMAGE -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.island_item_hasimage, parent, false)
                IslandFindHasImageViewHolder(view)
            }
            IslandFindMsg.TYPE_ONLYTEXT -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.island_item_onlytext, parent, false)
                IslandFindOnlyTextViewHolder(view)
            }
            IslandFindMsg.TYPE_ONLYIMAGE -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.island_item_find_onlyimage, parent, false)
                IslandFindOnlyImageViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.island_item_find_onlyimage, parent, false)
                IslandFindOnlyImageViewHolder(view)
            }
        }

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build())

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val islandFindMsg = islandFindMsgList[position]
            val intent = Intent(parent.context, DetailsActivity::class.java).apply {
                putExtra("DetailsUrl", islandFindMsg.url.toString())
            }
            //启动详情页面
            islandFindFragment.startActivity(intent)
            islandFindFragment.activity?.finish()
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val islandFindMsgDetail = islandFindMsgList[position]
        when {
            islandFindMsgDetail.pic == null -> {
                val islandFindOnlyTextViewHolder: IslandFindAdapter.IslandFindOnlyTextViewHolder = holder as IslandFindAdapter.IslandFindOnlyTextViewHolder
                islandFindOnlyTextViewHolder.apply {
                    author.text = islandFindMsgDetail.addtime
                    title.text = "这是第${islandFindMsgList.size}条"
                    content.text = islandFindMsgDetail.content
                }
            }
            islandFindMsgDetail.content == null -> {
                val islandFindOnlyImageViewHolder: IslandFindAdapter.IslandFindOnlyImageViewHolder = holder as IslandFindAdapter.IslandFindOnlyImageViewHolder
                islandFindOnlyImageViewHolder.apply {
                    imageView.setImageBitmap(getBitmap(islandFindMsgDetail.pic))
                }
            }else -> {
                val islandFindHasImageViewHolder: IslandFindAdapter.IslandFindHasImageViewHolder = holder as IslandFindAdapter.IslandFindHasImageViewHolder
                islandFindHasImageViewHolder.apply {
                    author.text = islandFindMsgDetail.addtime
                    title.text = "这是第${islandFindMsgList.size}条"
                    imageView.setImageBitmap(getBitmap(islandFindMsgDetail.pic))
            }
        }
        }
    }

    override fun getItemCount() = islandFindMsgList.size

    override fun getItemViewType(position: Int): Int {
        val islandMsgDetail = islandFindMsgList[position]
        return when {
            islandMsgDetail.pic == null -> {//加载只有文字的布局
                IslandFindMsg.TYPE_ONLYTEXT
            }
            islandMsgDetail.content == null -> {//加载只有图片的布局
                IslandFindMsg.TYPE_ONLYIMAGE
            }
            else -> {
                IslandFindMsg.TYPE_HASIMAGE
            }
        }
    }

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

    inner class IslandFindOnlyImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.island_item_findOnlyImage_imageView)
    }

    inner class IslandFindHasImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val author: TextView = view.findViewById(R.id.island_authorNameHasImage_textView)
        val title: TextView = view.findViewById(R.id.island_titleHasImage_textView)
        val imageView: ImageView = view.findViewById(R.id.island_imageHasImage_imageView)
    }

    inner class IslandFindOnlyTextViewHolder(view: View): RecyclerView.ViewHolder(view){
        val author: TextView = view.findViewById(R.id.island_authorNameOnlyText_textView)
        val title: TextView = view.findViewById(R.id.island_titleOnlyText_textView)
        val content: TextView = view.findViewById(R.id.island_content_textView)
    }
}