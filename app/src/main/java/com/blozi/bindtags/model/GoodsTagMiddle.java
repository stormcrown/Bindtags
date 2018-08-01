package com.blozi.bindtags.model;

/**
 * Created by fly_liu on 2017/7/10.
 */

public class GoodsTagMiddle {
    public GoodsTagMiddle(){

    }

    private String goodsId;
    private String goodsName;
    private String goodsCode;
    private String tagId;
    private String tagCode;
    private String tagStatus;
    private String physicalIpAddress;
    private String goodsBarcode;

    public String getTagId() {
        return tagId;
    }
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagCode() {
        return tagCode;
    }
    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagStatus() {
        return tagStatus;
    }
    public void setTagStatus(String tagStatus) {
        this.tagStatus = tagStatus;
    }

    public String getGoodsId(){
        return goodsId;
    }
    public void setGoodsId(String goodsId){
        this.goodsId = goodsId;
    }

    public String getGoodsName(){
        return goodsName;
    }
    public void setGoodsName(String goodsName){
        this.goodsName = goodsName;
    }

    public String getGoodsCode(){
        return goodsCode;
    }
    public void setGoodsCode(String goodsCode){
        this.goodsCode = goodsCode;
    }

    public String getPhysicalIpAddress(){
        return physicalIpAddress;
    }
    public void setPhysicalIpAddress(String physicalIpAddress){
        this.physicalIpAddress = physicalIpAddress;
    }

    public String getGoodsBarcode(){
        return goodsBarcode;
    }
    public void setGoodsBarcode(String goodsBarcode){
        this.goodsBarcode = goodsBarcode;
    }
}
