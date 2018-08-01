package com.blozi.bindtags.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem

import com.blozi.bindtags.R
import com.blozi.bindtags.activities.fragment.BaseFragment
import com.blozi.bindtags.activities.fragment.FragmentFactory
import com.blozi.bindtags.activities.fragment.mainTab.BindingTagAndGood
import com.blozi.bindtags.adapter.FragmentAdapter
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.asyncTask.local.LocalDeskSocket
import com.blozi.bindtags.asyncTask.local.LocalNetBaseTask
import com.blozi.bindtags.util.*
import com.blozi.bindtags.util.SystemConstants.*
import com.zxing_new.activity.CaptureActivity

class MainActivityLocal : BaseActivity(), BottomNavigationBar.OnTabSelectedListener {

    private var activityMain: DrawerLayout? = null
    private var bottomNavigationBar: BottomNavigationBar? = null
    private var bottomNavigationItems: Array<BottomNavigationItem>? = null
    private var bloziPreferenceManager: BLOZIPreferenceManager? = null
    var scanUtil: ScanUtil? = null
        private set
        get
    private var currentFragmentName = FragmentFactory.BindingTagAndGoodLocal
        get
    private var toolbar: Toolbar? = null
    //    private var fragmentContent:FrameLayout?=null
    private var fragmentAdapter: FragmentAdapter? = null
    private var page: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_local)
        activityMain = findViewById(R.id.activity_main)
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        scanUtil = ScanUtil(this)
        bloziPreferenceManager = BLOZIPreferenceManager.getInstance(this@MainActivityLocal)
        toolbar = findViewById(R.id.toolbar)
        page = findViewById(R.id.pageFragment)
//        fragmentContent = findViewById(R.id.content)
//        fragmentContent!!.visibility=View.VISIBLE
        toolbar!!.setTitle(StringBuffer(resources.getString(R.string.app_name)).append("-").append(resources.getString(R.string.Desktop)))
        setSupportActionBar(toolbar)
        toolbar!!.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.more_vertical_orange))
        bottomNavigationItems = arrayOf(
                BottomNavigationItem(R.drawable.bind_32, resources.getString(R.string.bind_thing)).setActiveColorResource(R.color.colorAccent),
                BottomNavigationItem(R.drawable.shrimp, resources.getString(R.string.CommodityModification)).setActiveColorResource(R.color.orange)
        )
        bottomNavigationBar!!.setTabSelectedListener(this)
        bottomNavigationBar!!.setMode(2)
        bottomNavigationBar!!.setBackgroundStyle(2)
        bottomNavigationBar!!
                .addItem(bottomNavigationItems!![0])
                .addItem(bottomNavigationItems!![1])
                .setFirstSelectedPosition(0)
                .initialise()
        bottomNavigationBar!!.show()

//        setFragment(currentFragmentName)
        fragmentAdapter = FragmentAdapter(FragmentAdapter.Type.Local, supportFragmentManager)
        page!!.setAdapter(fragmentAdapter)
        page!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                currentFragmentName = FragmentFactory.getLocalFrameByIndex(i)
                bottomNavigationBar!!.show()
                bottomNavigationBar!!.selectTab(i)
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    /**
     * 摄像头扫码结果处理
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("活动结束Local", requestCode.toString() + "\t" + resultCode + currentFragmentName)
        getCurrentFragmentObject().onActivityResult(requestCode, resultCode, data)
        scanUtil!!.stopScan()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_OK ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SystemMathod.startCaptureScan(this, SystemConstants.REQUEST_CODE)
                } else {
                    Toast.makeText(this@MainActivityLocal, R.string.PleaseOpenTheCameraPermissionsManually, Toast.LENGTH_SHORT).show()
                }
            READ_PHONE_STATE_RequestCode -> {

            }
            WRITE_EXTERNAL_STORAGE_RequestCode ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SystemMathod.checkedToUpdate(true, bloziPreferenceManager, this)
                } else {

                }
            else -> {
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // Log.i("按键","\t"+keyCode);
        scanUtil!!.onKeyDown(keyCode, event)
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        scanUtil!!.onKeyUp(keyCode, event)
        return super.onKeyUp(keyCode, event)
    }

    fun setFragment(fragmentName: String) {
        scanUtil!!.stopScan()
        /** 变更按钮颜色  */
        when (fragmentName) {
            FragmentFactory.BindingTagAndGoodLocal -> {
                ScanUtil.setRepeat(false)
                bottomNavigationBar!!.show()
            }
            FragmentFactory.GOOD_INFO -> {
                bottomNavigationBar!!.show()
                ScanUtil.setRepeat(false)
            }
        }
        currentFragmentName = fragmentName
    }

    override fun onResume() {
        super.onResume()
        ScanUtil.initScan()
        Global.currentActivity = this
        Global.mainActivity = this
    }

    override fun onPostResume() {
        super.onPostResume()
        val localNetBaseTask = LocalNetBaseTask(this)
        localNetBaseTask.execute(LocalDeskSocket.getRequestCode(LocalDeskSocket.RequestCode.Login, bloziPreferenceManager!!.localUserName, bloziPreferenceManager!!.localPassword, SystemUtil.getDeviceId(this)))
    }

    override fun onPause() {
        super.onPause()
        scanUtil!!.exitScan()
        Global.currentActivity = null
//        LocalDeskSocket.closeAll();
    }

    override fun onStop() {
//        LocalDeskSocket.sleepKeepAlive();

        super.onStop()
    }

    override fun onDestroy() {
        LocalDeskSocket.closeAll();
        super.onDestroy()
    }

    /**
     * 创建标题菜单
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actvity_main_local_toobar, menu)
        try {
            val menuBuilder = menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
//            val m = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
//            m.isAccessible = true
//            m.invoke(menu, true)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                SystemMathod.logOut(this)
            }
            R.id.allowEdit -> {
                bloziPreferenceManager!!.editable = !bloziPreferenceManager!!.editable
                getCurrentFragmentObject().editableToggle()
                if (!bloziPreferenceManager!!.editable) SystemMathod.colsedInput(this)
            }
            R.id.upTagOffline -> {
                val intent = Intent(this, ScanBarcodeOfflineActivity::class.java)
                intent.putExtra(ScanBarcodeOfflineActivity.Out, ScanBarcodeOfflineActivity.OutTop)
                startActivity(intent)
                overridePendingTransition(R.anim.in_top, R.anim.out_bottom)
            }
            R.id.scan ->
                try {
                    if ("DataTerminal" == SystemUtil.getDeviceBrand()) Thread.sleep(200L)
                    if (Build.VERSION.SDK_INT > 22 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_OK)
                    } else
                        SystemMathod.startCaptureScan(this, SystemConstants.REQUEST_CODE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
        }
        return super.onOptionsItemSelected(item)
    }

    // 第一次按下返回键的事件
    private var firstPressedTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed()
        } else {
            Toast.makeText(this@MainActivityLocal, R.string.PressAgainToExit, Toast.LENGTH_SHORT).show()
            firstPressedTime = System.currentTimeMillis()
        }
    }

    fun getCurrentFragmentObject(): BaseFragment {
        return FragmentFactory.getFragment(currentFragmentName)
    }

    internal inner class MainActicityLocalHandel : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == SystemConstants.scanSuccess) {
                val bundle = msg.data
                val scanMsg = bundle.get(SystemConstants.scanMsg) as String
                //                    Log.i("MainActivity",scanMsg);
                Log.i("主页面：", "接收广播：$scanMsg")
//                val isTag = SystemMathod.isTagCode(scanMsg)
                val isTag = SystemMathod.isTagCodeTenChar(scanMsg)
                if (TextUtils.isEmpty(scanMsg)) return

                if (FragmentFactory.BindingTagAndGoodLocal.equals(currentFragmentName)) {
                    val bindingTagAndGood = getCurrentFragmentObject() as BindingTagAndGood
                    if (isTag || bindingTagAndGood.isForceTag) {
                        bindingTagAndGood.setTagCodeStr(scanMsg)
                    } else {
                        LoadingDialog.alertDialog(this@MainActivityLocal)
                        val localNetBaseTask = LocalNetBaseTask(this@MainActivityLocal)
                        localNetBaseTask.execute(LocalDeskSocket.getRequestCode(LocalDeskSocket.RequestCode.GetGoodsName, scanMsg), scanMsg)
                    }
                } else if (FragmentFactory.GOOD_INFO_Local.equals(currentFragmentName)) {
//                    LoadingDialog.alertDialog(this@MainActivityLocal)
                    val localNetBaseTask = LocalNetBaseTask(this@MainActivityLocal)
                    localNetBaseTask.execute(LocalDeskSocket.getRequestCode(LocalDeskSocket.RequestCode.GetGoodsInfo, scanMsg), scanMsg)
                }

            }
        }
    }

    /**
     * 获取接受到的扫描数据,注册广播
     */
    var handler: Handler = MainActicityLocalHandel()
        private set
        get

    override fun onTabSelected(position: Int) {
        page!!.setCurrentItem(position, true)
        currentFragmentName = FragmentFactory.getLocalFrameByIndex(position)
    }

    override fun onTabUnselected(position: Int) {

    }

    override fun onTabReselected(position: Int) {
        if (getCurrentFragmentObject() is BindingTagAndGood) (getCurrentFragmentObject() as BindingTagAndGood).toBingd()

    }

    override fun finish() {
        FragmentFactory.clear()
        super.finish()
    }

}
