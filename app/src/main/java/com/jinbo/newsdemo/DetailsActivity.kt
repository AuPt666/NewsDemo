package com.jinbo.newsdemo

import android.annotation.SuppressLint
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
import java.net.URL

/***********详细信息页面**************/
class DetailsActivity: AppCompatActivity(){

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //设置toolbar相关功能
        val toolbar: Toolbar = findViewById(R.id.details_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //设置WebView相关功能
        val detailsWebView: WebView = findViewById(R.id.details_webView)
        detailsWebView.settings.javaScriptEnabled = true
        detailsWebView.webViewClient = WebViewClient()
        val url: String? = intent.getStringExtra("DetailsUrl")
        if (url != null) {
            detailsWebView.loadUrl(url)
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
}