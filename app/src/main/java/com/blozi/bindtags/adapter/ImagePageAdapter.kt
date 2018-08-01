package com.blozi.bindtags.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide


class ImagePageAdapter : PagerAdapter {
    constructor() : super()
    constructor(list: List<Int>) : super(){
        paths = list
    }
    private var paths: List<Int>?=null
//    val views:List<View> = ArrayList<View>()
    override fun getCount(): Int {
        return paths!!.size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val path = paths!![position]
        val imageView = ImageView(container.context)
        imageView.id = position
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        Glide.with(container.context).load(path).into(imageView)
        container.addView(imageView)
        return imageView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val imageView = container.findViewById<ImageView>(position)
        container.removeView(imageView)
    }
}