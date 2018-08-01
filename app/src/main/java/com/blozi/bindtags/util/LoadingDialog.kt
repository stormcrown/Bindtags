package com.blozi.bindtags.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.blozi.bindtags.R

object LoadingDialog {
    private var builder:AlertDialog.Builder? = null
    private var layoutInflater:LayoutInflater?=null
    private var animation:Animation?=null
    private var dialog :Dialog?=null
    private val handler = Handler()
    private val cancelAble = Runnable { if( dialog!=null && dialog!!.isShowing) dialog!!.setCancelable(true) }
    private val autoDismiss = Runnable {
        try {
            if( dialog!=null && dialog!!.isShowing ) dialog!!.dismiss()
        }catch (e :Exception ){
            e.printStackTrace()
        }
    }
    @Synchronized
    private fun basebase(context: Context):View{
        handler.removeCallbacks(cancelAble)
        handler.removeCallbacks(autoDismiss)
        builder = AlertDialog.Builder(context,R.style.dialog)
        layoutInflater = LayoutInflater.from(context)
        val loading = layoutInflater!!.inflate(R.layout.loading, null)
        animation = AnimationUtils.loadAnimation(context, R.anim.recycle_forevery)
        loading.findViewById<View>(R.id.imageView).animation = animation
        return loading
    }
    private fun base(context: Context ,msg:String?):View{
       val loading = basebase(context)
        if(msg!=null)loading.findViewById<AppCompatTextView>(R.id.msg).setText(msg)
        return loading
    }
    private fun base(context: Context ,msg:Int?):View{
        val loading = basebase(context)
        if(msg!=null)loading.findViewById<AppCompatTextView>(R.id.msg).setText(msg)
        return loading
    }
    fun alertDialog(context: Context ,msg:String?) {

        val loading = base(context,msg)
        animation!!.start()
        dialog = builder!!.create()
        dialog!!.setCancelable(false)
        dialog!!.show()
        loading.background=null

        dialog!!.window!!.setContentView(loading)

        handler.postDelayed(cancelAble, 10*1000)
        handler.postDelayed(autoDismiss, SystemConstants.over_time.toLong() )
    }

    fun alertDialog(context: Context ,msg:Int) {
        val loading = base(context,msg)
        animation!!.start()
        dialog = builder!!.create()
        dialog!!.setCancelable(false)
        dialog!!.show()
        loading.background=null
        dialog!!.window!!.setContentView(loading)
        handler.postDelayed(cancelAble, 10*1000)
        handler.postDelayed(autoDismiss, SystemConstants.over_time.toLong() )
    }
    fun alertDialog(context: Context) {
        alertDialog(context,null)
    }

    fun closeDialog(){
        handler.removeCallbacks(cancelAble)
        handler.removeCallbacks(autoDismiss)
        try {
            if( dialog!=null && dialog!!.isShowing ) dialog!!.dismiss()
        }catch (e :Exception ){
            e.printStackTrace()
        }

    }


    fun isShowing():Boolean{
        if(dialog!=null) return dialog!!.isShowing
        else return false
    }
}