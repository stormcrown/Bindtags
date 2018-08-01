package com.blozi.bindtags.adapter

import android.content.Context
import android.database.DataSetObserver
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ExpandableListAdapter
import android.widget.LinearLayout
import android.widget.TextView

import com.blozi.bindtags.R
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.model.TreeData
import com.blozi.bindtags.view.CommonItemLine

class SimpleTreeAdapter : ExpandableListAdapter {
    private val datas = ArrayList<TreeData>()
    private var context:Context?=null

    constructor(context:Context ) {
        this.context = context
    }
    fun addGroup(group:TreeData){
        for(tree in datas){
            if( tree!=null && tree.hideId!=null && tree.hideId.equals(group.hideId) ){
                if(group.children!=null){
                    for(child in group.children) tree.addTopChild(child)
                }
                return
            }
        }
        datas.add(group)

    }



    override fun registerDataSetObserver(dataSetObserver: DataSetObserver) {

    }

    override fun unregisterDataSetObserver(dataSetObserver: DataSetObserver) {

    }

    override fun getGroupCount(): Int {
        return datas.size
    }

    override fun getChildrenCount(index: Int): Int {
        Log.i("getChildrenCount",index.toString() +"\t" +datas.get(index).children.size)
        if (datas.size > index && datas.get(index).children != null) return datas.get(index).children.size
        return 0
    }

    override fun getGroup(index: Int): Any? {
        if (datas.size > index) return datas.get(index)
        return null
    }

    override fun getChild(indexGroup: Int, indexChild: Int): Any? {
        if (datas.size > indexGroup && datas.get(indexGroup).children != null && datas.get(indexGroup).children.size > indexChild) {
            return datas.get(indexGroup).children.get(indexChild)
        }
        return null
    }

    override fun getGroupId(i: Int): Long {
        return i.toLong()
    }

    override fun getChildId(i: Int, i1: Int): Long {
        return i1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(index: Int, b: Boolean, view: View?, viewGroup: ViewGroup): View? {

//        val inflater = LayoutInflater.from(viewGroup.context)
        val v =TextView(context) // CommonItemLine(context)
        Log.i("GroupView",getGroupCount().toString() +"\t"+b.toString())

        if (datas.size > index) {
            val p = AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            v.layoutParams = p
            v.setPadding(48,0,0,0)

            if(!TextUtils.isEmpty(datas.get(index).name))v.setText(datas.get(index).name)
//            if(!TextUtils.isEmpty(datas.get(index).code))v.setSubText(datas.get(index).code)
        }
        return v
    }

    override fun getChildView(indexGroup: Int, indexChild: Int, b: Boolean, view: View?, viewGroup: ViewGroup): View? {
        Log.i("ChildView",getGroupCount().toString()+"ChildView")
//        val inflater = LayoutInflater.from(viewGroup.context)
        val v = TextView(context)
        val linearLayout = LinearLayout(context)
        if (datas.size > indexGroup && datas.get(indexGroup).children != null && datas.get(indexGroup).children.size > indexChild) {

            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, 0, 0)
            linearLayout.layoutParams = layoutParams
            if(!TextUtils.isEmpty(datas.get(indexGroup).children.get(indexChild).name))v.setText(datas.get(indexGroup).children.get(indexChild).name)
//            if(!TextUtils.isEmpty(datas.get(indexGroup).children.get(indexChild).code))v.setSubText(datas.get(indexGroup).children.get(indexChild).code)
        }
        linearLayout.addView(v)
        return linearLayout
    }

    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return true
    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun onGroupExpanded(i: Int) {

    }

    override fun onGroupCollapsed(i: Int) {

    }

    override fun getCombinedChildId(l: Long, l1: Long): Long {
        return 0
    }

    override fun getCombinedGroupId(l: Long): Long {
        return 0
    }
}
