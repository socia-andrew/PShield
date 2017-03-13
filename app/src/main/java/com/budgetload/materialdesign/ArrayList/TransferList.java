package com.budgetload.materialdesign.ArrayList;

/**
 * Created by andrewlaurienrsocia on 11/24/15.
 */
public class TransferList {


    private String TxnNo;
    private String DatetimeIN;
    private String Sender;
    private String Receiver;
    private float amount;
    private float retailerprevstock;
    private float retailerprevconsumed;
    private float retailerprevAvailable;
    private float prevbalance;
    private float postbalance;
    private String senderCommunity;
    private String receiverCommunity;

    //Getters
    public String getTxnNo() {
        return this.TxnNo;
    }

    public String getDatetimeIN() {
        return this.DatetimeIN;
    }

    public String getSender() {
        return this.Sender;
    }

    public String getReceiver() {
        return this.Receiver;
    }

    public Float getamount() {
        return this.amount;
    }

    public Float getprevstock() {
        return this.retailerprevstock;
    }

    public Float getprevconsumed() {
        return this.retailerprevconsumed;
    }

    public Float getprevavailable() {
        return this.retailerprevAvailable;
    }

    public Float getprevbalance() {
        return this.prevbalance;
    }

    public Float getpostbalance() {
        return this.postbalance;
    }

    public String getSenderCommunity() {
        return this.senderCommunity;
    }

    public String getReceiverCommunity() {
        return this.receiverCommunity;
    }

    //Setters
    public void setTxnNo(String txnno) {
        this.TxnNo = txnno;
    }

    public void setDatetimeIN(String datetime) {
        this.DatetimeIN = datetime;
    }

    public void setSender(String sender) {
        this.Sender = sender;

    }

    public void setReceiver(String receiver) {
        this.Receiver = receiver;
    }

    public void setamount(Float amount) {
        this.amount = amount;
    }

    public void setprevstock(Float prevstock) {
        this.retailerprevstock = prevstock;
    }

    public void setprevconsume(Float prevconsumed) {
        this.retailerprevconsumed = prevconsumed;
    }

    public void setprevavailable(Float prevavailable) {
        this.retailerprevAvailable = prevavailable;
    }

    public void setprebalance(Float prevbalance) {
        this.prevbalance = prevbalance;
    }

    public void setPostbalance(Float postbalance) {
        this.postbalance = postbalance;
    }

    public void setSenderCommunity(String sendercommunity){
        this.senderCommunity = sendercommunity;
    }

    public void setReceiverCommunity(String receivercommunity){
        this.receiverCommunity = receivercommunity;
    }

}
