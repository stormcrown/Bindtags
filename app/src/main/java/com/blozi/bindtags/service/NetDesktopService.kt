//package com.blozi.blindtags.service
//
//import android.annotation.TargetApi
//import android.app.Notification
//import android.app.NotificationManager
//import android.app.Service
//import android.content.Intent
//import android.content.res.Configuration
//import android.os.Binder
//import android.os.IBinder
//import android.util.Log
//import com.blozi.blindtags.R
//import java.io.FileDescriptor
//import java.io.PrintWriter
//import com.blozi.blindtags.R.mipmap.ic_launcher
//import com.blozi.blindtags.asyncTask.local.LocalDeskSocket
//import com.blozi.blindtags.asyncTask.local.LocalNetBaseTask
//import com.blozi.blindtags.util.BLOZIPreferenceManager
//
//
//class NetDesktopService:Service {
//    private val TAG:String="NetDesktopService"
//    private var  notificationManager: NotificationManager?=null
//    private val binder:Binder = Binder()
//    var notification : Notification.Builder?=null
//
//    private val notifId = TAG.hashCode()
//    protected var bloziPreferenceManager: BLOZIPreferenceManager? = null
//    constructor() : super()
//
//    override fun onCreate() {
//        notificationManager =  getSystemService(NOTIFICATION_SERVICE) as NotificationManager;
//        notification = Notification.Builder(this)
//                .setSmallIcon(R.drawable.blozi_dark)//设置小图标
//                .setContentTitle( getString(R.string.DesktopService) )
//                .setContentText("none")
//
//        notificationManager!!.notify(notifId ,notification!!.build())
//        Log.i(TAG, "onCreate方法被调用!")
//        bloziPreferenceManager = BLOZIPreferenceManager.getInstance(this)
//        var localNetBaseTask = LocalNetBaseTask(this);
//                       // localNetBaseTask.execute(user_name,password, SystemUtil.getDeviceId(context));
//        super.onCreate()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//    }
//    override fun onBind(intent: Intent?): IBinder {
//        Log.i(TAG, "onbind方法被调用!")
//
//        return binder
//    }
//    //Service断开连接时回调
//    override fun onUnbind(intent: Intent): Boolean {
//        Log.i(TAG, "onUnbind方法被调用!")
//        return true
//    }
//
//    //Service被启动时调用
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        Log.i(TAG, "onStartCommand方法被调用!")
//
//
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//}