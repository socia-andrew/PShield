package com.budgetload.materialdesign.Common;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.budgetload.materialdesign.Constant.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.budgetload.materialdesign.Common.GlobalVariables.imei;

/**
 * Created by andrewlaurienrsocia on 11/12/15.
 */
public class CreateSession extends AsyncTask<String, Void, String> implements Constant {


    Context context;
    Activity activity;

    QueryListener myListener;

    public CreateSession(Activity activity) {

        this.activity = activity;
        this.context = activity;

    }

    public void setQueryListener(QueryListener myListener) {
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
    protected String doInBackground(String... params) {
        String text;
        try {
            String PartnerID = params[0];

            String myarray = imei.toLowerCase() + "" + Constant.createsession;

            String authcode = GlobalFunctions.generateSha(myarray);

            String apiURL = SESSIONURL + "&IMEI=" + imei.toLowerCase() + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";

            Log.d("URI", apiURL);

            HttpGet httpGet = new HttpGet(apiURL);
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 60000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 60000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            DefaultHttpClient httpClient = new DefaultHttpClient();//httpParameters
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            text = getASCIIContentFromEntity(entity);

            // Log.d("Data", "" + text);

        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            Log.d("Data", "Here1");
            text = "timeout";
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Data", "Here2");
            text = null;
        }

        return text;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        try {
            if (s != null) {

                if (s.equalsIgnoreCase("timeout")) {
                    myListener.QuerySuccessFul("timeout;timeout");
                } else {

                    try {
                        JSONObject json = new JSONObject(s);
                        JSONObject articles = json.getJSONObject("Session");
                        String sessionresult = articles.getString("Result");
                        String sessionid = articles.getString("SessionID");

                        if (sessionresult.equals("OK")) {
                            String result = "Success" + ";" + sessionid;
                            myListener.QuerySuccessFul(result);
                        } else {
                            myListener.QuerySuccessFul("Failed;FAILED");
                        }

                    } catch (JSONException e) {
                        myListener.QuerySuccessFul("Failed;FAILED");
                    }
                }
            } else {
                myListener.QuerySuccessFul("Failed;FAILED");
            }
        } catch (Exception e) {
        }


    }

    public interface QueryListener {
        void QuerySuccessFul(String data);

        //void QueryFailed();
    }


}
