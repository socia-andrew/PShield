package com.budgetload.materialdesign.Common;

import android.content.Context;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by andrewlaurienrsocia on 11/10/15.
 */
public class commonFunctions {

    Context mContext;


    //Constructor
    public commonFunctions(Context context) {
        this.mContext = context;
    }

    public boolean validate(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().trim().length() <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean ValidateEmpty(EditText[] fields) {
        boolean myreturn = false;
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().trim().length() > 0) {
                myreturn = true;
                break;
            }
        }
        return myreturn;
    }

    public String formatnumber(String number) {
        DecimalFormat dec;
        dec = new DecimalFormat("#,###,###.####");
        dec.setMinimumFractionDigits(2);
        return dec.format(number);
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



}
