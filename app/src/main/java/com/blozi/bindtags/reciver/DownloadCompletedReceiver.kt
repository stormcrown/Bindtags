package com.blozi.bindtags.reciver

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log

import com.blozi.bindtags.util.DownloadHelper
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import java.io.File
import android.Manifest.permission
import android.Manifest.permission.REQUEST_INSTALL_PACKAGES
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import com.blozi.bindtags.activities.MainActivity
import com.blozi.bindtags.activities.MainActivityLocal
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.util.SystemConstants


class DownloadCompletedReceiver : BroadcastReceiver() {


    // 安装下载接收器
    override fun onReceive(context: Context, intent: Intent) {
        // Log.i(TAG,"收到广播");
        val manager = DownloadHelper.manager ?: return
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
            val query = DownloadManager.Query()
            //在广播中取出下载任务的id
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
            query.setFilterById(id)
            val c = manager.query(query)
            if (c.moveToFirst()) {
                //获取文件下载路径
                val fileUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                val resolver = context.contentResolver
//                resolver.openFileDescriptor(Uri.parse(fileUri),"APK")
//                val fileNameIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME)
//                val filename = c.getString(fileNameIdx)
                //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                if (fileUri != null) {
                    try {
                        installApk(context, fileUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED == intent.action) {
            val ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS)
            //点击通知栏取消下载
            //            manager.remove(ids);

        }

    }

    // 安装Apk
    private fun installApk(context: Context, uri: Uri) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.setDataAndType(uri, "application/vnd.android.package-archive")
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(i)
        } catch (e: Exception) {
            Log.e(TAG, "安装失败")
            e.printStackTrace()
        }
    }

    // 安装Apk
    private fun installApk(context: Context, filePath: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val b = context.getPackageManager().canRequestPackageInstalls()
                if (!b) {
                    val bundle = Bundle()
                    bundle.putString(SystemConstants.DownloadApk, filePath)
                    val message = Message()
                    message.data = bundle
                    message.what = SystemConstants.Dowmload_APK_REQUESTCODE
                    var handler: Handler? = null
                    if (Global.currentActivity != null && Global.currentActivity is MainActivity) {
                        val activity0 = Global.currentActivity as MainActivity?
                        handler = activity0!!.handler
                    }else if (Global.currentActivity != null && Global.currentActivity is MainActivityLocal) {
                        val activity0 = Global.currentActivity as MainActivityLocal?
                        handler = activity0!!.handler
                    }
                    if(handler!=null){
                        handler.handleMessage(message)
                    }

                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val file = File(Uri.parse(filePath).path)
                val apkUri = FileProvider.getUriForFile(context, "com.blozi.bindtags.fileProvider", file)//在AndroidManifest中的android:authorities值
                val install = Intent(Intent.ACTION_VIEW)
                install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                install.setDataAndType(apkUri, "application/vnd.android.package-archive")
                context.startActivity(install)
            } else {
                val i = Intent(Intent.ACTION_VIEW)
                i.setDataAndType(Uri.parse("file://$filePath"), "application/vnd.android.package-archive")
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(i)
            }
        } catch (e: Exception) {
            Log.e(TAG, "安装失败")
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = "DownloadCompletedReceiv"
    }
}
