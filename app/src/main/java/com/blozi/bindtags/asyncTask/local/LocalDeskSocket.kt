package com.blozi.bindtags.asyncTask.local

import android.text.TextUtils
import android.util.Log
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

class LocalDeskSocket {
    enum class RequestCode {
        Heartbeat, Login, GetGoodsName, UploadTagAndGoodsCode, GetGoodsInfo, UpdateGoodsInfo
    }

    companion object {
        var loginMsg = ""
            set
        val specialDateFormate = SimpleDateFormat("yyyyMMddHHmmss")
        val outTime = "超时"
        var IPNOTRight = "IP及端口不正确"
        val msgHead = "FAFAFA"
        val msgEnd = "FCFCFC"
        val msgSplit = "_"
        val msgSplitReplace = "!#%&*^\$@"
        val y = "y"
        val n = "n"
        @Volatile
        var me: LocalDeskSocket? = null
            private set
            get
        @Volatile
        private var net: Socket? = null
        //        private var context: Context?=null
        private var url: String = ""
        private var ip: String = ""
        private var port: Int = 8888
        @Volatile
        private var errorMsg = ""
        @Volatile
        private var keepAllive: KeepNetAliveThread? = null

        fun getInstance(url: String): LocalDeskSocket? {
            closeAll()
            try {
                me = LocalDeskSocket(url)
//                val str =
            } catch (e: Exception) {
                e.printStackTrace();
                errorMsg = e.localizedMessage
                return null
            }
//            keepAllive = KeepNetAliveThread(getRequestCode(RequestCode.Heartbeat ,specialDateFormate.format(Date()) ), url)
            keepAllive = KeepNetAliveThread(url)
            keepAllive!!.start()
            return me
        }

        fun sleepKeepAlive() {
            keepAllive!!.sleep = true
        }

        fun closeAll() {
            try {
                if (keepAllive != null) {
                    keepAllive!!.exit = true
                    //keepAllive!!.join()
                }
                if (net != null) {
                    net!!.close()
                }
                net = null
                me = null
                keepAllive = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getRequestCode(request: RequestCode, vararg params: String?): String {
            when (request) {
                RequestCode.Heartbeat ->
                    return StringBuffer(msgHead).append(msgSplit)
                            .append("0").append(msgSplit)
                            .append(y).append(msgSplit)
                            .append(params[0]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(msgEnd).toString()
                RequestCode.Login ->
                    return StringBuffer(msgHead).append(msgSplit)
                            .append("1").append(msgSplit)
                            .append(y).append(msgSplit)
                            .append(params[0]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(params[1]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(params[2]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            //   .append(SystemUtil.getDeviceId(context)!!.replace(msgSplit,msgSplitReplace)).append(msgSplit)
                            .append(msgEnd).toString()
                RequestCode.GetGoodsName -> //return "72"
                    return StringBuffer(msgHead).append(msgSplit)
                            .append("2").append(msgSplit)
                            .append(y).append(msgSplit)
                            .append(params[0]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(msgEnd).toString()
                RequestCode.UploadTagAndGoodsCode ->
                    return StringBuffer(msgHead).append(msgSplit)
                            .append("3").append(msgSplit)
                            .append(y).append(msgSplit)
                            .append(params[0]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(params[1]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(msgEnd).toString()
                RequestCode.GetGoodsInfo ->
                    return StringBuffer(msgHead).append(msgSplit)
                            .append("4").append(msgSplit)
                            .append(y).append(msgSplit)
                            .append(params[0]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(msgEnd).toString()
                RequestCode.UpdateGoodsInfo ->
                    return StringBuffer(msgHead).append(msgSplit)
                            .append("5").append(msgSplit)
                            .append(y).append(msgSplit)
                            .append(params[0]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(params[1]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(params[2]!!.replace(msgSplit, msgSplitReplace)).append(msgSplit)
                            .append(msgEnd).toString()
            }
        }

        fun getRequestCode(int: Int): RequestCode? {
            when (int) {
                70 -> return RequestCode.Heartbeat
                71 -> return RequestCode.Login
                72 -> return RequestCode.GetGoodsName
                73 -> return RequestCode.UploadTagAndGoodsCode
                74 -> return RequestCode.GetGoodsInfo
                75 -> return RequestCode.UpdateGoodsInfo
            }
            return null
        }
    }


    private constructor(url0: String) {
        Log.i("d", url0)
        if (!url.equals(url0) || net == null || net!!.isClosed) {
            url = url0
            var list = url0.split(":")
            if (list.size == 2) {
                ip = list[0]
                if (TextUtils.isDigitsOnly(list[1])) port = list[1].toInt()
                else {
                    //Toast.makeText(context,"端口不正确",Toast.LENGTH_LONG).show()
                    throw Exception("IP及端口不正确")
                }

                try {
                    Log.i("d", ip + "\t" + port)

                    if (net != null) {
                        net!!.close()
                        net = null
                    }
                    net = Socket(ip, port)
                    net!!.keepAlive = true
                    net!!.soTimeout = 30000;
                    me = this
                } catch (e: Exception) {
                    Log.i("d", "连接错误")
                    e.printStackTrace()
                    throw Exception("连接错误,无法连接到服务器")
                }
            } else {
                //  Toast.makeText(context,"IP及端口不正确",Toast.LENGTH_LONG).show()
                throw Exception("IP及端口不正确")
            }
        }


    }

    @Synchronized
    fun sendMessage(msg: String): String {
        var lock = Any()
        synchronized(lock) {
            var send = ""
            net!!.soTimeout = 3000;
            if (net!!.isConnected && !net!!.isClosed) {
                var os = net!!.getOutputStream()
                Log.i("连接成功", msg)
                os.write(msg.toByteArray())
                os.flush()
            }
            var star = Date()
            while (true) {
                if (net!!.isConnected && !net!!.isClosed) {
                    var ins = net!!.getInputStream()
                    if (ins.available() > 0) {
//                           Log.i("连接成功", "响应" + ins.available())
                        var bytes = ByteArray(ins!!.available())
                        ins.read(bytes)
                        send = String(bytes)
                        Log.i("接收成功", send)
                        break
                    } else {
                        var end = Date()
                        if (end.time - star.time > 5000) {
                            send = outTime
                            break
                        } else {
                            Thread.sleep(100)
                            continue
                        }
                    }
                } else {
                    //Log.i("连接失败", "faild")
                    break
                }

            }
            return send
        }

    }

    class KeepNetAliveThread : Thread {
//        private var keep = ""
        private var url = ""
        var exit = false
        var sleep = false

        constructor() : super()
//        constructor(keep: String, url: String) {
//            this.keep = keep
//            this.url = url
//        }

        constructor(url: String) {
            this.url = url
        }

        override fun run() {
            //Thread.sleep(5*1000)
            while (!exit) {
                try {
                    if (net != null && net!!.isConnected && !net!!.isClosed && !net!!.isOutputShutdown && me != null) {
                        var respon = LocalDeskSocket.me!!.sendMessage(getRequestCode(RequestCode.Heartbeat ,specialDateFormate.format(Date()) ))
                        Log.i("心跳", respon)
                    } else {
                        me = getInstance(url)
                    }
                    Thread.sleep(10 * 1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (net != null) {
                        net!!.close()
                    }
                    net = null
                    me = null
                }
            }
            if (sleep) {
                Thread.sleep(10 * 1000)
                sleep = false
            }
            // super.run()
        }
    }

}