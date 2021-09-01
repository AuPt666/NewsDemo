package com.jinbo.newsdemo.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jinbo.newsdemo.BaseApplication
import com.jinbo.newsdemo.DetailsActivity
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.Repository
import com.jinbo.newsdemo.logic.model.NewsResponse

/***********主页列表适配器**************/
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

        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val homeMsg = homeMsgList[position]
            val intent = Intent(homeFragment.context, DetailsActivity::class.java).apply {
                putExtra("DetailsUrl", homeMsg.url.toString())
                putExtra("DetailsLikeFlag", true)
            }
            //将浏览记录保存至历史列表
            homeFragment.homeViewModel.saveHistory(homeMsg, BaseApplication.context)
            //启动详情页面
            homeFragment.context?.startActivity(intent)
            notifyItemChanged(position)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val homeMsgDetail = homeMsgList[position]
        when(position%3) {
             0 -> {
                val homeOnlyTextViewHolder: HomeOnlyTextViewHolder = holder as HomeOnlyTextViewHolder
                homeOnlyTextViewHolder.apply {
                    category.text = homeMsgDetail.category
                    src.text = homeMsgDetail.src
                    title.text = homeMsgDetail.title
                    content.text = homeMsgDetail.content
                    if (Repository.isRead(homeMsgDetail.title)) {
                        readState.text = "已阅读"
                    } else{
                        readState.text = ""
                    }
                }
            }
            1 -> {
                val homeOnlyImageViewHolder: HomeOnlyImageViewHolder = holder as HomeOnlyImageViewHolder
                homeOnlyImageViewHolder.apply {
                    title.text = homeMsgDetail.title
                    src.text = homeMsgDetail.src
                    Glide.with(homeFragment.requireContext()).asBitmap()
                        .placeholder(R.drawable.loading).load(homeMsgDetail.pic).into(imageVessel)
                    if (Repository.isRead(homeMsgDetail.title)) {
                        readState.text = "已阅读"
                    } else{
                        readState.text = ""
                    }
                }
            }else -> {
                val homeHasImageViewHolder: HomeHasImageViewHolder = holder as HomeHasImageViewHolder
                homeHasImageViewHolder.apply {
                    category.text = homeMsgDetail.category
                    src.text = homeMsgDetail.src
                    title.text = homeMsgDetail.title
                    content.text = homeMsgDetail.content
                    Glide.with(homeFragment.requireContext()).asBitmap()
                        .placeholder(R.drawable.loading).load(homeMsgDetail.pic).into(pic)
                    if (Repository.isRead(homeMsgDetail.title)) {
                        readState.text = "已阅读"
                    } else{
                        readState.text = ""
                    }
                }
            }
        }
    }

    override fun getItemCount() = homeMsgList.size

    override fun getItemViewType(position: Int): Int {
        return when(position%3) {
            0 -> {//加载只有文字的布局
                HomeMsg.TYPE_ONLYTEXT
            }
            1 -> {//加载只有图片的布局
                HomeMsg.TYPE_ONLYIMAGE
            }
            else -> {//加载既有图片又有文字的布局
                HomeMsg.TYPE_HASIMAGE
            }
        }
    }

    //只有图片时加载的ViewHolder
    inner class HomeOnlyImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.homeItemOnlyImage_titleTextView)
        val src: TextView = view.findViewById(R.id.homeItemOnlyImage_srcTextView)
        val imageVessel: ImageView = view.findViewById(R.id.homeItemOnlyImage_imageVessel)
        val readState: TextView = view.findViewById(R.id.homeItemOnlyImage_readStateTextView)
    }

    //只有文字时加载的ViewHolder
    inner class HomeOnlyTextViewHolder(view: View): RecyclerView.ViewHolder(view){
        val category: TextView = view.findViewById(R.id.homeItemOnlyText_categoryTextView)
        val src: TextView = view.findViewById(R.id.homeItemOnlyText_srcTextView)
        val title: TextView = view.findViewById(R.id.homeItemOnlyText_titleTextView)
        val content: TextView = view.findViewById(R.id.homeItemOnlyText_contentTextView)
        val readState: TextView = view.findViewById(R.id.homeItemOnlyText_readStateTextView)
    }

    //都有的时候加载的ViewHolder
    inner class HomeHasImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val category: TextView = view.findViewById(R.id.homeItemWithImage_categoryTextView)
        val src: TextView = view.findViewById(R.id.homeItemWithImage_srcTextView)
        val title: TextView = view.findViewById(R.id.homeItemWithImage_titleTextView)
        val content: TextView = view.findViewById(R.id.homeItemWithImage_contentTextView)
        val pic: ImageView = view.findViewById(R.id.homeItemWithImage_picImageView)
        val readState: TextView = view.findViewById(R.id.homeItemHasImage_readStateTextView)
    }


}