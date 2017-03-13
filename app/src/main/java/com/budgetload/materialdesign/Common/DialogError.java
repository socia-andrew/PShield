package com.budgetload.materialdesign.Common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

/**
 * Created by andrewlaurienrsocia on 11/10/15.
 */


public class DialogError {

    static AlertDialog.Builder alertDialog;

    public static void alertDialogShow(Context context, String Title, String message) {

        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(Title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //alertDialog.show();

        AlertDialog alert11 = alertDialog.create();
        alert11.show();


        //Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        //buttonbackground.setTextColor(Color.parseColor("#646464"));

        Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));


    }


    public static void hideDialog(Context context) {

        alertDialog = new AlertDialog.Builder(context);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();


    }

}


