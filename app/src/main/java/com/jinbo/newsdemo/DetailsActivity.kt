package com.jinbo.newsdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jinbo.newsdemo.logic.Repository
import java.net.URL

/***********详细信息页面**************/
class DetailsActivity: AppCompatActivity(){

    private val url by lazy { intent.getStringExtra("DetailsUrl") }
    private var likeFlag = false//标记点赞状态

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        likeFlag = intent.getBooleanExtra("DetailsLikeFlag", false)

        //设置toolbar相关功能
        val toolbar: Toolbar = findViewById(R.id.details_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //设置WebView相关功能
        val detailsWebView: WebView = findViewById(R.id.details_webView)
        detailsWebView.settings.javaScriptEnabled = true
        detailsWebView.webViewClient = WebViewClient()
        if (url != null) {
            detailsWebView.loadUrl(url!!)
        }

        //设置点赞功能
        val floatingActionButton: FloatingActionButton = findViewById(R.id.details_fab)
        floatingActionButton.setOnClickListener {
            likeFlag = !likeFlag
            if(likeFlag) Toast.makeText(this, "点赞成功", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "取消点赞", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_detailsactivity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.toolbarMenu_collect -> Toast.makeText(this, "你点击了收藏", Toast.LENGTH_SHORT).show()
            R.id.toolbarMenu_share -> Toast.makeText(this, "你点击了分享", Toast.LENGTH_SHORT).show()
            R.id.toolbarMenu_incomeColumn -> Toast.makeText(this, "你点击了收入专栏", Toast.LENGTH_SHORT).show()
            R.id.toolbarMenu_displayMode -> Toast.makeText(this, "你点击了显示模式", Toast.LENGTH_SHORT).show()
            R.id.toolbarMenu_report -> Toast.makeText(this, "举报！举报！", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        //将点赞结果存储
        if (likeFlag) Repository.saveLike(URL(url))
    }
}