package com.blozi.bindtags.activities.fragment.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.blozi.bindtags.R
import com.blozi.bindtags.activities.fragment.BaseFragment
import com.blozi.bindtags.util.webUtils.BloziWebViewClient
import java.util.*

class WebFragment :BaseFragment {
    private var webView : WebView?=null
    private var webSettings:WebSettings?=null
    private var webViewClient: BloziWebViewClient?=null
    private var webChromeClient = WebChromeClient()
    constructor() : super()
    var url = "file:///android_asset/WebRoot/html/first.html"
    override fun editableToggle() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.web_fragment, null)
        super.onCreateView(inflater, container, savedInstanceState)
        if (arguments != null && arguments!!.get("url") != null) {
            url = arguments!!.get("url").toString()
        }
        webView = view.findViewById(R.id.webview)
        webSettings = webView!!.settings
        webSettings!!.javaScriptEnabled=true
        webSettings!!.defaultTextEncodingName = "utf-8"
        webViewClient = BloziWebViewClient(getContext())


        webView!!.webViewClient=webViewClient
        webView!!.webChromeClient= webChromeClient
        val date1 =Date()

        webView!!.loadUrl(url)
        val date2 = Date()
        val t= date2.time -date1.time
        Log.i("耗时",t.toString() )

//        webView!!.clearCache(true)

        return view
    }

    override fun isForceTag(): Boolean {
        return false
    }

    override fun accetpMsg(str: String?) {

    }
}