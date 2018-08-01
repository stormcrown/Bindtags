package com.blozi.bindtags.model;

/**
 * Created by fly_liu on 2017/8/14.
 */

public class GoodsEdBarcode {
    public GoodsEdBarcode(){

    }

    private String goodsBarcode;
    private String edBarcode;

    public String getGoodsBarcode(){
        return goodsBarcode;
    }
    public void setGoodsBarcode(String goodsBarcode){
        this.goodsBarcode = goodsBarcode;
    }

    public String getEdBarcode(){
        return edBarcode;
    }
    public void setEdBarcode(String edBarcode){
        this.edBarcode = edBarcode;
    }
}
