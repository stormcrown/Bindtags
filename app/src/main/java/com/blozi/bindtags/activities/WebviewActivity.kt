package com.blozi.bindtags.activities

import android.os.Bundle
import android.app.Activity
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView


import com.blozi.bindtags.R
import com.blozi.bindtags.util.webUtils.BloziWebViewClient

class WebviewActivity : Activity() {

    private var webview: WebView? = null
    private var webSettings: WebSettings?=null
    private var webViewClient: BloziWebViewClient?=null
    private var webChromeClient = WebChromeClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)

        webview = findViewById(R.id.webview)
        webSettings =webview!!.settings
        webSettings!!.javaScriptEnabled=true
        webSettings!!.defaultTextEncodingName = "utf-8"
        webViewClient = BloziWebViewClient(this)
        webview!!.webViewClient = webViewClient
        webview!!.webChromeClient= webChromeClient


        webview!!.loadUrl("file:///android_asset/WebRoot/html/login.html")

    }

}
