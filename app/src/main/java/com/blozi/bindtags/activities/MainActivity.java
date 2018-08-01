package com.blozi.bindtags.activities;

import android.app.AlertDialog;
import android.app.DownloadManager;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blozi.bindtags.R;

import com.blozi.bindtags.activities.fragment.BaseFragment;
import com.blozi.bindtags.adapter.FragmentAdapter;
import com.blozi.bindtags.activities.fragment.FragmentFactory;
import com.blozi.bindtags.activities.fragment.mainTab.BindingTagAndGood;
import com.blozi.bindtags.activities.fragment.mainTab.ImportTags;
import com.blozi.bindtags.application.Global;
import com.blozi.bindtags.asyncTask.online.GetGlobleMessageTask;
import com.blozi.bindtags.asyncTask.online.ShowGoodsInfoTask;
import com.blozi.bindtags.model.RackInfo;
import com.blozi.bindtags.util.BLOZIPreferenceManager;
import com.blozi.bindtags.util.LoadingDialog;
import com.blozi.bindtags.util.ScanUtil;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.SystemMathod;
import com.blozi.bindtags.util.SystemUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zxing_new.activity.CaptureActivity;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.blozi.bindtags.util.SystemConstants.CAMERA_OK;
import static com.blozi.bindtags.util.SystemConstants.CheckUpdate_APK_REQUESTCODE;
import static com.blozi.bindtags.util.SystemConstants.Dowmload_APK_REQUESTCODE;
import static com.blozi.bindtags.util.SystemConstants.INSTALL_PACKAGES_REQUESTCODE;
import static com.blozi.bindtags.util.SystemConstants.WRITE_EXTERNAL_STORAGE_RequestCode;


/**
 * Created by 骆长涛 on 2018/3/a.
 */

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    public String userSession, webServiceUrl, userPass, userName;
    private FragmentAdapter fragmentAdapter;
    private ViewPager page;
    public static float screen_density = 1;
    public static int screen_width = 320;
    public static int screen_height = 480;

    private String currentFragmentName = FragmentFactory.BindingTagAndGood;
    //    private BroadcastReceiver mBrReceiver;
//    private  BaseFragment currentFragmentObject;
    private BLOZIPreferenceManager bloziPreferenceManager;

    //    private BarcodeManager mBarcodeManager;
//    private BarcodeConfig mBarcodeConfig;
    private BottomNavigationBar bottomNavigationBar;
    private ScanUtil scanUtil;
    private int selectedTab=0;
    private BottomNavigationItem[] bottomNavigationItems = new BottomNavigationItem[3];
    //    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private String[] storeNames;
    private String[] storeIds;
    private final List<Map<String, String>> list = new ArrayList<>();
    private DrawerLayout main;
    private NavigationView nav_view;
    private TextView userNameNav, loginId, main_store, current_store_name;
    private Menu left;
    private View navHead;
//    private ImageView background;//,nav_background;
//    private RelativeLayout navHeadRe;
    /**
     * 安卓系统下载类
     **/
    private DownloadManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Log.i("APP", "{android :"+ SystemUtil.getSystemVersion() +",  model:"+ SystemUtil.getSystemModel() +", BRAND:"+SystemUtil.getDeviceBrand() +", IMEI:"+SystemUtil.getIMEI(this) +" }" );
        bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(MainActivity.this);
//        bloziPreferenceManager.showAllMessage();
        webServiceUrl = bloziPreferenceManager.getCompliteURL();
//        setTheme(android.R.style.Theme_Wallpaper_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        scanUtil = new ScanUtil(this);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screen_density = dm.density;
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
        userSession = bloziPreferenceManager.getLoginid();
        userPass = bloziPreferenceManager.getPassword();
        userName = bloziPreferenceManager.getUserName();
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        page = findViewById(R.id.pageFragment);
//        background = findViewById(R.id.background);
//        Glide.with(MainActivity.this).load(SystemConstants.backgroundImags[(int)  Math.floor( Math.random()*SystemConstants.backgroundImags.length)]).into(background);
//        List<BaseFragment> baseFragmentList = new ArrayList<>();
//        baseFragmentList.add(new RackListFragment() );
        fragmentAdapter = new FragmentAdapter(FragmentAdapter.Type.Online,getSupportFragmentManager());
        page.setAdapter(fragmentAdapter);

        nav_view = findViewById(R.id.nav_view);

        navHead = nav_view.getHeaderView(0);


//        navHeadRe = navHead.findViewById(R.id.nav_head_main_re);
        left = nav_view.getMenu();
//        left.findItem(R.id.editGoods).setVisible(bloziPreferenceManager.isAdminPlus());
        userNameNav = navHead.findViewById(R.id.user_name);
        loginId = navHead.findViewById(R.id.login_id);
        main_store = navHead.findViewById(R.id.main_store);
        current_store_name = navHead.findViewById(R.id.current_store_name);


        userNameNav.setText(userName);
        loginId.setText(userSession);


        Global.currentActivity = this;
        Global.mainActivity=this;
//        mBarcodeConfig = new BarcodeConfig(this);
//        mBarcodeManager = new BarcodeManager(this);
//        mBarcodeManager.addListener(new BarcodeListener() {
//            @Override
//            public void barcodeEvent(BarcodeEvent event) {
//                // 当条码事件的命令为“SCANNER_READ”时，进行操作
//                if (event.getOrder().equals(SCANNER_READ)) {
//                    String barcode = mBarcodeManager.getBarcode();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(SystemConstants.scanMsg, barcode);
//                    Message message = new Message();
//                    message.setData(bundle);
//                    message.what = SystemConstants.scanSuccess;
//                    if(handler!=null)handler.sendMessage(message);
//                }
//            }
//
//        });
        bottomNavigationItems = new BottomNavigationItem[]{
                new BottomNavigationItem(R.drawable.bind_32, getResources().getString(R.string.bind_thing)).setActiveColorResource(R.color.colorPrimary),
                new BottomNavigationItem(R.drawable.pullup, getResources().getString(R.string.upload)).setActiveColorResource(R.color.colorPrimary),
                new BottomNavigationItem(R.drawable.grid, getResources().getString(R.string.rackManage)).setActiveColorResource(R.color.colorPrimary),
                new BottomNavigationItem(R.drawable.shrimp, getResources().getString(R.string.CommodityModification)).setActiveColorResource(R.color.colorPrimary)
        };
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setMode(2);
        bottomNavigationBar.setBackgroundStyle(1);
        bottomNavigationBar
                .addItem(bottomNavigationItems[0])
                .addItem(bottomNavigationItems[1]).addItem(bottomNavigationItems[2]).addItem(bottomNavigationItems[3])
                .setFirstSelectedPosition(0)
                .initialise();
        page.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                currentFragmentName = FragmentFactory.getFrameByIndex(i);
                if(i<4){
                    if(fragmentAdapter.getHasExtraFragment()){
                        fragmentAdapter.setHasExtraFragment(false);
                        page.setAdapter(fragmentAdapter);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        bottomNavigationBar.show();
                    }
                    if(page.getCurrentItem()!= i) page.setCurrentItem(i);
                    bottomNavigationBar.show();
                    bottomNavigationBar.selectTab(i);



                }
                else bottomNavigationBar.hide();
//                bottomNavigationBar.performClick();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        bottomNavigationBar.show();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Global.curryentStore.get(Global.STORE_NAME));
        setSupportActionBar(toolbar);
        main = findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, main, toolbar, R.string.auto, R.string.onoff);
//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        });
        main.setScrimColor(0x00000000);
        main.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//                Log.i("DrawerListener",slideOffset+"");
                try {
                    // 得到contentView
                    View content = main.getChildAt(0);
                    float offset =  (drawerView.getWidth() * slideOffset);
                    content.setTranslationX(offset);
//                    InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) { }
            @Override
            public void onDrawerStateChanged(int newState) { }
        });
        main.addDrawerListener(toggle);
        toggle.syncState();
//        toolbar.setNavigationIcon(R.drawable.blozi_logo_mini_b);
        toolbar.setNavigationOnClickListener((v )->{
            try {
                SystemMathod.colsedInput(this);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                main.openDrawer(nav_view);
            }

        });
        toolbar.setTitleTextAppearance(this,R.style.blozi_collapseToolbar_collapsedTitleTextAppearance);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.more_vertical_orange));

        nav_view.setNavigationItemSelectedListener((item)-> {
                return leftMenu(item);
            }
        );
        current_store_name.setOnClickListener( view ->  { dialogChoiceCurrentStore(); });
        Intent intent = new Intent("com.android.service_settings");
        intent.putExtra("pda_sn", "string");//机器码设置成“string”

        this.sendBroadcast(intent);

        try {
            GetGlobleMessageTask getGlobleMessageTask = new GetGlobleMessageTask(this, bloziPreferenceManager.getCompliteURL(),true);
            getGlobleMessageTask.execute("");
//            Glide.with(getApplicationContext())
//                    .load("file:///android_asset/WebRoot/images/15.jpg")
//                    .into(new SimpleTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            nav_view.setBackground(resource);
//                        }
//                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void hideBottomBar(){
        if(bottomNavigationBar!=null)bottomNavigationBar.hide();
    }
    public void showBottomBar(){
        if(bottomNavigationBar!=null)bottomNavigationBar.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        scanUtil.initScan();
        if (Global.currentActivity != MainActivity.this) Global.currentActivity = MainActivity.this;
        if(Global.mainActivity!=this)Global.mainActivity=this;
        if (Global.mainStore == null || Global.mainStore.keySet().size() == 0) {
            Intent intent = new Intent(this, ShowTipsActivity.class);
            startActivity(intent);
            return;
        } else {
            list.clear();
            list.add(Global.mainStore);
            list.addAll(Global.childrenStores);
            storeNames = new String[list.size()];
            storeIds = new String[list.size()];
            Global.currentStoreIndex = 0;
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = list.get(i);
                if (map != null && map.get(Global.STORE_NAME) != null && map.get(Global.STORE_NAME) != null) {
                    storeNames[i] = map.get(Global.STORE_NAME);
                    storeIds[i] = map.get(Global.STOREID);
                }
            }
            main_store.setText(String.format(getResources().getString(R.string.mainStoreFormate), Global.mainStore.get(Global.STORE_NAME)));
            current_store_name.setText(String.format(getResources().getString(R.string.currentStoreFormate), Global.curryentStore.get(Global.STORE_NAME)));
        }

        if (handler == null) handler = new MainActicityHandel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanUtil.stopScan();
        handler = null;
    }

    @Override
    protected void onDestroy() {
        scanUtil.exitScan();
        super.onDestroy();

    }

    public ScanUtil getScanUtil() {
        return scanUtil;
    }

    /**
     * 单选
     */
    private void dialogChoiceCurrentStore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
        builder.setTitle(R.string.changeCurrentStore);
        builder.setIcon(R.drawable.store_primary);
        builder.setSingleChoiceItems(storeNames, Global.currentStoreIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Global.currentStoreIndex = which;
                        Global.curryentStore.put(Global.STOREID, storeIds[which]);
                        Global.curryentStore.put(Global.STORE_NAME, storeNames[which]);
                        toolbar.setTitle(storeNames[which]);
                        current_store_name.setText(String.format(getResources().getString(R.string.currentStoreFormate), Global.curryentStore.get(Global.STORE_NAME)));
                        dialog.dismiss();
                        if (currentFragmentName.equals(FragmentFactory.RackList)) {
                            FragmentFactory.getRackListFragment().reset();
                            bottomNavigationBar.show();
                        }else if(currentFragmentName.equals(FragmentFactory.GOOD_INFO)){
                            FragmentFactory.getGoodInfoManage().initStar(null);
                            bottomNavigationBar.show();
                        }
                    }
                })
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 创建标题菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actvity_main_toobar, menu);
        try {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
//            Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//            m.setAccessible(true);
//            m.invoke(menu, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.changeStore:
                dialogChoiceCurrentStore();
                break;
            case R.id.allowEdit:
                bloziPreferenceManager.setEditable(!bloziPreferenceManager.getEditable());
                for(BaseFragment fragment :FragmentFactory.getAllMainFragment()){
                    try{
                        if(fragment!=null) fragment.editableToggle();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(!bloziPreferenceManager.getEditable())SystemMathod.colsedInput(this);
                break;
            case R.id.scan:
                try {
//                    scanUtil.stopScan(false);
                    if ("DataTerminal".equals(SystemUtil.getDeviceBrand())) Thread.sleep(200L);
                    if( Build.VERSION.SDK_INT>22 &&  ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_OK);
                    }else SystemMathod.startCaptureScan(this, SystemConstants.REQUEST_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.add:
                Intent intent = new Intent(this, RackDetailActivity.class);
                Bundle bundle =new Bundle();
                ArrayList<RackInfo> list = new ArrayList<>();
                list.add(new RackInfo());
                bundle.putParcelableArrayList(SystemConstants.RACK_TRANI,list);
                bundle.putInt(SystemConstants.INDEX_TRANI , 0);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_right,R.anim.out_left);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode){
            case CAMERA_OK:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    SystemMathod.startCaptureScan(this,SystemConstants.REQUEST_CODE);
                }else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    Toast.makeText(MainActivity.this,R.string.PleaseOpenTheCameraPermissionsManually,Toast.LENGTH_SHORT).show();
                }
                break;
            case CheckUpdate_APK_REQUESTCODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    SystemMathod.checkedToUpdate(true,bloziPreferenceManager,this);
                }else {

                }
                    break;
            case Dowmload_APK_REQUESTCODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    SystemMathod.downLoadAPK(this);
                }
                break;
            case INSTALL_PACKAGES_REQUESTCODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    SystemMathod.downLoadAPK(this);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 摄像头扫码结果处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("活动结束", requestCode+"\t"+resultCode+currentFragmentName );
        getCurrentFragmentObject().onActivityResult(requestCode,resultCode,data);
        scanUtil.stopScan(true);
    }
//    public void startCaptureScan(int requestCode){
//        if( Build.VERSION.SDK_INT>22 &&  ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_OK);
//        }else {
//            Intent openCameraIntent = new Intent(this, CaptureActivity.class);
//            startActivityForResult(openCameraIntent,requestCode);
//        }
//    }
    /**
     * 左侧菜单导航
     */
    public boolean leftMenu(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        if (id == R.id.deleteTags) {
//            Date date = new Date();
//            setFragment(FragmentFactory.DELETE_TAGS);
//            Date date1 = new Date();
//            Log.i("测试",date1.getTime() - date.getTime() +"" );
//        }
//        else
            if (id == R.id.upTagOffline) {
            Intent intent = new Intent(this, ScanBarcodeOfflineActivity.class);
            intent.putExtra(ScanBarcodeOfflineActivity.Out,ScanBarcodeOfflineActivity.OutToRight);
            startActivity(intent);
            overridePendingTransition(R.anim.in_right,R.anim.out_left);
        } else if (id == R.id.exit) {
            SystemMathod.logOut(this);
        }
        else if (id == R.id.updateAPP) {
               GetGlobleMessageTask getGlobleMessageTask = new GetGlobleMessageTask(this, bloziPreferenceManager.getCompliteURL(),true);
                getGlobleMessageTask.execute("");
        }
//        else if (id == R.id.testH5) {
//            setFragment(FragmentFactory.WebFragment);
//        }
        main.closeDrawer(GravityCompat.START);
        return true;
    }

    class MainActicityHandel extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SystemConstants.scanSuccess) {
                Bundle bundle = msg.getData();
                String scanMsg = (String) bundle.get(SystemConstants.scanMsg);
//                    Log.i("MainActivity",scanMsg);
                Log.i("主页面：", "接收广播：" + scanMsg);
                Boolean isTag = SystemMathod.isTagCodeTenChar(scanMsg);
                if (TextUtils.isEmpty(scanMsg)) {
                    return;
                }
                if (FragmentFactory.BindingTagAndGood.equals(currentFragmentName)) {
                    BindingTagAndGood bindingTagAndGood = (BindingTagAndGood) getCurrentFragmentObject();
                    if (isTag || bindingTagAndGood.isForceTag() ) {
                        bindingTagAndGood.setTagCodeStr(scanMsg);
                    } else {
                        showLoad(MainActivity.this, R.string.loading);
                        ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(MainActivity.this, webServiceUrl);
                        showGoodsInfoService.execute(userSession, userPass, scanMsg, Global.curryentStore.get(Global.STOREID));
                    }
                } else if (FragmentFactory.IMPORT_TAGS.equals(currentFragmentName) || FragmentFactory.DELETE_TAGS.equals(currentFragmentName)) {
                    ImportTags importTags = (ImportTags) getCurrentFragmentObject();
                    if (isTag || importTags.isForceTag() ) {
                        importTags.addTagId(scanMsg);
                        importTags.accetpMsg(null);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.NotTagcode, Toast.LENGTH_SHORT).show();
                        importTags.accetpMsg(getResources().getString(R.string.NotTagcode) );
                    }
                } else if (FragmentFactory.GOOD_INFO.equals(currentFragmentName)) {
                    showLoad(MainActivity.this, R.string.loading);
                    ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(MainActivity.this, webServiceUrl);
                    showGoodsInfoService.execute(userSession, userPass, scanMsg, Global.curryentStore.get(Global.STOREID), Boolean.TRUE.toString());
                }
            }
        }
    }

    public String getCurrentFragmentName() {
        return currentFragmentName;
    }
    public BaseFragment getCurrentFragmentObject() {
        return FragmentFactory.getFragment(currentFragmentName);
    }
    public void changeToolbarColor(int id){
        if(toolbar!=null)toolbar.setBackgroundColor(getResources().getColor(id));
    }
    public void setFragment(String fragmentName) {
        if(currentFragmentName.equals(fragmentName))return;
        scanUtil.stopScan();
        /** 变更按钮颜色 */
        switch (fragmentName) {
            case FragmentFactory.BindingTagAndGood:
                scanUtil.setRepeat(false);
                bottomNavigationBar.show();
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case FragmentFactory.IMPORT_TAGS:
                bottomNavigationBar.show();
                scanUtil.setRepeat(true);
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case FragmentFactory.DELETE_TAGS:
                bottomNavigationBar.hide();
                scanUtil.setRepeat(true);
                toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                fragmentAdapter.setHasExtraFragment(true);

                page.setAdapter(fragmentAdapter);
                break;
            case FragmentFactory.GOOD_INFO:
                bottomNavigationBar.show();
                scanUtil.setRepeat(false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case FragmentFactory.RackList:
                bottomNavigationBar.show();
                scanUtil.setRepeat(false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case FragmentFactory.WebFragment:
                bottomNavigationBar.hide();
                scanUtil.setRepeat(true);
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fragmentAdapter.setHasExtraFragment(true);
                page.setAdapter(fragmentAdapter);
                break;
        }
        if ((FragmentFactory.GOOD_INFO.equals(fragmentName)) && !bloziPreferenceManager.isAdminPlus()) {
            Toast.makeText(MainActivity.this, R.string.NeedAdmin, Toast.LENGTH_LONG).show();
            return;
        }
        Log.i("Fragment",fragmentName);
        page.setCurrentItem(FragmentFactory.getIndexByName(fragmentName));

        currentFragmentName = fragmentName;
    }


    @Override
    public void onTabSelected(int position) {

        setFragment(FragmentFactory.getFrameByIndex(position));
        selectedTab = position;
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    // 第一次按下返回键的事件
    private long firstPressedTime;

    @Override
    public void onBackPressed() {

        if (FragmentFactory.DELETE_TAGS.equals(currentFragmentName)) {
           // page.setAdapter(fragmentAdapter);
            bottomNavigationBar.selectTab(selectedTab);
        }
        else if (System.currentTimeMillis() - firstPressedTime < 2000) {
            if(LoadingDialog.INSTANCE.isShowing())LoadingDialog.INSTANCE.closeDialog();
            else super.onBackPressed();
        } else {
            Toast.makeText(MainActivity.this, R.string.PressAgainToExit, Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Log.i("按键","\t"+keyCode);
        scanUtil.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        scanUtil.onKeyUp(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 获取接受到的扫描数据,注册广播
     */
    private Handler handler = null;


    public Handler getHandler() {
        return handler;
    }


    /**
     * 打开加载页面
     */
    public void showLoad(Context context, String msg) {
        LoadingDialog.INSTANCE.alertDialog(context,msg);
    }
    public void showLoad(Context context, int msg) {
        LoadingDialog.INSTANCE.alertDialog(context,msg);
    }

    /**
     * 关闭加载页面
     */
    public void closeLoad() {
        LoadingDialog.INSTANCE.closeDialog();
    }

    @Override
    public void finish() {
        FragmentFactory.clear();
        super.finish();
    }
}
