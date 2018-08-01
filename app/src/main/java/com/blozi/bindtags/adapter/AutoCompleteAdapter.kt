package com.blozi.bindtags.adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blozi.bindtags.R
import com.blozi.bindtags.activities.LoginActivity
import com.blozi.bindtags.util.BLOZIPreferenceManager
/*
* 自动填充输入框，用于登录界面
* */
class AutoCompleteAdapter :BaseAdapter, Filterable {

    private var data :Array<String>
    private var context:Context
    constructor(context:Context,data:Array<String>) : super(){
        this.context=context
        this.data = data
    }
    constructor(data: Array<String>, context: Context) : super() {
        this.data = data
        this.context = context
    }
    override fun getFilter(): Filter{
        return AutoCompleteFilter(data)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.common_textline, null)
        parent!!.background=null
        val textline = view.findViewById<TextView>(R.id.com_text)
        textline.setText(data[position])
        val image =view.findViewById<ImageView>(R.id.deleteIcon)
        image.setOnClickListener {
            val alertDialog= AlertDialog.Builder(context)
                    .setTitle(R.string.delete)
                    .setMessage(context.resources.getString(R.string.deleteAccount).format(data[position])  )
                    .setPositiveButton(R.string.confirm,DialogInterface.OnClickListener { dialog, which ->
                        val blo =BLOZIPreferenceManager.getInstance(context)
                        blo!!.removeOnLineAccount(data[position])

                       // if(context is LoginActivity)
                            (context as LoginActivity).deleteOnlineAccounts(data[position])

                    })
                    .setNegativeButton(R.string.cancel,null)
            alertDialog.create().show()
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return data.size
    }
    inner class AutoCompleteFilter :Filter{
        private val data :Array<String>
        constructor(data :Array<String>) : super(){
            this.data =data
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = Filter.FilterResults()
            var values = ArrayList<String>()
            if(!TextUtils.isEmpty(constraint)){
                var prefix = constraint.toString().toLowerCase()
                synchronized(data){
                    for(str in data){
                        if(str.toLowerCase().indexOf(prefix)>-1 && !prefix.equals(str.toLowerCase())  )values.add(str)
                    }
                }
            }
            results.values = values
            results.count = values.size
            return results
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return super.convertResultToString(resultValue)
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val a=this@AutoCompleteAdapter
            a.data =data
            if(results!!.count>0) a.notifyDataSetChanged() else a.notifyDataSetInvalidated()

        }
    }

}