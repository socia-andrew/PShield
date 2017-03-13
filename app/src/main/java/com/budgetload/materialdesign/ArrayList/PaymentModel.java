package com.budgetload.materialdesign.ArrayList;

/**
 * Created by andrewlaurienrsocia on 28/09/2016.
 */

public class PaymentModel {

    public String Outletname;
    public String mode;
    public int photoID;
    public String method;
    public int paymentrate;
    public String delivery;

    public PaymentModel(String outletname, String mode, int photoID, String method, int paymentrate, String delivery) {
        this.Outletname = outletname;
        this.mode = mode;
        this.photoID = photoID;
        this.method = method;
        this.paymentrate = paymentrate;
        this.delivery = delivery;
    }

//    public PaymentModel(String outletname, int photoid) {
//        this.Outletname = outletname;
//        this.photoID = photoid;
//    }

//    public void setMode(String mode) {
//        this.mode = mode;
//    }
//
//    public void setOutletname(String outletname) {
//        this.Outletname = outletname;
//    }
//
//    public String getOutletname() {
//        return this.Outletname;
//    }
//
//    public String getMode() {
//        return this.mode;
//    }
}
