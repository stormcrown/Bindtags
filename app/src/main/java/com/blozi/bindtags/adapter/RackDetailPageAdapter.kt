package com.blozi.bindtags.adapter

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.view.PagerAdapter
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.blozi.bindtags.R
import com.blozi.bindtags.activities.RackDetailActivity
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.model.RackInfo
import com.blozi.bindtags.util.SystemConstants
import com.blozi.bindtags.util.SystemMathod
import com.bumptech.glide.Glide
import java.util.*


class RackDetailPageAdapter : PagerAdapter {
    private val bundle = Bundle()
    private val message = Message()

    constructor() : super()
    constructor(list: List<RackInfo>?) : super() {
        racks.addAll(list!!)
    }

    fun addRacks(list: List<RackInfo>?) {
        racks.addAll(list!!)
    }

    private var racks = ArrayList<RackInfo>()
    public fun getTheRack(position: Int):RackInfo{
        return racks[position]
    }

    private var editRack: RackInfo? = null
    private var editable = false
    private var editIndex = 0

    override fun getCount(): Int {
        return racks.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    /**
     * 当执行 notifyDataSetChanged(); 方法时，
     * 先执行 getItemPosition
     * 如果返回 PagerAdapter.POSITION_NONE 再执行 instantiateItem
     * 基于PageView 的缓存机制，前后页也会执行一遍这个方法
     * */
    fun editToggle(position: Int): Boolean {
        editable = !editable
        editIndex = position
        toEdit()
        return editable
    }

    fun cancelEdit(): Boolean {
        editable = false
        toEdit()
        return editable
    }

    fun edit(position: Int): Boolean {
        editable = true
        editIndex = position
        toEdit()
        return editable
    }

    //    fun edit(position: Int, boolean: Boolean): Boolean {
//        editable = boolean
//        editIndex = position
//        edit()
//        return editable
//    }
    private fun toEdit() {
        if (!editable) SystemMathod.colsedInput()
        notifyDataSetChanged();
    }

    /**
     * notifyDataSetChanged(); 之后根据此方法确定是否需要刷新界面。
     * 所以干脆在此修改界面，避免刷新界面的抖动
     * */
    override fun getItemPosition(mainView: Any): Int {
        Log.i("RackInfo","刷新")
        val rack = racks[editIndex]
        val rackId = (mainView as View).findViewById<TextView>(R.id.rack_id)
        if(rack!=null && (TextUtils.isEmpty(rack.rackInfoId ) || rack.rackInfoId.equals(rackId.text.toString())     )  ) editConfig(mainView)
        return PagerAdapter.POSITION_UNCHANGED
    }

    private fun editConfig(mainView: Any) {
        val rackId = (mainView as View).findViewById<TextView>(R.id.rack_id)
        val rackName = (mainView as View).findViewById<TextInputEditText?>(R.id.rack_name)
        val rackCode = (mainView as View).findViewById<TextInputEditText?>(R.id.rack_code)

        val maxLay = (mainView as View).findViewById<TextInputEditText?>(R.id.rack_max_lay)
        val maxLayLayout = (mainView as View).findViewById<TextInputLayout?>(R.id.rack_max_lay_layout)

        val placeLength = (mainView as View).findViewById<TextInputEditText?>(R.id.place_length)
        val placeLengthLayout = (mainView as View).findViewById<TextInputLayout?>(R.id.place_length_layout)

        val height = (mainView as View).findViewById<TextInputEditText?>(R.id.rack_height)
        val length = (mainView as View).findViewById<TextInputEditText?>(R.id.rack_width)
        val remarke = (mainView as View).findViewById<TextInputEditText?>(R.id.rack_remarks)

        SystemMathod.setEditable(rackName, editable)
        SystemMathod.setEditable(rackCode, editable)
        SystemMathod.setEditable(maxLay, editable)
        SystemMathod.setEditable(placeLength, editable)
        SystemMathod.setEditable(height, editable)
        SystemMathod.setEditable(length, editable)
        SystemMathod.setEditable(remarke, editable)

        if (editable) {
            for (rack in racks) {
//                Log.i("RackInfo", rack.toString())
                if (rackId!!.text.toString().equals(rack.rackInfoId)) {
                    editRack = rack
                    bundle.putParcelable(SystemConstants.RACK_TRANI, editRack)
                    message.data = bundle
                    message.what = RackDetailActivity.handlerRackWhat
                    if (mainView.context is RackDetailActivity) (mainView.context as RackDetailActivity).handler.handleMessage(message)
                }
            }
            rackName!!.requestFocus()
            rackName!!.setSelection(rackName!!.text!!.length)
//            val rackId = mainView.findViewById<TextView>(R.id.rack_id)
            val rackInfo_keys = RackInfo()
            handleRackDetaiMsg(mainView.context, rackInfo_keys.rackInfoId_KEY, rackId.text.toString())
            editTexthandleRackDetaiMsgConfig(mainView.context, null,rackName, rackInfo_keys.rackName_KEY)
            editTexthandleRackDetaiMsgConfig(mainView.context, null,rackCode, rackInfo_keys.rackCode_KEY)
            editTexthandleRackDetaiMsgConfig(mainView.context, maxLayLayout,maxLay, rackInfo_keys.maxLay_KEY)
            editTexthandleRackDetaiMsgConfig(mainView.context, placeLengthLayout,placeLength, rackInfo_keys.placeLength_KEY)
            editTexthandleRackDetaiMsgConfig(mainView.context, null,height, rackInfo_keys.height_KEY)
            editTexthandleRackDetaiMsgConfig(mainView.context, null,length, rackInfo_keys.length_KEY)
            editTexthandleRackDetaiMsgConfig(mainView.context, null,remarke, rackInfo_keys.remarks_KEY)
        }
    }

    /**
     * 配置输入框的TextWatcher
     * */
    private fun editTexthandleRackDetaiMsgConfig(context: Context,textInputLayout: TextInputLayout?, editText: EditText?, key: String) {
        editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                /* 检测数据不能为空的项目 */
                val rackInfo_keys = RackInfo()
                if (TextUtils.isEmpty(s.toString())) {
                    when (key) {
                        rackInfo_keys.maxLay_KEY -> {
                            Toast.makeText(context, R.string.MaxLayNotAllowNull, Toast.LENGTH_SHORT).show()
                            if(textInputLayout!=null)textInputLayout.error = context.getString(R.string.MaxLayNotAllowNull)
                            return
                        }
                        rackInfo_keys.placeLength_KEY -> {
                            Toast.makeText(context, R.string.DisplayLengthNotAllowNull, Toast.LENGTH_SHORT).show()
                            if(textInputLayout!=null)textInputLayout.error = context.getString(R.string.DisplayLengthNotAllowNull)
                            return
                        }
                    }
                }else{
                    when (key) {
                        rackInfo_keys.maxLay_KEY -> {
                            if(textInputLayout!=null)textInputLayout.error = null
                        }
                        rackInfo_keys.placeLength_KEY -> {
                            if(textInputLayout!=null)textInputLayout.error = null
                        }
                    }
                }
                handleRackDetaiMsg(context, key, s.toString())
            }
        })
//        handleRackDetaiMsg(context, key, editText.text.toString())
    }

    private fun handleRackDetaiMsg(context: Context, key: String, value: String) {
        bundle.putString(key, value)
//        bundle.putParcelable()
        message.data = bundle
        message.what = RackDetailActivity.handelEditMsg
        if (context is RackDetailActivity) context.handler.handleMessage(message)
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Log.i("RackInfo","初始化")
        val rack = racks[position]

        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.rack_detail, null)
        view.id = position

        val rackId = view.findViewById<TextView>(R.id.rack_id)
        rackId!!.setText(rack!!.rackInfoId)

        val rackName = view.findViewById<TextInputEditText>(R.id.rack_name)
        rackName!!.setText(rack.rackName)

        val rackCode = view.findViewById<TextInputEditText>(R.id.rack_code)
        rackCode!!.setText(rack.rackCode)

        val maxLay = view.findViewById<TextInputEditText>(R.id.rack_max_lay)
        maxLay!!.setText(if (rack.maxLay == null) "0" else "" + rack.maxLay)

        val placeLength = view.findViewById<TextInputEditText>(R.id.place_length)
        placeLength!!.setText(if (rack.placeLength == null) "0" else "" + rack.placeLength)

        val height = view.findViewById<TextInputEditText>(R.id.rack_height)
        height!!.setText(if (rack!!.height == null) "0" else "" + rack!!.height)

        val length = view.findViewById<TextInputEditText>(R.id.rack_width)
        length!!.setText(if (rack.length == null) "0" else "" + rack.length)

        val remarke = view.findViewById<TextInputEditText>(R.id.rack_remarks)
        remarke!!.setText(rack.remarks)

        if (editable && position == editIndex) {
            editConfig(view)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = container.findViewById<View>(position)
        container.removeView(view)
    }
}