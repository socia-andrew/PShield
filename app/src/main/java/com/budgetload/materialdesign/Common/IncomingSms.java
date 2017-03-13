package com.budgetload.materialdesign.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.budgetload.materialdesign.activity.Verify;

/**
 * Created by andrewlaurienrsocia on 05/10/2016.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    // public OnSmsReceivedListener listener = null;

    public IncomingSms() {
        super();
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
        SmsMessage[] messages = new SmsMessage[pduArray.length];
        for (int i = 0; i < pduArray.length; i++)
            messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);

        String SideNumber = messages[0].getDisplayOriginatingAddress();
        long Timestamp = messages[0].getTimestampMillis();


        StringBuilder bt = new StringBuilder();
        for (SmsMessage message : messages) bt.append(message.getMessageBody());

        String Smsbody = bt.toString();

        Log.d("Message", Smsbody + "" + SideNumber);


        if (SideNumber.equalsIgnoreCase("Budgetload")) {
            intent.putExtra("Message", Smsbody);
            intent.putExtra("Sender", SideNumber);
            intent.setClass(context, Verify.class);
            context.startActivity(intent);
        }

        //if (listener != null) {
        //    Log.d("Message", "Listener is not null");
        //    listener.onSmsReceived(SideNumber, Smsbody);
        //}


    }

//    public interface OnSmsReceivedListener {
//        void onSmsReceived(String sender, String message);
//    }
//
//    public void setOnSmsReceivedListener(Context context) {
//        Log.d("Message", "her data");
//        this.listener = (OnSmsReceivedListener) context;
//    }

}


