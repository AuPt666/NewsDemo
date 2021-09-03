package com.jinbo.newsdemo.ui.island

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jinbo.newsdemo.DetailsActivity
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.model.NewsResponse

/***********小岛足迹页面（浏览历史）适配器**************/
class IslandFootPrintAdapter(private val islandFootPrintFragment: Fragment, private var islandFootPrintMsgList: List<NewsResponse.Detail>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.island_item_footprint_history, parent, false)
        val holder = IslandFootPrintViewHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val islandFootPrintMsg = islandFootPrintMsgList[position]
            val intent = Intent(parent.context, DetailsActivity::class.java).apply {
                putExtra("DetailsUrl", islandFootPrintMsg.url.toString())
                putExtra("ViewType", 0)
            }
            //启动详情页面
            islandFootPrintFragment.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val islandFootPrintMsgDetail = islandFootPrintMsgList[position]
        if (islandFootPrintMsgDetail != null){
            val islandFootPrintViewHolder: IslandFootPrintViewHolder = holder as IslandFootPrintAdapter.IslandFootPrintViewHolder
            islandFootPrintViewHolder.apply {
                val bitmapUrl = islandFootPrintMsgDetail.pic
                Glide.with(islandFootPrintFragment.requireContext()).load(islandFootPrintMsgDetail.pic).into(head);
                title.text = islandFootPrintMsgDetail.title
                content.text = islandFootPrintMsgDetail.content
            }
        }
    }

    override fun getItemCount() = islandFootPrintMsgList.size

    fun setIslandFootPrintMsgList(list: List<NewsResponse.Detail>){
        islandFootPrintMsgList = list
    }


    inner class IslandFootPrintViewHolder(view: View): RecyclerView.ViewHolder(view){
        val head: ImageView = view.findViewById(R.id.island_item_footprint_history_head_imageView)
        val title: TextView = view.findViewById(R.id.island_item_footprint_history_title_textView)
        val content: TextView = view.findViewById(R.id.island_item_footprint_history_content_textView)
    }

}