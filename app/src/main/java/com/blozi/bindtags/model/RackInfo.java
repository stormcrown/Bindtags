package com.blozi.bindtags.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class RackInfo implements Parcelable {
    /*  下面的不可变量主要用于传递数据时作为键使用 */
    private final String RackInfoId_KEY = "rackInfoId";
    private final String StoreInfoId_KEY="storeInfoId";
    private final String RackName_KEY="rackName";
    private final String RackCode_KEY="rackCode";
    private final String MaxLay_KEY="maxLay";
    private final String Height_KEY="height";
    private final String Length_KEY="length" ;
    private final String PlaceLength_KEY="placeLength";
    private final String Remarks_KEY = "remarks";
    private final String ImagePath_KEY="imagePath";
    private final String CreateTime_KEY="createTime";
    private final String UpdateTime_KEY="updateTime";
    private final String UpdateUserInfoId_KEY="updateUserInfoId";
    private final String IsEffect_KEY="isEffect";
    private final String CreateUserInfoId_KEY="createUserInfoId";
    // Fields
    private String rackInfoId;
    private String storeInfoId;
    private String rackName;
    private String rackCode;
    private Integer maxLay;
    private Integer height;
    private Integer length ;
    private Integer placeLength;
    private String remarks;
    private String imagePath;
    private Date createTime;
    private Date updateTime;
    private String updateUserInfoId;
    private String isEffect;
    private String createUserInfoId;

    public String getRackInfoId() {
        return rackInfoId;
    }

    public void setRackInfoId(String rackInfoId) {
        this.rackInfoId = rackInfoId;
    }

    public String getStoreInfoId() {
        return storeInfoId;
    }

    public void setStoreInfoId(String storeInfoId) {
        this.storeInfoId = storeInfoId;
    }

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    public String getRackCode() {
        return rackCode;
    }

    public void setRackCode(String rackCode) {
        this.rackCode = rackCode;
    }

    public Integer getMaxLay() {
        return maxLay;
    }

    public void setMaxLay(Integer maxLay) {
        this.maxLay = maxLay;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPlaceLength() {
        return placeLength;
    }

    public void setPlaceLength(Integer placeLength) {
        this.placeLength = placeLength;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserInfoId() {
        return updateUserInfoId;
    }

    public void setUpdateUserInfoId(String updateUserInfoId) {
        this.updateUserInfoId = updateUserInfoId;
    }

    public String getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(String isEffect) {
        this.isEffect = isEffect;
    }

    public String getCreateUserInfoId() {
        return createUserInfoId;
    }

    public void setCreateUserInfoId(String createUserInfoId) {
        this.createUserInfoId = createUserInfoId;
    }

    public String getRackInfoId_KEY() {
        return RackInfoId_KEY;
    }

    public String getStoreInfoId_KEY() {
        return StoreInfoId_KEY;
    }

    public String getRackName_KEY() {
        return RackName_KEY;
    }

    public String getRackCode_KEY() {
        return RackCode_KEY;
    }

    public String getMaxLay_KEY() {
        return MaxLay_KEY;
    }

    public String getHeight_KEY() {
        return Height_KEY;
    }

    public String getLength_KEY() {
        return Length_KEY;
    }

    public String getPlaceLength_KEY() {
        return PlaceLength_KEY;
    }

    public String getRemarks_KEY() {
        return Remarks_KEY;
    }

    public String getImagePath_KEY() {
        return ImagePath_KEY;
    }

    public String getCreateTime_KEY() {
        return CreateTime_KEY;
    }

    public String getUpdateTime_KEY() {
        return UpdateTime_KEY;
    }

    public String getUpdateUserInfoId_KEY() {
        return UpdateUserInfoId_KEY;
    }

    public String getIsEffect_KEY() {
        return IsEffect_KEY;
    }

    public String getCreateUserInfoId_KEY() {
        return CreateUserInfoId_KEY;
    }

    public static Creator<RackInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RackInfo)) return false;

        RackInfo rackInfo = (RackInfo) o;

        if (getRackInfoId() != null ? !getRackInfoId().equals(rackInfo.getRackInfoId()) : rackInfo.getRackInfoId() != null)
            return false;
        if (getStoreInfoId() != null ? !getStoreInfoId().equals(rackInfo.getStoreInfoId()) : rackInfo.getStoreInfoId() != null)
            return false;
        if (getRackName() != null ? !getRackName().equals(rackInfo.getRackName()) : rackInfo.getRackName() != null)
            return false;
        if (getRackCode() != null ? !getRackCode().equals(rackInfo.getRackCode()) : rackInfo.getRackCode() != null)
            return false;
        if (getMaxLay() != null ? !getMaxLay().equals(rackInfo.getMaxLay()) : rackInfo.getMaxLay() != null)
            return false;
        if (getHeight() != null ? !getHeight().equals(rackInfo.getHeight()) : rackInfo.getHeight() != null)
            return false;
        if (getLength() != null ? !getLength().equals(rackInfo.getLength()) : rackInfo.getLength() != null)
            return false;
        if (getPlaceLength() != null ? !getPlaceLength().equals(rackInfo.getPlaceLength()) : rackInfo.getPlaceLength() != null)
            return false;
        if (getRemarks() != null ? !getRemarks().equals(rackInfo.getRemarks()) : rackInfo.getRemarks() != null)
            return false;
        if (getImagePath() != null ? !getImagePath().equals(rackInfo.getImagePath()) : rackInfo.getImagePath() != null)
            return false;
        if (getCreateTime() != null ? !getCreateTime().equals(rackInfo.getCreateTime()) : rackInfo.getCreateTime() != null)
            return false;
        if (getUpdateTime() != null ? !getUpdateTime().equals(rackInfo.getUpdateTime()) : rackInfo.getUpdateTime() != null)
            return false;
        if (getUpdateUserInfoId() != null ? !getUpdateUserInfoId().equals(rackInfo.getUpdateUserInfoId()) : rackInfo.getUpdateUserInfoId() != null)
            return false;
        if (getIsEffect() != null ? !getIsEffect().equals(rackInfo.getIsEffect()) : rackInfo.getIsEffect() != null)
            return false;
        return getCreateUserInfoId() != null ? getCreateUserInfoId().equals(rackInfo.getCreateUserInfoId()) : rackInfo.getCreateUserInfoId() == null;
    }

    @Override
    public int hashCode() {
        int result = getRackInfoId() != null ? getRackInfoId().hashCode() : 0;
        result = 31 * result + (getStoreInfoId() != null ? getStoreInfoId().hashCode() : 0);
        result = 31 * result + (getRackName() != null ? getRackName().hashCode() : 0);
        result = 31 * result + (getRackCode() != null ? getRackCode().hashCode() : 0);
        result = 31 * result + (getMaxLay() != null ? getMaxLay().hashCode() : 0);
        result = 31 * result + (getHeight() != null ? getHeight().hashCode() : 0);
        result = 31 * result + (getLength() != null ? getLength().hashCode() : 0);
        result = 31 * result + (getPlaceLength() != null ? getPlaceLength().hashCode() : 0);
        result = 31 * result + (getRemarks() != null ? getRemarks().hashCode() : 0);
        result = 31 * result + (getImagePath() != null ? getImagePath().hashCode() : 0);
        result = 31 * result + (getCreateTime() != null ? getCreateTime().hashCode() : 0);
        result = 31 * result + (getUpdateTime() != null ? getUpdateTime().hashCode() : 0);
        result = 31 * result + (getUpdateUserInfoId() != null ? getUpdateUserInfoId().hashCode() : 0);
        result = 31 * result + (getIsEffect() != null ? getIsEffect().hashCode() : 0);
        result = 31 * result + (getCreateUserInfoId() != null ? getCreateUserInfoId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RackInfo{" +
                "rackInfoId='" + rackInfoId + '\'' +
                ", storeInfoId='" + storeInfoId + '\'' +
                ", rackName='" + rackName + '\'' +
                ", rackCode='" + rackCode + '\'' +
                ", maxLay=" + maxLay +
                ", height=" + height +
                ", length=" + length +
                ", placeLength=" + placeLength +
                ", remarks='" + remarks + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", updateUserInfoId='" + updateUserInfoId + '\'' +
                ", isEffect='" + isEffect + '\'' +
                ", createUserInfoId='" + createUserInfoId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rackInfoId);
        dest.writeString(this.storeInfoId);
        dest.writeString(this.rackName);
        dest.writeString(this.rackCode);
        dest.writeValue(this.maxLay);
        dest.writeValue(this.height);
        dest.writeValue(this.length);
        dest.writeValue(this.placeLength);
        dest.writeString(this.remarks);
        dest.writeString(this.imagePath);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
        dest.writeLong(this.updateTime != null ? this.updateTime.getTime() : -1);
        dest.writeString(this.updateUserInfoId);
        dest.writeString(this.isEffect);
        dest.writeString(this.createUserInfoId);
    }

    public RackInfo() {
    }

    protected RackInfo(Parcel in) {
        this.rackInfoId = in.readString();
        this.storeInfoId = in.readString();
        this.rackName = in.readString();
        this.rackCode = in.readString();
        this.maxLay = (Integer) in.readValue(Integer.class.getClassLoader());
        this.height = (Integer) in.readValue(Integer.class.getClassLoader());
        this.length = (Integer) in.readValue(Integer.class.getClassLoader());
        this.placeLength = (Integer) in.readValue(Integer.class.getClassLoader());
        this.remarks = in.readString();
        this.imagePath = in.readString();
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        long tmpUpdateTime = in.readLong();
        this.updateTime = tmpUpdateTime == -1 ? null : new Date(tmpUpdateTime);
        this.updateUserInfoId = in.readString();
        this.isEffect = in.readString();
        this.createUserInfoId = in.readString();
    }

    public static final Creator<RackInfo> CREATOR = new Creator<RackInfo>() {
        @Override
        public RackInfo createFromParcel(Parcel source) {
            return new RackInfo(source);
        }

        @Override
        public RackInfo[] newArray(int size) {
            return new RackInfo[size];
        }
    };
}
