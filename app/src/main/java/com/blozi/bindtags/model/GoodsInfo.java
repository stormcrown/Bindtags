package com.blozi.bindtags.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by 骆长涛 on 2018/4/12.
 */

public class GoodsInfo  implements Parcelable {
    private String GoodsInfoId_Key ="goodsInfoId";
    //    private StoreInfo storeInfo;
//    private TemplateInfo templateInfo;
    private String GoodsName_Key = "goodsName";
    private String GoodsPrice_Key = "goodsPrice";
    private String EmberPrice_Key = "emberPrice";
    private String PromotionPrice_Key = "promotionPrice";
    private String DiscountPrice_Key = "discountPrice";
    private String GoodsCode_Key = "goodsCode";
    private String GoodsBarcode_Key = "goodsBarcode";
    private String GoodsQrcode_Key = "goodsQrcode";
    private String GoodsSpecifications_Key = "goodsSpecifications";
    private String GoodsPlace_Key = "goodsPlace";
    private String Unit_Key = "unit";
    private String Number_Key = "number" ;
    private String alcoholicStrength_Key = "alcoholicStrength";
    private String level_Key = "level";
    private String endTime_Key = "endTime";
    private String stock_Key = "stock";
    private String createUserId_Key = "createUserId";
    private String createTime_Key = "createTime";
    private String updateUserId_Key = "updateUserId";
    private String updateTime_Key = "updateTime";
    private String isEffect_Key = "isEffect";
    private String remarkOne_Key = "remarkOne";
    private String remarkTwo_Key = "remarkTwo";
    private String remarkThree_Key = "remarkThree";
    //    private TemplateInfo marketTemplateInfo;
    private String startTime_Key = "startTime";
    private String remarkMarket_Key = "remarkMarket";
    private String brand_Key = "brand";




    private String goodsInfoId;
//    private StoreInfo storeInfo;
//    private TemplateInfo templateInfo;
    private String goodsName;
    private Double goodsPrice;
    private Double memberPrice;
    private Double promotionPrice;
    private Double discountPrice;
    private String goodsCode;
    private String goodsBarcode;
    private String goodsQrcode;
    private String goodsSpecifications;
    private String goodsPlace;
    private String unit;
    private String number;
    private String alcoholicStrength;
    private String level;
    private Date endTime;
    private String stock;
    private String createUserId;
    private Date createTime;
    private String updateUserId;
    private Date updateTime;
    private String isEffect;
    private String remarkOne;
    private String remarkTwo;
    private String remarkThree;
//    private TemplateInfo marketTemplateInfo;
    private Date startTime;
    private String remarkMarket;
    private String brand;

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "goodsInfoId='" + goodsInfoId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", memberPrice=" + memberPrice +
                ", promotionPrice=" + promotionPrice +
                ", discountPrice=" + discountPrice +
                ", goodsCode='" + goodsCode + '\'' +
                ", goodsBarcode='" + goodsBarcode + '\'' +
                ", goodsQrcode='" + goodsQrcode + '\'' +
                ", goodsSpecifications='" + goodsSpecifications + '\'' +
                ", goodsPlace='" + goodsPlace + '\'' +
                ", unit='" + unit + '\'' +
                ", number='" + number + '\'' +
                ", alcoholicStrength='" + alcoholicStrength + '\'' +
                ", level='" + level + '\'' +
                ", endTime=" + endTime +
                ", stock='" + stock + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", createTime=" + createTime +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateTime=" + updateTime +
                ", isEffect='" + isEffect + '\'' +
                ", remarkOne='" + remarkOne + '\'' +
                ", remarkTwo='" + remarkTwo + '\'' +
                ", remarkThree='" + remarkThree + '\'' +
                ", startTime=" + startTime +
                ", remarkMarket='" + remarkMarket + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }

    public String getGoodsInfoId() {
        return goodsInfoId;
    }

    public void setGoodsInfoId(String goodsInfoId) {
        this.goodsInfoId = goodsInfoId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(Double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public Double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsBarcode() {
        return goodsBarcode;
    }

    public void setGoodsBarcode(String goodsBarcode) {
        this.goodsBarcode = goodsBarcode;
    }

    public String getGoodsQrcode() {
        return goodsQrcode;
    }

    public void setGoodsQrcode(String goodsQrcode) {
        this.goodsQrcode = goodsQrcode;
    }

    public String getGoodsSpecifications() {
        return goodsSpecifications;
    }

    public void setGoodsSpecifications(String goodsSpecifications) {
        this.goodsSpecifications = goodsSpecifications;
    }

    public String getGoodsPlace() {
        return goodsPlace;
    }

    public void setGoodsPlace(String goodsPlace) {
        this.goodsPlace = goodsPlace;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAlcoholicStrength() {
        return alcoholicStrength;
    }

    public void setAlcoholicStrength(String alcoholicStrength) {
        this.alcoholicStrength = alcoholicStrength;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(String isEffect) {
        this.isEffect = isEffect;
    }

    public String getRemarkOne() {
        return remarkOne;
    }

    public void setRemarkOne(String remarkOne) {
        this.remarkOne = remarkOne;
    }

    public String getRemarkTwo() {
        return remarkTwo;
    }

    public void setRemarkTwo(String remarkTwo) {
        this.remarkTwo = remarkTwo;
    }

    public String getRemarkThree() {
        return remarkThree;
    }

    public void setRemarkThree(String remarkThree) {
        this.remarkThree = remarkThree;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getRemarkMarket() {
        return remarkMarket;
    }

    public void setRemarkMarket(String remarkMarket) {
        this.remarkMarket = remarkMarket;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goodsInfoId);
        dest.writeString(this.goodsName);
        dest.writeValue(this.goodsPrice);
        dest.writeValue(this.memberPrice);
        dest.writeValue(this.promotionPrice);
        dest.writeValue(this.discountPrice);
        dest.writeString(this.goodsCode);
        dest.writeString(this.goodsBarcode);
        dest.writeString(this.goodsQrcode);
        dest.writeString(this.goodsSpecifications);
        dest.writeString(this.goodsPlace);
        dest.writeString(this.unit);
        dest.writeString(this.number);
        dest.writeString(this.alcoholicStrength);
        dest.writeString(this.level);
        dest.writeLong(this.endTime != null ? this.endTime.getTime() : -1);
        dest.writeString(this.stock);
        dest.writeString(this.createUserId);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
        dest.writeString(this.updateUserId);
        dest.writeLong(this.updateTime != null ? this.updateTime.getTime() : -1);
        dest.writeString(this.isEffect);
        dest.writeString(this.remarkOne);
        dest.writeString(this.remarkTwo);
        dest.writeString(this.remarkThree);
        dest.writeLong(this.startTime != null ? this.startTime.getTime() : -1);
        dest.writeString(this.remarkMarket);
        dest.writeString(this.brand);
    }

    public GoodsInfo() {
    }

    protected GoodsInfo(Parcel in) {
        this.goodsInfoId = in.readString();
        this.goodsName = in.readString();
        this.goodsPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.memberPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.promotionPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.discountPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.goodsCode = in.readString();
        this.goodsBarcode = in.readString();
        this.goodsQrcode = in.readString();
        this.goodsSpecifications = in.readString();
        this.goodsPlace = in.readString();
        this.unit = in.readString();
        this.number = in.readString();
        this.alcoholicStrength = in.readString();
        this.level = in.readString();
        long tmpEndTime = in.readLong();
        this.endTime = tmpEndTime == -1 ? null : new Date(tmpEndTime);
        this.stock = in.readString();
        this.createUserId = in.readString();
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        this.updateUserId = in.readString();
        long tmpUpdateTime = in.readLong();
        this.updateTime = tmpUpdateTime == -1 ? null : new Date(tmpUpdateTime);
        this.isEffect = in.readString();
        this.remarkOne = in.readString();
        this.remarkTwo = in.readString();
        this.remarkThree = in.readString();
        long tmpStartTime = in.readLong();
        this.startTime = tmpStartTime == -1 ? null : new Date(tmpStartTime);
        this.remarkMarket = in.readString();
        this.brand = in.readString();
    }

    public static final Creator<GoodsInfo> CREATOR = new Creator<GoodsInfo>() {
        @Override
        public GoodsInfo createFromParcel(Parcel source) {
            return new GoodsInfo(source);
        }

        @Override
        public GoodsInfo[] newArray(int size) {
            return new GoodsInfo[size];
        }
    };
}
