package com.budgetload.materialdesign.Fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.List;

/**
 * Created by andrewlaurienrsocia on 30/08/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public String TAG = "Data";

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // ...
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        DataBaseHandler db = new DataBaseHandler(this);


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d("Data", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Data", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Data", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


        //Log.d("FirebaseMessage", "From: " + remoteMessage.getFrom());

        JSONObject message = null;

        //if (isAppIsInBackground(this)) {

        try {
            message = new JSONObject(remoteMessage.getData());

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setSmallIcon(R.drawable.notificationsicon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(URLDecoder.decode(message.getString("message_title").toString()))
                    .setContentText(URLDecoder.decode(message.getString("message").toString()));

            Intent intent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, builder.build());

//            SimpleDateFormat format = new SimpleDateFormat("hh:mma");
//            Calendar c = Calendar.getInstance();
//            String date = new DateFormatSymbols().getMonths()[c.get(Calendar.MONTH)] + " " +
//                    c.get(Calendar.DAY_OF_MONTH) + ", " + c.get(Calendar.YEAR) + " " + format.format(new Date());
            String requestormobile = "";
            String title = message.getString("message_title");
            String messagecontent = message.getString("message");
            String sender = message.getString("sender");
            String datesent = message.getString("datesent");
            String status = "UNREAD";

            if (message.has("rmobile")) {
                requestormobile = message.getString("rmobile");
            }

            //  Log.d("DataHere", title + ":" + json + ":" + sender + ":" + datesent);

            db.saveNotification(db, URLDecoder.decode(title), URLDecoder.decode(messagecontent), URLDecoder.decode(sender), URLDecoder.decode(datesent), status, requestormobile);

            //Log.d("date", date);
            // LoginActivity.db.insertNotification(new Notification(message.getString("message_title").substring(0,1),
            //         message.getString("message_title"), message.getString("message"), date));

            // LoginActivity.fromNotification = true;
        } catch (JSONException e) {
            //Log.e("JSONERROR", e.getMessage() + " " + remoteMessage.getData().toString());
            Log.e("JSONERROR", e.getMessage());
        }
//        } else {
//            Log.d("FirebaseMessage", "App is closed");
//        }
//        //Log.d("FirebaseMessage", "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }


    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
