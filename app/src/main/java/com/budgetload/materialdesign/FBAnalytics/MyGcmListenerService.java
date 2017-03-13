package com.budgetload.materialdesign.FBAnalytics;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by andrewlaurienrsocia on 11/11/2016.
 */

public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {



//        Context context = this.getApplicationContext();
//        Intent defaultAction = new Intent(context, MainActivity.class)
//                .setAction(Intent.ACTION_DEFAULT)
//                .putExtra("push", data);
//
//        String title = data.getString("title");
//        String body = data.getString("body");
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.notification_template_icon_bg)
//                .setContentTitle(title == null ? "" : title)
//                .setContentText(body == null ? "Hello world" : body)
//                .setAutoCancel(true)
//                .setContentIntent(PendingIntent.getActivity(
//                        context,
//                        0,
//                        defaultAction,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                ));
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(123, mBuilder.build());
    }

}
