package com.blozi.bindtags.activities.fragment;


import android.os.Bundle;

import com.blozi.bindtags.activities.fragment.common.WebFragment;
import com.blozi.bindtags.activities.fragment.mainTab.BindingTagAndGood;
import com.blozi.bindtags.activities.fragment.mainTab.GoodInfoManage;
import com.blozi.bindtags.activities.fragment.mainTab.ImportTags;
import com.blozi.bindtags.activities.fragment.mainTab.RackListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 骆长涛 on 2018/3/14.
 */

public class FragmentFactory {
//    public static final String ME = "Me";
    public static final String BindingTagAndGood = "BindingTagAndGood";
    public static final String BindingTagAndGoodLocal = "BindingTagAndGoodLocal";
    public static final String IMPORT_TAGS = "ImportTags";
    public static final String IMPORT_TAGSLocal = "ImportTagsLocal";
    public static final String DELETE_TAGS = "deleteTags";
    public static final String GOOD_INFO = "GoodInfo";
    public static final String GOOD_INFO_Local = "GoodInfo_Local";
    public static final String RackList = "RackList";
    public static final String WebFragment = "WebFragment";
    public static final String Online = "Online";
    public static final String Local = "Local";
    private static BindingTagAndGood bindingTagAndGood,bindingTagAndGoodLocal;
    private static ImportTags importTags ,importTagsLocal ,deleteTags;
    private static GoodInfoManage goodInfoManage,goodInfoManageLocal;
    private static RackListFragment rackListFragment;
    private static WebFragment webFragment;
    public static List<BaseFragment>  getAllMainFragment(){
        List<BaseFragment> baseFragmentList = new ArrayList<>();
        baseFragmentList.add(bindingTagAndGood);
        baseFragmentList.add(importTags);
        baseFragmentList.add(rackListFragment);
        baseFragmentList.add(goodInfoManage);
        baseFragmentList.add(deleteTags);
        baseFragmentList.add(webFragment);
        return baseFragmentList;
    }
    public static List<BaseFragment>  getAllMainFragmentAcc(){
        List<BaseFragment> baseFragmentList = new ArrayList<>();
        baseFragmentList.add(bindingTagAndGood);
        baseFragmentList.add(importTags);
        baseFragmentList.add(rackListFragment);
        baseFragmentList.add(goodInfoManage);
        baseFragmentList.add(deleteTags);
        baseFragmentList.add(webFragment);
        return baseFragmentList;
    }
    public static com.blozi.bindtags.activities.fragment.mainTab.BindingTagAndGood getBindingTagAndGood() {
        if(bindingTagAndGood==null)bindingTagAndGood = new BindingTagAndGood();
        return bindingTagAndGood;
    }

    public static com.blozi.bindtags.activities.fragment.mainTab.BindingTagAndGood getBindingTagAndGoodLocal() {
        if(bindingTagAndGoodLocal==null)bindingTagAndGoodLocal=new BindingTagAndGood();
        return bindingTagAndGoodLocal;
    }

    public static ImportTags getImportTagsLocal() {
        if(importTagsLocal==null)importTagsLocal=new ImportTags();
        return importTagsLocal;
    }

    public static ImportTags getImportTags() {
        if(importTags==null)importTags = new ImportTags();
        return importTags;
    }

    public static ImportTags getDeleteTags() {
        if(deleteTags==null ){
            deleteTags = new ImportTags();
            Bundle bundle = new Bundle();
            bundle.putString("isDelete", true + "");
            deleteTags.setArguments(bundle);
        }
        return deleteTags;
    }

    public static GoodInfoManage getGoodInfoManage() {
        if( goodInfoManage==null )goodInfoManage = new GoodInfoManage();
        return goodInfoManage;
    }
    public static GoodInfoManage getGoodInfoManageLocal() {
        if( goodInfoManageLocal==null ){
            goodInfoManageLocal = new GoodInfoManage();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isLocal", true);
            goodInfoManageLocal.setArguments(bundle);
        }
        return goodInfoManageLocal;
    }

    public static RackListFragment getRackListFragment() {
        if( rackListFragment==null )rackListFragment = new RackListFragment();
        return rackListFragment;
    }
    public static WebFragment getWebFragment(){
        if(webFragment==null)webFragment =new WebFragment();
        return webFragment;
    }
    /**
     * 主界面的fragment
     * */
    public static final String getFrameByIndex(int index) {
        switch (index) {
            case 0:
                return BindingTagAndGood;
            case 1:
                return IMPORT_TAGS;
            case 2:
                return RackList;
            case 3:
                return GOOD_INFO;
            default:
                return BindingTagAndGood;
        }
    }
    public static final int getIndexByName(String fragmentName) {
        switch (fragmentName) {
            case BindingTagAndGood:
                return 0;
            case IMPORT_TAGS:
                return 1;
            case RackList:
                return 2;
            case GOOD_INFO:
                return 3;
            case DELETE_TAGS:
                return 4;
            case WebFragment:
                return 4;
            default:
                return 0;
        }
    }
    public static final String getLocalFrameByIndex(int index) {
        switch (index) {
            case 0:
                return BindingTagAndGoodLocal;
            case 1:
                return GOOD_INFO_Local;
            default:
                return BindingTagAndGoodLocal;
        }
    }

    public static BaseFragment getFragment(String fragmentName) {
        switch (fragmentName) {
            case BindingTagAndGood:
                return getBindingTagAndGood();
            case BindingTagAndGoodLocal:
                return getBindingTagAndGoodLocal();
            case IMPORT_TAGS:
                return getImportTags();
            case IMPORT_TAGSLocal:
                return getImportTagsLocal();
            case DELETE_TAGS:
                return getDeleteTags();
            case GOOD_INFO:
                return getGoodInfoManage();
            case GOOD_INFO_Local:
                return getGoodInfoManageLocal();
            case RackList:
                return getRackListFragment();
            case WebFragment:
                return getWebFragment();
            default:
                return getBindingTagAndGood();
        }
    }
    public static void clear(){
        bindingTagAndGood =null;
        bindingTagAndGoodLocal = null;
        importTags = null;
        importTagsLocal = null;
        deleteTags = null;
        goodInfoManage=null;
        goodInfoManageLocal=null;
        rackListFragment=null;
        webFragment=null;
    }
}
