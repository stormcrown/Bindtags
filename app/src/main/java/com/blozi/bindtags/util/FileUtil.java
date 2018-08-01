package com.blozi.bindtags.util;

//import com.sun.istack.internal.NotNull;
import android.util.Base64;

import java.io.*;
import java.util.List;

/**
 * @aothor 骆长涛
 * @datetime 2018/2/a 12:33
 **/
public class FileUtil {
    /**临时文件*/
    public static File saveFile(InputStream inputStream, String newFileName , String newDicory)throws IOException {
        byte [] file = new byte[inputStream.available()];
        inputStream.read(file);
        inputStream.close();
        return saveFile(file,newFileName,newDicory);
    }
    /** 复制文件 **/
    public static File copyFile(File original,String newDictory,String newFileName )throws IOException{
        InputStream inputStream = new FileInputStream(original);
        byte [] file = new byte[inputStream.available()];
        inputStream.read(file);
        inputStream.close();
        return  saveFile(file,newFileName,newDictory);
    }
    public static File saveFile(byte[] file, String newFileName , String newDicory)throws IOException {
        /* 创建新文件夹 */
        File dir = new File(newDicory);
        if(!dir.exists() && !dir.isDirectory() )dir.mkdirs();
        /* 创建新空白文件 */
        File newFile = new File(newDicory+"/"+newFileName);
        if(!newFile.exists())newFile.createNewFile();
        OutputStream os = new FileOutputStream(newFile);
        os.write(file);
        os.flush();
        os.close();
        return newFile;
    }
    public static String encodeBase64File(InputStream inputFile) throws IOException {
        byte[] buffer = new byte[inputFile.available()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }
    public static String encodeBase64File(byte[] buffer) {
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }
    public static byte[] decoderBase64File(String base64Code){
            return Base64.decode(base64Code,Base64.DEFAULT);
    }

    public static boolean deleteDir(File dir) {
        if(dir==null)return true;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static boolean deleteDir(List<String> dirs){
        for (String str:dirs){
            File dir = new File(str);
            if(dir.exists() )deleteDir(dir);
        }
        return true;
    }
    public static boolean deleteDir(String dirs){
        return deleteDir(new File(dirs));

    }


}
