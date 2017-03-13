package com.budgetload.materialdesign.Parse;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.budgetload.materialdesign.R;

import java.util.List;


/**
 * Created by Ravi on 01/06/15.
 */
public class NotificationUtils {

    private String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    public NotificationUtils() {
    }

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, Intent intent) {

//
//        // Construct pending intent to serve as action for notification item
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("message", "Launched via notification with message: " + val + " and timestamp " + timestamp);
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        // Create notification
//        String longText = "Intent service has a new message with: " + val + " and a timestamp of: " + timestamp;
//        Notification noti =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("New Result!")
//                        .setContentText("Simple Intent service has a new message")
//                        .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))
//                        .setContentIntent(pIntent)
//                        .build();
//
//
//        // Hide the notification after its selected
//        noti.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        // mId allows you to update the notification later on.
//        mNotificationManager.notify(NOTIF_ID, noti);

        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;

        if (isAppIsInBackground(mContext)) {
            // notification icon
            // int icon = R.mipmap.ic_launcher;
            int smallIcon = R.drawable.budgetloadnotifcation;
            int mNotificationId = AppConfig.NOTIFICATION_ID;
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(mContext, 0, intent, 0);

            // NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            //      NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
            //              mContext);


//            Notification notification =
//                    mBuilder.setSmallIcon(R.drawable.ic_test)
//                            .setTicker(title).setWhen(0)
//                            .setAutoCancel(true)
//                            .setContentTitle(title)
//                            //.setStyle(inboxStyle)
//                            .setContentIntent(resultPendingIntent)
//                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                            //.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                            .setContentText(message)
//                            .setColor(Color.parseColor("#000000"));
//                            //.getNotification();

            Notification notification =
                    new NotificationCompat.Builder(mContext)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setContentIntent(resultPendingIntent)
                            .build();


            notification.flags |= Notification.FLAG_AUTO_CANCEL;


            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(mNotificationId, notification);


        } else {

            //  .setContentTitle("New Result!")
            //          .setContentText("Simple Intent service has a new message")

            // intent.putExtra("title", title);
            // intent.putExtra("message", message);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //  Bundle b = new Bundle();
            //   b.putString("Notification", "StartNotification");
            //   intent.putExtras(b);
            //   mContext.startActivity(intent);


        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.budgetloadnotifcation : R.drawable.ic_launcher;
    }

    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
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
