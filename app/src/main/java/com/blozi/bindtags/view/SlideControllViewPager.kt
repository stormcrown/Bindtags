package com.blozi.bindtags.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent



class SlideControllViewPager :ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    private var x1=0f
    private var x2=0f
    private var y1=0f
    private var y2=0f
    /**是否可以进行滑动,默认允许*/
    private var isSlideableHorizontal = true
    private var isSlideableVertical = true
    fun setIsSlideableHorizontal(slide: Boolean) {
        isSlideableHorizontal = slide
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {



        return super.onTouchEvent(ev)
    }
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if(ev!!.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = ev.getX();
            y1 = ev.getY();
        }
        if(ev.getAction() == MotionEvent.ACTION_MOVE) {
            x2 = ev.getX();
            y2 = ev.getY();
        }
        val x=Math.abs(x1-x2)
        val y = Math.abs(y1-y2)
        if( isSlideableVertical && y>x ) return super.onInterceptTouchEvent(ev)
        else if(isSlideableHorizontal && y<x )return super.onInterceptTouchEvent(ev)
     //   Log.i("不移动",""+ev.action)
        return false

//        return super.onInterceptTouchEvent(ev)
    }
}