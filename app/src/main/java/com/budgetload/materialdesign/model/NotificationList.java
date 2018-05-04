package com.budgetload.materialdesign.model;

/**
 * Created by andrewlaurienrsocia on 11/27/15.
 */
public class NotificationList {

    private String sender;
    private String title;
    private String message;
    private String datesent;
    private int id;
    private String status;
    private String mobile;


    //Getters
    public String getSender() {
        return this.sender;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDatesent() {
        return this.datesent;
    }

    public int getID() {
        return this.id;
    }

    public String getstatus() {
        return this.status;
    }

    public String getMobile() {
        return this.mobile;
    }

    //Setters
    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDatesent(String datesent) {
        this.datesent = datesent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
