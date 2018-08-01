package com.blozi.bindtags.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.blozi.bindtags.R
import com.blozi.bindtags.activities.fragment.FragmentFactory
import com.blozi.bindtags.adapter.FragmentRecyclerViewAdapter
import com.blozi.bindtags.adapter.RackDetailPageAdapter
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.asyncTask.online.DeleteRackInfoTask
import com.blozi.bindtags.asyncTask.online.SaveRackInfoTask
import com.blozi.bindtags.model.RackInfo
import com.blozi.bindtags.util.BLOZIPreferenceManager
import com.blozi.bindtags.util.LoadingDialog
import com.blozi.bindtags.util.SystemConstants
import com.blozi.bindtags.util.SystemMathod
import com.blozi.bindtags.view.SlideControllViewPager
import org.json.JSONObject
import java.util.concurrent.BlockingDeque

/**
 *@Author 骆长涛
 */
class RackDetailActivity : BaseActivity {
    constructor() : super()

    companion object {
        val handelEditMsg = 454
        val handlerRackWhat = 89
        val handlerEditResult = 55
        val handlerDeleteResult = 548
    }

    //    private var rack: RecyclerView? = null
    private var mainViewManager: LinearLayoutManager? = null
    private var mainViewAdapter: FragmentRecyclerViewAdapter? = null
    private var toolbar: Toolbar? = null
    private var pressBack = false
    private var page: SlideControllViewPager? = null
    private var pagerAdapter: RackDetailPageAdapter? = null
    private var saveButton: FloatingActionButton? = null
    private var edit = false
    var rackInfo = RackInfo()
    val handler: Handler = RackDeatilHandler()
        get
    var saveItemMenu: MenuItem? = null
    var deleteItemMenu: MenuItem? = null

    internal inner class RackDeatilHandler : Handler {
        constructor() : super()

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val bundle = msg!!.data
            if (msg!!.what == handelEditMsg) {
                val  rackinfo_keys=RackInfo()
                val rackInfoId = bundle.getString(rackinfo_keys.rackInfoId_KEY)
                val rackName = bundle.getString(rackinfo_keys.rackName_KEY)
                val rackCode = bundle.getString(rackinfo_keys.rackCode_KEY)
                val maxLay = bundle.getString(rackinfo_keys.maxLay_KEY)
                val height = bundle.getString(rackinfo_keys.height_KEY)
                val length = bundle.getString(rackinfo_keys.length_KEY)
                val placeLength = bundle.getString(rackinfo_keys.placeLength_KEY)
                val remarks = bundle.getString(rackinfo_keys.remarks_KEY)

                if (!TextUtils.isEmpty(rackInfoId)) rackInfo.rackInfoId = rackInfoId
                if (rackName != null) rackInfo.rackName = rackName
                if (rackCode != null) rackInfo.rackCode = rackCode
                if (!TextUtils.isEmpty(maxLay) && TextUtils.isDigitsOnly(maxLay)) rackInfo.maxLay = maxLay.toInt()
                if (!TextUtils.isEmpty(height) && TextUtils.isDigitsOnly(height)) rackInfo.height = height.toInt()
                if (!TextUtils.isEmpty(length) && TextUtils.isDigitsOnly(length)) rackInfo.length = length.toInt()
                if (!TextUtils.isEmpty(placeLength) && TextUtils.isDigitsOnly(placeLength)) rackInfo.placeLength = placeLength.toInt()
                if (remarks != null) rackInfo.remarks = remarks

            } else if (msg.what == handlerRackWhat) {
                rackInfo = bundle.getParcelable<RackInfo>(SystemConstants.RACK_TRANI)
            } else if (msg.what == handlerEditResult || msg.what == handlerDeleteResult) {
                val isSuccess = bundle.getString("isSuccess")
                val msgStr = bundle.getString("msg")
                if (SystemConstants.IS_EFFECT_YES.equals(isSuccess)) {
                    FragmentFactory.getRackListFragment().reset()
                    pagerAdapter!!.cancelEdit()
                    if (rackInfo.rackInfoId == null || msg.what == handlerDeleteResult) onBackPressed()
                    else rackInfo = RackInfo()
                }
                if (!TextUtils.isEmpty(msgStr)) Toast.makeText(this@RackDetailActivity, msgStr, Toast.LENGTH_LONG).show()

            }

            Log.i("RackInfo", rackInfo.toString())
            if (msg.what == handlerRackWhat || msg.what == handelEditMsg) {
                if (checkRackInfo()) {
                    if (saveButton != null) saveButton!!.visibility = View.VISIBLE
                    if (saveItemMenu != null) saveItemMenu!!.setVisible(true)
                } else {
                    if (saveButton != null) saveButton!!.visibility = View.GONE
                    if (saveItemMenu != null) saveItemMenu!!.setVisible(false)

                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.simple_page)

        initPro()
        initData()
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        Global.currentActivity = this
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (pressBack) overridePendingTransition(R.anim.in_left, R.anim.out_right)
    }

    override fun onBackPressed() {
        pressBack = true
        super.onBackPressed()
    }

    /**
     * 创建标题菜单
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            val inflater = menuInflater
            inflater.inflate(R.menu.rack_detail, menu)
            val menuBuilder = menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            saveItemMenu = menu.findItem(R.id.save)
            saveItemMenu!!.setVisible(false)
            deleteItemMenu = menu.findItem(R.id.delete)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.allowEdit -> {
                if (page != null && pagerAdapter != null) edit = pagerAdapter!!.editToggle(page!!.currentItem)
                page!!.setIsSlideableHorizontal(!edit)
            }
            R.id.save -> {
                tryToSave()
            }
            R.id.view_detail->{
                if(pagerAdapter!=null && page !=null){
                    val intent = Intent(this, RackDetailMapActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable(SystemConstants.RACK_TRANI, pagerAdapter!!.getTheRack(page!!.currentItem))
                    intent.putExtras(bundle)
                    startActivity(intent)
                    overridePendingTransition(R.anim.in_right, R.anim.out_left)
                }
            }
            R.id.delete -> {
                val rac = pagerAdapter!!.getTheRack(page!!.currentItem)
                if (  rac != null && !TextUtils.isEmpty(rac.rackInfoId) ) {
                    val deleteDialog = AlertDialog.Builder(this).setIcon(R.drawable.warning)
                            .setTitle(R.string.delete)
                            .setMessage(R.string.deleteRackTips)
                    var deleteStr: String? = null
                    var cancelStr: String? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        deleteDialog.setPositiveButtonIcon(resources.getDrawable(R.drawable.delete_red_2, null))
                                .setNegativeButtonIcon(resources.getDrawable(R.drawable.cancel_primay, null))
                    } else {
                        deleteStr = resources.getString(R.string.delete)
                        cancelStr = resources.getString(R.string.cancel)
                    }
                    deleteDialog.setPositiveButton(deleteStr, { _, _ ->
                            val bloziPreferenceManager = BLOZIPreferenceManager.getInstance(this@RackDetailActivity)
                            val deleteRackInfoTask = DeleteRackInfoTask(this@RackDetailActivity, bloziPreferenceManager!!.compliteURL)
                            LoadingDialog.alertDialog(this@RackDetailActivity)
                            deleteRackInfoTask.execute(Global.curryentStore[Global.STOREID], rac!!.rackInfoId)
                    })
                            .setNegativeButton(cancelStr, { d, w -> })
                    deleteDialog.create().show()
                }else{
                    Toast.makeText(this@RackDetailActivity,R.string.Thecurrentshelfisnotyetsaved,Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onContextItemSelected(item)
    }


    private fun initPro() {
        val racks_TRANI = intent.getParcelableArrayListExtra<RackInfo>(SystemConstants.RACKS_TRANI)
        val rack_TRANI = intent.getParcelableArrayListExtra<RackInfo>(SystemConstants.RACK_TRANI)
        val index = intent.getIntExtra(SystemConstants.INDEX_TRANI, 0)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar!!.setNavigationIcon(R.drawable.pull_left_orange)
        page = findViewById(R.id.page)
        if (racks_TRANI != null && racks_TRANI.size > 0) {
            pagerAdapter = RackDetailPageAdapter(racks_TRANI)
        } else if (rack_TRANI != null && rack_TRANI.size == 1) {
            pagerAdapter = RackDetailPageAdapter(rack_TRANI)
            edit = pagerAdapter!!.edit(index)
        }
        page!!.adapter = pagerAdapter
        page!!.currentItem = index
        saveButton = findViewById(R.id.float_save)
    }

    private fun initData() {
        toolbar!!.setTitle(R.string.rackDetail)
        toolbar!!.setNavigationOnClickListener { v ->
            try {
                SystemMathod.colsedInput(this@RackDetailActivity)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                onBackPressed()
            }
        }
        page!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {
            }

            override fun onPageSelected(i: Int) {
                edit = pagerAdapter!!.cancelEdit()
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
        saveButton!!.setOnClickListener { view: View? ->
            tryToSave()
        }

    }

    private fun tryToSave() {
        if (rackInfo.maxLay == null || rackInfo.placeLength == null) {
            Toast.makeText(this@RackDetailActivity, R.string.TheMaxLayersAndTheLongestShowcaseCannotbeempty, Toast.LENGTH_SHORT).show()
            return
        }
        val js = JSONObject()
        val rackInfo_key = RackInfo()
        if (!TextUtils.isEmpty(rackInfo.rackInfoId)) js.put(rackInfo_key.rackInfoId_KEY, rackInfo.rackInfoId)
        if (!TextUtils.isEmpty(rackInfo.rackName)) js.put(rackInfo_key.rackName_KEY, rackInfo.rackName)
        if (!TextUtils.isEmpty(rackInfo.rackCode)) js.put(rackInfo_key.rackCode_KEY, rackInfo.rackCode)
        if (rackInfo.maxLay != null) js.put(rackInfo_key.maxLay_KEY, rackInfo.maxLay)
        if (rackInfo.placeLength != null) js.put(rackInfo_key.placeLength_KEY, rackInfo.placeLength)
        if (rackInfo.height != null) js.put(rackInfo_key.height_KEY, rackInfo.height)
        if (rackInfo.length != null) js.put(rackInfo_key.length_KEY, rackInfo.length)
        if (!TextUtils.isEmpty(rackInfo.remarks)) js.put(rackInfo_key.remarks_KEY, rackInfo.remarks)
        val bloz = BLOZIPreferenceManager.getInstance(this@RackDetailActivity)
        val savRackInfo = SaveRackInfoTask(this@RackDetailActivity, bloz!!.compliteURL)
        LoadingDialog.alertDialog(this@RackDetailActivity)
        savRackInfo.execute(Global.curryentStore[Global.STOREID], js.toString())
    }

    private fun checkRackInfo(): Boolean {
        if (rackInfo.maxLay != null || rackInfo.placeLength != null) {
            return true
        }
        return false
    }

}