package com.budgetload.materialdesign.Common;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.activity.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by andrewlaurienrsocia on 11/19/15.
 */
public class FetchWallet extends AsyncTask<String, Void, String> implements Constant {

    Context context;
    Activity activity;
    DataBaseHandler db;
    String mobile;

    WalletListener myListener;

    public FetchWallet(Activity activity) {

        this.activity = activity;
        this.context = activity;

    }

    public void setWalletListener(WalletListener myListener) {
        this.myListener = myListener;
    }

    protected String getASCIIContentFromEntity(HttpEntity entity)
            throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0)
                out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected String doInBackground(String... params) {
        String text;
        try {
            String imei = params[0];
            String SessionID = params[1];
            String PartnerID = params[2];

            db = new DataBaseHandler(context);

            Cursor cursor = db.getIsRegistered(db);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                } while (cursor.moveToNext());
            }

            String myarray = imei + "" + mobile + "" + Constant.getwallet + "" + SessionID.toLowerCase();


            String authcode = GlobalFunctions.generateSha(myarray);


            String apiURL = BALANCEURL
                    + "&IMEI="
                    + imei + "&SourceMobTel="
                    + mobile + "&SessionNo="
                    + SessionID + "&AuthCode=" + authcode +
                    "&VersionCode=" + versionCode
                    + "&PartnerID=" + PartnerID + "";

            // Log.d("URI", apiURL);

            HttpGet httpGet = new HttpGet(apiURL);
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 60000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 60000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            text = getASCIIContentFromEntity(entity);


        } catch (Exception e) {
            text = null;
            Log.d("Exception", "HERE" + e.toString());

        }
        return text;

    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);


        try {
            if (s != null) {


                JSONArray myarray;

                try {

                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("WalletBalance");
                    String sessionresult = articles.getString("Result");
                    String resultCode = articles.getString("ResultCode");

                    if (resultCode.equals("1009") || resultCode.equals("1007")) {
                        myListener.QuerySuccessFul("Deactivated" + ";FAILED");
                    } else {

                        if (sessionresult.trim().equals("OK")) {
                            String credit = articles.getString("Credits");
                            String debit = articles.getString("Debits");
                            String balance = articles.getString("Balance");
                            String msgstatus = articles.getString("MessagingStatus");
                            String VersionCode = articles.getString("VersionCode");
                            db.saveNewWallet(db, credit, debit, balance);
                            MainActivity.walletvalue = balance;
                            myListener.QuerySuccessFul("Success" + ";" + balance + ";" + msgstatus + ";" + VersionCode);
                        } else {
                            myListener.QuerySuccessFul("Failed" + ";FAILED" + ";FAILED" + ";FAILED");
                        }
                    }

                } catch (JSONException e) {
                    myListener.QuerySuccessFul("Failed" + ";FAILED" + ";FAILED" + ";FAILED");
                }
            } else {
                myListener.QuerySuccessFul("Failed" + ";FAILED" + ";FAILED" + ";FAILED");
            }
        } catch (Exception e) {
        }

    }

    public interface WalletListener {
        void QuerySuccessFul(String data);
    }

}
