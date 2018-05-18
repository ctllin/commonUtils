package com.ctl.test.model;

import java.io.Serializable;
import java.util.Date;

public class SendCoupon implements Serializable {
    private Integer id;

    private Integer couponType;

    private Integer couponId;

    private String couponUuid;

    private String couponBatchNumber;

    private String couponName;

    private Date execTime;

    private Integer messageNotice;

    private Byte delFlag;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponUuid() {
        return couponUuid;
    }

    public void setCouponUuid(String couponUuid) {
        this.couponUuid = couponUuid == null ? null : couponUuid.trim();
    }

    public String getCouponBatchNumber() {
        return couponBatchNumber;
    }

    public void setCouponBatchNumber(String couponBatchNumber) {
        this.couponBatchNumber = couponBatchNumber == null ? null : couponBatchNumber.trim();
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName == null ? null : couponName.trim();
    }

    public Date getExecTime() {
        return execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    public Integer getMessageNotice() {
        return messageNotice;
    }

    public void setMessageNotice(Integer messageNotice) {
        this.messageNotice = messageNotice;
    }

    public Byte getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SendCoupon other = (SendCoupon) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCouponType() == null ? other.getCouponType() == null : this.getCouponType().equals(other.getCouponType()))
            && (this.getCouponId() == null ? other.getCouponId() == null : this.getCouponId().equals(other.getCouponId()))
            && (this.getCouponUuid() == null ? other.getCouponUuid() == null : this.getCouponUuid().equals(other.getCouponUuid()))
            && (this.getCouponBatchNumber() == null ? other.getCouponBatchNumber() == null : this.getCouponBatchNumber().equals(other.getCouponBatchNumber()))
            && (this.getCouponName() == null ? other.getCouponName() == null : this.getCouponName().equals(other.getCouponName()))
            && (this.getExecTime() == null ? other.getExecTime() == null : this.getExecTime().equals(other.getExecTime()))
            && (this.getMessageNotice() == null ? other.getMessageNotice() == null : this.getMessageNotice().equals(other.getMessageNotice()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCouponType() == null) ? 0 : getCouponType().hashCode());
        result = prime * result + ((getCouponId() == null) ? 0 : getCouponId().hashCode());
        result = prime * result + ((getCouponUuid() == null) ? 0 : getCouponUuid().hashCode());
        result = prime * result + ((getCouponBatchNumber() == null) ? 0 : getCouponBatchNumber().hashCode());
        result = prime * result + ((getCouponName() == null) ? 0 : getCouponName().hashCode());
        result = prime * result + ((getExecTime() == null) ? 0 : getExecTime().hashCode());
        result = prime * result + ((getMessageNotice() == null) ? 0 : getMessageNotice().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", couponType=").append(couponType);
        sb.append(", couponId=").append(couponId);
        sb.append(", couponUuid=").append(couponUuid);
        sb.append(", couponBatchNumber=").append(couponBatchNumber);
        sb.append(", couponName=").append(couponName);
        sb.append(", execTime=").append(execTime);
        sb.append(", messageNotice=").append(messageNotice);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}