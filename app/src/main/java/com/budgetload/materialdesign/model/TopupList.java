package com.budgetload.materialdesign.model;

/**
 * Created by andrewlaurienrsocia on 11/24/15.
 */
public class TopupList {

    private String mobile;
    private String status;
    private Float amount;
    private String datetime;
    private String txnid;
    private String BlRefNo;

    //Getters
    public String getMobile() {
        return this.mobile;
    }

    public String getStatus() {
        return this.status;
    }

    public Float getAmount() {
        return this.amount;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public String getTxnid() {
        return this.txnid;
    }

    public String getBlRefNo(){return this.BlRefNo;}

    //Setters
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public void setBlRefNo(String BLRefNo){
        this.BlRefNo = BLRefNo;
    }


}
