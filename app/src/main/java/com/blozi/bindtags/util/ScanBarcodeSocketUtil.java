package com.blozi.bindtags.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.blozi.bindtags.activities.ScanBarcodeOfflineActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by fly_liu on 2017/8/11.
 */

public class ScanBarcodeSocketUtil {
    public static final int Check =778;
    protected ScanBarcodeOfflineActivity currentActivity;

    public ScanBarcodeSocketUtil(ScanBarcodeOfflineActivity mainActivity) {
        this.currentActivity = mainActivity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(ScanBarcodeOfflineActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public static Socket socket = null;
    private static boolean flag;
    private String date = "$AA#";

    /**
     *
     * @param ip
     * @param port
     * @param info
     * @return
     */
    public void sendDate(String ip, int port, String info) {
        flag = false;
        if(socket==null){
            try {
                socket = new Socket(ip,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(socket!=null){
            try {
                socket.setSoTimeout(3000);
                while (true){
                    try{
                        if(true == socket.isConnected() && false == socket.isClosed()) {
                            OutputStream os = socket.getOutputStream();
                            if(!"".equals(info)&&info!=null){
                                os.write(info.getBytes());
                                info = "";
                                os.flush();
                                // 客户端接收服务器端的响应，读取服务器端向客户端的输入流
                                InputStream isRead = socket.getInputStream();
                                // 缓冲区
                                byte[] buffer = new byte[isRead.available()];
                                // 读取缓冲区
                                isRead.read(buffer);
                                // 转换为字符串
                                String responseInfo = new String(buffer);
                                System.out.println("查看返回值:------------"+responseInfo);
                            }
                        }
                        else{
                            close();
                            check("连接失败");
                            break;
                        }
                    } catch (Exception e){
                        try{
                            close();
                            check("连接失败");
                        } catch (Exception e1){
                            e1.printStackTrace();
                            check("连接失败");
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    close();
                    check("连接失败");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    check("连接失败");
                }
            }
        }
        else{
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
                check("连接失败");
            }
            check("连接失败");
        }
        try {
            close();
            check("连接失败");
        } catch (IOException e) {
            e.printStackTrace();
            check("连接失败");
        }
    }

    private static void close() throws IOException {
        try{
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
        } catch (Exception e){
            e.printStackTrace();
        }
        socket = null;
    }

    private void check(String result) {
        Bundle bundle = new Bundle();
        bundle.putString("flag",result);
        Message message = new Message();
        message.setData(bundle);
        message.what = Check;//设置线程数
        Handler handler = currentActivity.getHandler();
        handler.sendMessage(message);
    }

}