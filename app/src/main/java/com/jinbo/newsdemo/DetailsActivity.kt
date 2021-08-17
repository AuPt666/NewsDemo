package com.jinbo.newsdemo

import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.net.URL

class DetailsActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_details)
        val toolbar: Toolbar = findViewById(R.id.details_toolbar)
        val detailsWebView: WebView = findViewById(R.id.details_webView)
        detailsWebView.settings.javaScriptEnabled = true
        detailsWebView.webViewClient = WebViewClient()
        val url: String? = intent.getStringExtra("DetailsUrl")
        if (url != null) {
            detailsWebView.loadUrl(url)
        }
    }
}