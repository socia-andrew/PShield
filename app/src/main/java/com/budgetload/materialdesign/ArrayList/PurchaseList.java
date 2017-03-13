package com.budgetload.materialdesign.ArrayList;

/**
 * Created by andrewlaurienrsocia on 11/25/15.
 */
public class PurchaseList {

    private String txnno;
    private String status;
    private String branchid;
    private Float amount;
    private String datepurchase;

    //Getters
    public String getTxnno() {
        return this.txnno;
    }

    public String getStatus() {
        return this.status;
    }

    public String getBranchid() {
        return this.branchid;
    }

    public Float getAmount() {
        return this.amount;
    }

    public String getDatepurchase() {
        return this.datepurchase;
    }

    //Setters
    public void setTxnno(String txnno) {
        this.txnno = txnno;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setDatepurchase(String datepurchase) {
        this.datepurchase = datepurchase;
    }
}
