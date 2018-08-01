package com.blozi.bindtags.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.blozi.bindtags.activities.fragment.FragmentFactory


class FragmentAdapter : FragmentPagerAdapter {
    enum class Type{
        Online,Local
    }
    var hasExtraFragment = false
    private var type = Type.Online
    constructor(type:Type, fm: FragmentManager) : super(fm){
        this.type = type
    }
    override fun getItem(index: Int): Fragment {
        when(type){
            Type.Online ->{
                if(index==4) return FragmentFactory.getDeleteTags()
                else return FragmentFactory.getFragment(FragmentFactory.getFrameByIndex(index));
            }
            Type.Local->{
                return FragmentFactory.getFragment(FragmentFactory.getLocalFrameByIndex(index));
            }
            else-> {
                return FragmentFactory.getFragment(FragmentFactory.getFrameByIndex(index));
            }
        }

    }

    override fun getCount(): Int {
        when(type){
            Type.Online ->{
                if(!hasExtraFragment) return 4
                else return 5
            }
            Type.Local->{
                return 2;
            }
            else-> {
                return 0;
            }
        }
    }
}