package com.budgetload.materialdesign.Common;

/**
 * Created by andrewlaurienrsocia on 11/18/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.budgetload.materialdesign.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.budgetload.materialdesign.R.id.groupcode;
import static com.budgetload.materialdesign.Common.GlobalVariables.imei;

public class Deactivated {


    Context mContext;

    // Constructor
    public Deactivated(Context context) {
        this.mContext = context;
    }

    public static void showDeactivatedAccount(Context context, final Activity activity) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Invalid Account");
        alertDialog.setMessage("It seems this account is deactivated. Please register to continue using this application.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
            }
        });


        AlertDialog alert11 = alertDialog.create();
        alert11.show();


        //Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        //buttonbackground.setTextColor(Color.parseColor("#646464"));

        Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));

        FirebaseMessaging.getInstance().unsubscribeFromTopic(MainActivity.PartnerID + "_ALL");
        FirebaseMessaging.getInstance().unsubscribeFromTopic(MainActivity.PartnerID + "_" + imei);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(MainActivity.PartnerID + "_" + groupcode);
        FirebaseMessaging.getInstance().subscribeToTopic(MainActivity.PartnerID + "_0" + MainActivity.regmobile);


        //alertDialog.show();


        // alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#646464"));
        //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#19BD4E"));
        // return myselect;

        // return selection;
    }

}