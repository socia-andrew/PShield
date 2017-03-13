//package com.budgetload.materialdesign.Parse;
//
//import android.app.Application;
//import android.content.Context;
//import android.net.wifi.WifiManager;
//import android.telephony.TelephonyManager;
//
//import com.parse.Parse;
//import com.parse.ParseInstallation;
//
///**
// * Created by andrewlaurienrsocia on 11/26/15.
// */
//public class MyApplication extends Application {
//
//    private static MyApplication mInstance;
//
//
////    private String android_id = Secure.getString(getBaseContext().getContentResolver(),
////            Secure.ANDROID_ID);
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mInstance = this;
//
//
//        String imei = getIMEI();
//
////        DataBaseHandler db = new DataBaseHandler(this);
////        final String PartnerID = db.getPartnerID(db);
////        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
////        installation.deleteInBackground(new DeleteCallback() {
////            @Override
////            public void done(ParseException ex) {
////                Log.d("ParseDelete", "ParseInstallation deleteInBackground done");
////                if (ex != null) {
////                    Log.e("ParseDelete", "ParseInstallation deleteInBackground", ex);
////                }
////            }
////        });
//
//
//
//        Parse.initialize(this, AppConfig.PARSE_APPLICATION_ID, AppConfig.PARSE_CLIENT_KEY);
//        ParseInstallation.getCurrentInstallation().saveInBackground();
//
//
//        //remove all installation in parse
//        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//
////        if (installation != null) {
////            installation.remove("channels");
////            installation.saveEventually(new SaveCallback() {  // or saveInBackground
////                @Override
////                public void done(ParseException e) {
////                    // TODO: add code here
////                }
////            });
////
////            //    Toast.makeText(getBaseContext(), "Unsubscribe", 500).show();
////        }
//
//
//
////        ParseQuery duplicateQuery = ParseInstallation.getQuery();
////        duplicateQuery.whereEqualTo("uniqueID", android_id);
////        duplicateQuery.getFirstInBackground(new GetCallback<ParseInstallation>() {
////            @Override
////            public void done(ParseInstallation duplicate, ParseException e) {
////                if (e != null) {
////                    e.printStackTrace(); //null in my case
////                }
////                duplicate.deleteInBackground();
////            }
////
////        });
//
////        ParsePush.subscribeInBackground("BL" + imei, new SaveCallback() {
////            @Override
////            public void done(ParseException e) {
////                Log.e("Parse", "Successfully subscribed to Parse!");
////            }
////        });
////
////        Object channels = ParseInstallation.getCurrentInstallation().getList("channels");
////
////
////        Log.d("Channels", "" + channels + "");
////        if (PartnerID != null) {
////
////            Log.d("ChannelHere", PartnerID);
////            ParsePush.subscribeInBackground(PartnerID + "_ALL", new SaveCallback() {
////                @Override
////                public void done(ParseException e) {
////                    Log.e("Parse", "Successfully subscribed to Parse!" + PartnerID);
////                }
////            });
////
////        }
//
//    }
//
//    private void setupParse(Context context) {
//        Parse.initialize(this, AppConfig.PARSE_APPLICATION_ID, AppConfig.PARSE_CLIENT_KEY);
//        ParseInstallation.getCurrentInstallation().put("uniqueId", getWifiMacAddress(context));
//        ParseInstallation.getCurrentInstallation().saveInBackground();
//    }
//
//    private String getWifiMacAddress(Context context) {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
//            return wifiManager.getConnectionInfo().getMacAddress();
//        }
//
//        return "";
//    }
//
//    public String getIMEI() {
//
//        TelephonyManager telephonyManager = (TelephonyManager) this
//                .getSystemService(Context.TELEPHONY_SERVICE);
//
//        return telephonyManager.getDeviceId();
//
//    }
//
//    public static synchronized MyApplication getInstance() {
//        return mInstance;
//    }
//
//}