package com.blozi.bindtags.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;

//import com.blozi.blindtags.activities.SendDateToApActivity;
//import com.blozi.blindtags.activities.ShowBindGoodsActivity;
//import com.blozi.blindtags.activities.updateTagsActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by fly_liu on 2017/8/11.
 */

public class SocketUtil {
    protected Activity currentActivity;

    public SocketUtil(Activity sendDateToApActivity) {
        this.currentActivity = sendDateToApActivity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    private static Socket socket;
    private static boolean flag;

    /**
     *
     * @param ip
     * @param port
     * @param info
     * @return
     */
    public void sendDate(String ip, int port, String info) {
        flag = false;
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(ip,port);
        try {
            socket.connect(socketAddress,1000);
            socket.setSoTimeout(3000);
            OutputStream os = socket.getOutputStream();
            os.write(info.getBytes());
            os.flush();
            Thread.sleep(1000);
            String responseInfo = "";
            if(true == socket.isConnected() && false == socket.isClosed()) {
                // 客户端接收服务器端的响应，读取服务器端向客户端的输入流
                InputStream isRead = socket.getInputStream();
                // 缓冲区
                byte[] buffer = new byte[isRead.available()];
                // 读取缓冲区
                isRead.read(buffer);
                // 转换为字符串
                responseInfo = new String(buffer);
            }
            check(responseInfo);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException e) {
            try {
                close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void check(String result) {
        //判断是否成功
        Bundle bundle = new Bundle();
        if(!"".equals(result)&&result!=null){
            bundle.putString("flag","true");
        }
        else{
            bundle.putString("flag","false");
        }
        Message message = new Message();
        message.setData(bundle);
        message.what = 2;//设置线程数
        String reuslt = currentActivity.toString();
//        if(reuslt.contains("SendDateToApActivity")){
//            SendDateToApActivity activity = (SendDateToApActivity) currentActivity;
//            Handler handler = activity.getHandler();
//            handler.sendMessage(message);
//        }
//        else if(result.contains("updateTagsActivity")){
//            updateTagsActivity activity = (updateTagsActivity) currentActivity;
//            Handler handler = activity.getHandler();
//            handler.sendMessage(message);
//        }
    }

    private static void close() throws IOException {
        if(!socket.isOutputShutdown()){
            try {
                socket.shutdownOutput();//这是关闭输出流，完成任务，关闭当前连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!socket.isInputShutdown()){
            try {
                socket.shutdownInput();//关闭输入流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!socket.isClosed()){
            socket.close();//关闭整个socket连接
        }
    }

}
