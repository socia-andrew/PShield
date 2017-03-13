package com.budgetload.materialdesign.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

/**
 * Created by andrewlaurienrsocia on 11/12/15.
 */
public class logoutDialog {

    Context mContext;

    // Constructor
    public logoutDialog(Context context) {
        this.mContext = context;
    }

    public static void logoutDialogShow(Context context, final Activity activity) {

        //final AlertDialog alertDialog = new AlertDialog.Builder(context,
        //        AlertDialog.THEME_HOLO_LIGHT).create();


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Are you sure you want to exit app?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //activity.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // myselect = false;

                dialog.dismiss();

            }
        });

        //  alertDialog.show();

        AlertDialog alert11 = alertDialog.create();
        alert11.show();


        Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonbackground.setTextColor(Color.parseColor("#646464"));

        Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));


    }
}
