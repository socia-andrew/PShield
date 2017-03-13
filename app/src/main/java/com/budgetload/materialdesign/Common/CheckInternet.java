package com.budgetload.materialdesign.Common;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

/**
 * Created by andrewlaurienrsocia on 11/10/15.
 */
public class CheckInternet {

    Context mContext;

    // Constructor
    public CheckInternet(Context context) {
        this.mContext = context;
    }

    public static void showConnectionDialog(Context context) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Connection");
        alertDialog.setMessage("Unable to connect to internet. Please check your connection.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });


        // alertDialog.show();


        AlertDialog alert11 = alertDialog.create();
        alert11.show();


        // Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        // buttonbackground.setTextColor(Color.parseColor("#646464"));

        Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));


        // return myselect;

        // return selection;
    }
}
