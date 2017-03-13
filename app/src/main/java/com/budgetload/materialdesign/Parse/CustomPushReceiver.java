//package com.budgetload.materialdesign.Parse;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.budgetload.materialdesign.Constant.Indicators;
//import com.budgetload.materialdesign.DataBase.DataBaseHandler;
//import com.budgetload.materialdesign.activity.MainActivity;
//import com.parse.ParsePushBroadcastReceiver;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
///**
// * Created by Ravi on 01/06/15.
// */
//
////extends ParsePushBroadcastReceiver
//public class CustomPushReceiver {
//    private final String TAG = CustomPushReceiver.class.getSimpleName();
//
//    private NotificationUtils notificationUtils;
//
//    private Intent parseIntent;
//
//    public CustomPushReceiver() {
//        super();
//    }
//
//    @Override
//    protected void onPushReceive(Context context, Intent intent) {
//        //super.onPushReceive(context, intent);
//
//        if (intent == null)
//            return;
//
//        try {
//            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
//
//            Log.e(TAG, "Push received: " + json);
//
//            parseIntent = intent;
//
//            parsePushJson(context, json);
//
//        } catch (JSONException e) {
//            Log.e(TAG, "Push message json exception: " + e.getMessage());
//        }
//    }
//
////    @Override
////    protected void onPushDismiss(Context context, Intent intent) {
////        //super.onPushDismiss(context, intent);
////    }
//
////    @Override
////    protected void onPushOpen(Context context, Intent intent) {
////        //super.onPushOpen(context, intent);
////        Log.d("PARSE PUSH","OPEN HERE");
////    }
//
//    /**
//     * Parses the push notification json
//     *
//     * @param context
//     * @param json
//     */
//    private void parsePushJson(Context context, JSONObject json) {
//        try {
//
//            DataBaseHandler db = new DataBaseHandler(context);
//
//            Log.d("PushData", "" + json + "");
//
//            // boolean isBackground = json.getBoolean("is_background");
//
//            String title = json.getString("alert");
//            String message = json.getString("message");
//            String sender = json.getString("sender");
//            String datesent = json.getString("datesent");
//            String status = "UNREAD";
//
//            // Log.d("MyData", "" + isBackground + "");
//
//            Log.d("DataHere", title + ":" + json + ":" + sender + ":" + datesent);
//
//            db.saveNotification(db, title, message, sender, datesent, status);
//
//            Indicators.NotificationIndicator = "true";
//            //if (!isBackground) {
//            Intent resultIntent = new Intent(context, MainActivity.class);
//            Bundle b = new Bundle();
//            b.putString("Purpose", "Notification");
//            showNotificationMessage(context, title, message, resultIntent);
//            //}
//
//        } catch (JSONException e) {
//            Log.e(TAG, "Push message json exception: " + e.getMessage());
//        }
//    }
//
//
//    /**
//     * Shows the notification message in the notification bar
//     * If the app is in background, launches the app
//     *
//     * @param context
//     * @param title
//     * @param message
//     * @param intent
//     */
//    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
//
//        notificationUtils = new NotificationUtils(context);
//        intent.putExtras(parseIntent.getExtras());
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        notificationUtils.showNotificationMessage(title, message, intent);
//    }
//}