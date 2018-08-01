package com.blozi.bindtags.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log


import android.content.Context.DOWNLOAD_SERVICE
import android.os.Environment

object DownloadHelper {
    /** 安卓系统下载类  */
    var manager: DownloadManager? = null
        private set

    /** 初始化下载器  */
    fun initDownManager(url: String, context: Context?, fileName: String) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(fileName) || context == null) {
            Log.i("Error", "文件名、下载路径、上下文都不能为空。")
            return
        }
        manager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        //设置下载地址
        val down = DownloadManager.Request(Uri.parse(url))
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        // 显示下载界面
        down.setVisibleInDownloadsUi(true)
        //在通知栏显示下载进度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            down.allowScanningByMediaScanner()
            // 下载时，通知栏显示途中，下载完成后通知
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            // 下载时，通知栏显示途中， 下载完成后通知消失
            //            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }

        // 设置下载后文件存放的位置，目录必须已经存在，否则会报错
        down.setDestinationInExternalPublicDir(SystemConstants.Public_TEMP_DIR, fileName)
//        down.setDestinationInExternalPublicDir(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).absolutePath, fileName)
        // 将下载请求放入队列
        manager!!.enqueue(down)


    }
}
