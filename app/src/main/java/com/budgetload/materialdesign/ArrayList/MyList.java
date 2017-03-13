package com.budgetload.materialdesign.ArrayList;

public class MyList {

    private String mobilenum;
    private String contactname;
    private int contactimage;
    private String contactid;
    private String carriername;


    public String getMobileNumber() {
        return mobilenum;
    }

    public void setMobileNumber(String mobilenum) {
        this.mobilenum = mobilenum;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String name) {
        this.contactname = name;
    }


    public void setImage(int image) {
        this.contactimage = image;
    }

    public int getImage() {
        return contactimage;
    }

    public void setContactId(String contactid) {
        this.contactid = contactid;
    }

    public String getContactid() {
        return contactid;
    }

    public void setCarriername(String carriername) {
        this.carriername = carriername;
    }

    public String getCarriername() {
        return carriername;
    }


}
