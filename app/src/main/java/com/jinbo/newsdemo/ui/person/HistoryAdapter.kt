package com.jinbo.newsdemo.ui.person

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jinbo.newsdemo.DetailsActivity
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.model.NewsResponse

class HistoryAdapter(private val historyActivity: HistoryActivity, private var historyList: List<NewsResponse.Detail>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        val holder = HistoryViewHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val historyMsg = historyList[position]
            val intent = Intent(parent.context, DetailsActivity::class.java).apply {
                putExtra("DetailsUrl", historyMsg.url.toString())
                putExtra("ViewType", 0)
            }
            //启动详情页面
            historyActivity.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val historyMsy = historyList[position]
        if (historyMsy != null){
            val historyViewHolder = holder as HistoryViewHolder
            Glide.with(historyActivity).asBitmap()
                .placeholder(R.drawable.loading).load(historyMsy.pic).into(historyViewHolder.head);
            historyViewHolder.apply {
                title.text = historyMsy.title
                content.text = historyMsy.content
            }
        }
    }

    override fun getItemCount() = historyList.size

    fun setHistoryList(list: List<NewsResponse.Detail>){
        historyList = list
    }

    inner class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        val head: ImageView = view.findViewById(R.id.history_item_head_imageView)
        val title: TextView = view.findViewById(R.id.history_item_title_textView)
        val content: TextView = view.findViewById(R.id.history_item_content_textView)
    }
}