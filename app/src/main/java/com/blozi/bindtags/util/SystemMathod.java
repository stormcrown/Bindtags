package com.blozi.bindtags.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.GoodsInfoActivity;
import com.blozi.bindtags.activities.LoginActivity;
import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.activities.MainActivityLocal;
import com.blozi.bindtags.activities.fragment.FragmentFactory;
import com.blozi.bindtags.application.Global;
import com.zxing_new.activity.CaptureActivity;

import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blozi.bindtags.util.SystemConstants.Dowmload_APK_REQUESTCODE;
import static com.blozi.bindtags.util.SystemConstants.INSTALL_PACKAGES_REQUESTCODE;

/**
 * Created by 骆长涛 on 2018/3/a.
 */

public class SystemMathod {
//    /**
//     *
//     *
//     * */
//    public static boolean isTagCode(String code) {
//        boolean flag = false;
//        try {
//            if (!isTagCodeTenChar(code)) return false;
//            Integer tagCode_10 = Integer.parseInt(code);
//
//            String tagcode_16 = tagCode_10.toHexString(tagCode_10);
//            int len = tagcode_16.length();
//            /** 只有补一位的情况 */
//            if (len == 7) tagcode_16 = "0" + tagcode_16;
////            else if(len==6)tagcode_16="00"+tagcode_16;
////            if(len<8){
////                int c =8-len;
////                for(int i=0;i<c;i++)tagcode_16="0"+tagcode_16;
////            }
//            if (tagcode_16.length() == 8) {
//                String tag_01 = tagcode_16.substring(0, 2);
//                String tag_23 = tagcode_16.substring(2, 4);
//                String tag_45 = tagcode_16.substring(4, 6);
//                String tag_67 = tagcode_16.substring(6, 8);
//                Integer tag_23_10 = Integer.parseInt(tag_23, 16);
//                Integer tag_67_10 = Integer.parseInt(tag_67, 16);
////                if(Math.abs(tag_23_10-tag_67_10)==1 && tag_01.equals(tag_45)  )flag=true;
//                if (Math.abs(tag_23_10 - tag_67_10) == 1) flag = true; // 不再比较是否相当
//            }
//        } catch (Exception e) {
//
//        } finally {
//            return flag;
//        }
//
//    }

    /**
     * 只要是十位旧可以
     */
    public static boolean isTagCodeTenChar(String code) {
        final String p2 = "[0-9]{10}";
        Pattern pattern2 = Pattern.compile(p2);
        Matcher p2M = pattern2.matcher(code);
        boolean m2B = p2M.matches();
        return m2B;
    }

    public static void startCaptureScan(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT > 22 && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, SystemConstants.CAMERA_OK);
        } else {
            Intent openCameraIntent = new Intent(activity, CaptureActivity.class);
            activity.startActivityForResult(openCameraIntent, requestCode);
        }
    }

    /**
     * 检测并更新
     */
    public static void checkedToUpdate(Boolean showTip, final BLOZIPreferenceManager bloziPreferenceManager, final Activity activity) {
        //   Log.i("系统语言",SystemUtil.getSystemLoacl().toString());
        String versionCode = bloziPreferenceManager.getCommonValue(BLOZIPreferenceManager.key_app_VersionCode);
        if (!TextUtils.isEmpty(versionCode) && TextUtils.isDigitsOnly(versionCode) && Integer.parseInt(versionCode) > SystemUtil.getVersionCode(activity) && !TextUtils.isEmpty(bloziPreferenceManager.getCommonValue(BLOZIPreferenceManager.key_app_APKFileName))) {
            Log.i("SSS", versionCode + "\t" + SystemUtil.getVersionCode(activity));
            String updateContent = BLOZIPreferenceManager.key_app_UpdateContent;
            Locale locale = SystemUtil.getSystemLoacl();
            if ("zh".equalsIgnoreCase(locale.getLanguage())) {
                if ("TW".equalsIgnoreCase(locale.getCountry()))
                    updateContent = BLOZIPreferenceManager.key_app_UpdateContentCNT;
                else updateContent = BLOZIPreferenceManager.key_app_UpdateContentCNS;

            }
            AlertDialog.Builder ab = new AlertDialog.Builder(activity).setTitle(R.string.update)
                    .setMessage(bloziPreferenceManager.getCommonValue(updateContent))
                    .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downLoadAPK(activity);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            ab.create().show();
        } else if (showTip) {
            Toast.makeText(activity, R.string.APPIsTheLasted, Toast.LENGTH_SHORT).show();
        }
    }

    public static void downLoadAPK(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean b = activity.getPackageManager().canRequestPackageInstalls();
            if (!b) {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        }
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Dowmload_APK_REQUESTCODE);
        } else {
            BLOZIPreferenceManager bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(activity);
            SystemConstants.createFileRoot();
            DownloadHelper.INSTANCE.initDownManager(bloziPreferenceManager.getNewApkUrl(), activity, bloziPreferenceManager.getCommonValue(BLOZIPreferenceManager.key_app_APKFileName));
        }
    }

    /**
     * 关闭输入法
     */
    public static void colsedInput(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen)
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输入法
     */
    public static void colsedInput() {
        try {
            colsedInput(Global.currentActivity);
        } catch (Exception e) {

        }
    }

    public static void setEditable(EditText editText, boolean editable) {
        editText.setFocusable(editable);
        editText.setClickable(editable);
        editText.setFocusableInTouchMode(editable);
        editText.setCursorVisible(editable);
    }

    public static LinearLayout getEmptyLine(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120);
//设置边距
        layoutParams.setMargins(0, 0, 0, 0);
//将以上的属性赋给LinearLayout
        linearLayout.setLayoutParams(layoutParams);
//    linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
        return linearLayout;
    }

    /**
     * 退出登陆界面
     */
    public static void logOut(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        FragmentFactory.clear();
    }
//    //加载图片
//    public static File getURLimage(String url) {
//        File file = null;
//        try {
//            Log.i("图片", "图片"+url );
//            URL myurl = new URL(url);
//            // 获得连接
//            Log.i("图片", "图片" );
//            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
//            conn.setConnectTimeout(100000);//设置超时
//            conn.setDoInput(true);
//            conn.setUseCaches(false);//不缓存
//            conn.connect();
//            InputStream is = conn.getInputStream();//获得图片的数据流
//            Log.i("图片", "图片大小：" + is.available() );
//            file = FileUtil.saveFile(is,"TEST",SystemConstants.goodsImageFileRoot);
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return file;
//    }


    // 安装Apk
    public void installApk(Context context,String  filePath) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean b = context.getPackageManager().canRequestPackageInstalls();
                if (!b) {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions(Global.currentActivity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                File file =new File(Uri.parse(filePath).getPath());
                Uri apkUri = FileProvider.getUriForFile(context, "com.blozi.bindtags.fileProvider", file);//在AndroidManifest中的android:authorities值
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ; //= ;
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                context.startActivity(install);
            } else {
                Intent i =new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse("file://$filePath"), "application/vnd.android.package-archive");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);;
                context.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
