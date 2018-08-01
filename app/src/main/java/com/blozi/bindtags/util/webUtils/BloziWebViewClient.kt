package com.blozi.bindtags.util.webUtils

import android.annotation.TargetApi
import android.content.Context
import android.webkit.WebViewClient
import android.webkit.WebResourceResponse
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView


class BloziWebViewClient :WebViewClient {
    private  var context:Context?=null

    constructor(context: Context?) : super() {
        this.context = context
    }

    override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
        if (url.contains("jquery.slim.min.js")) {//加载指定.js时 引导服务端加载本地Assets/www文件夹下的cordova.js
                return WebResourceResponse("application/x-javascript", "utf-8", context!!. getAssets().open("WebRoot/plugins/jquery/3.2.1/jquery.slim.min.js"))
        }else if(url.contains("bootstrap.min.js")){
            return WebResourceResponse("application/x-javascript", "utf-8",  context!!. getAssets().open("WebRoot/plugins/bootstrap/4.0.0/js/bootstrap.min.js"))
        }else if(url.contains("bootstrap.min.css")){
            return WebResourceResponse("text/css", "utf-8",  context!!. getAssets().open("WebRoot/plugins/bootstrap/4.0.0/css/bootstrap.min.css"))
        }else if(url.contains("popper.min.js")){
            return WebResourceResponse("application/x-javascript", "utf-8",  context!!. getAssets().open("WebRoot/plugins/popper/1.12.9/popper.min.js"))
        }
        return super.shouldInterceptRequest(view, url)
    }
    @TargetApi(21)
    override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (request.url.toString().contains("jquery.slim.min.js")) {//加载指定.js时 引导服务端加载本地Assets/www文件夹下的cordova.js
                return WebResourceResponse("application/x-javascript", "utf-8",  context!!. getAssets().open("WebRoot/plugins/jquery/3.2.1/jquery.slim.min.js"))
            }else if(request.url.toString().contains("bootstrap.min.js")){
                return WebResourceResponse("application/x-javascript", "utf-8",  context!!. getAssets().open("WebRoot/plugins/bootstrap/4.0.0/js/bootstrap.min.js"))
            }else if(request.url.toString().contains("bootstrap.min.css")){
                return WebResourceResponse("text/css", "utf-8",  context!!. getAssets().open("WebRoot/plugins/bootstrap/4.0.0/css/bootstrap.min.css"))
            }else if(request.url.toString().contains("popper.min.js")){
                return WebResourceResponse("application/x-javascript", "utf-8",  context!!. getAssets().open("WebRoot/plugins/popper/1.12.9/popper.min.js"))
            }
        }
        return super.shouldInterceptRequest(view, request)
    }
}