package com.budgetload.materialdesign.Common;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.activity.GlobalVariables;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by merlion on 11/26/15.
 */
public class SendNotification extends AsyncTask<String, Void, String> implements Constant {

    Context context;
    Activity activity;
    SendListener myListener;


    public SendNotification(Activity activity) {

        this.activity = activity;
        this.context = activity;

    }

    public void setSendListener(SendListener myListener) {
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


        String mobile = params[0];
        String SessionID = params[1];
        String PartnerID = params[2];
        String message = params[3];
//        String title = params[4];
//        String newdate = params[5];
//        String topic = params[6];
//        String sender = params[7];
        String text;

        //sendNotification.execute(mobile, SessionID, PartnerID, message, "BudgetLoad", newdate, topic);


//        try {
//            // Add your data
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
//                    2);
//            nameValuePairs.add(new BasicNameValuePair("cmd", "placeorder"));
//            nameValuePairs.add(new BasicNameValuePair("subcmd",
//                    "orderqueue"));
//            nameValuePairs.add(new BasicNameValuePair("custid", custid));
//            nameValuePairs
//                    .add(new BasicNameValuePair("custname", custname));
//            nameValuePairs.add(new BasicNameValuePair("totalnumofitems",
//                    totalnumofitems));
//            nameValuePairs.add(new BasicNameValuePair("totalamount",
//                    totalamount));
//            nameValuePairs.add(new BasicNameValuePair("imei", myIMEI));
//            nameValuePairs.add(new BasicNameValuePair("branchid", branch));
//            nameValuePairs.add(new BasicNameValuePair("totalbulk", "" + bulkcount));
//            nameValuePairs.add(new BasicNameValuePair("totalnonbulk", "" + nonbulkcount));
//
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity entity = response.getEntity();
//            text = getASCIIContentFromEntity(entity);
//
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//            text = null;
//        } catch (IOException e) {
//            text = null;
//            e.printStackTrace();
//        }


        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constant.OTHERSURL);

        try {

            String authcode = GlobalFunctions.getSha1Hex(GlobalVariables.imei.toLowerCase() + mobile + "M I S C E L L A N E O U S" + SessionID.toLowerCase());

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("CMD", "SendNotification"));
            nameValuePairs.add(new BasicNameValuePair("PartnerID", PartnerID));
            nameValuePairs.add(new BasicNameValuePair("id", "d6b27ee9de22e000700f0163e662ecba1d201c99"));
            nameValuePairs.add(new BasicNameValuePair("IMEI", GlobalVariables.imei.toLowerCase()));
            nameValuePairs.add(new BasicNameValuePair("SourceMobTel", mobile));
            nameValuePairs.add(new BasicNameValuePair("SessionNo", SessionID.toLowerCase()));
            nameValuePairs.add(new BasicNameValuePair("AuthCode", authcode));
            nameValuePairs.add(new BasicNameValuePair("message", message));


            // String apiURL = Constant.OTHERSURL + "&IMEI=" + GlobalVariables.imei.toLowerCase() + "&SourceMobTel=" + mobile + "&SessionNo=" + SessionID.toLowerCase() + "&AuthCode=" + authcode
            //         + "&PartnerID=" + MainActivity.PartnerID + "&CMD=SendNotification&message=" + message + "";


            //&title=" + title + "&topic=" + topic + "&mydate=" + newdate + "&sender="+sender+"


            //Log.d("URI", apiURL);
//
//            HttpGet httpGet = new HttpGet(apiURL);
//            HttpParams httpParameters = new BasicHttpParams();
//            int timeoutConnection = 3000;
//            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//            int timeoutSocket = 5000;
//            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//
//            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
//            HttpResponse response = httpClient.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            text = getASCIIContentFromEntity(entity);

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            text = getASCIIContentFromEntity(entity);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            text = null;
        } catch (IOException e) {
            text = null;
            e.printStackTrace();
        }
        return text;

    }

//    @Override
//    protected String doInBackground(Void... params) {
//
//
//
//
////        SessionID = params[0];
////        imei = params[1];
////        email = params[2];
////        txntype = params[3];
////        PartnerID = params[4];
//
//        //String authcode = GlobalFunctions.getSha1Hex(imei+ mobile + "M O R E  T R A N S A C T I O N" + SessionID.toLowerCase());
//        //String apiURL =  NOTIFICATION+"&IMEI="+imei+"&SourceMobTel="+mobile+"&SessionNo="+SessionID.toLowerCase()+"&AuthCode="+authcode+"&TxnType="+txntype+"&PartnerID="+PartnerID+"&ToDo=TRANSACTION";
//        //HttpClient httpClient = new DefaultHttpClient();
//        //HttpContext localContext = new BasicHttpContext();
//
//        // Log.d("URI", apiURL);
//
//        //HttpGet httpGet = new HttpGet(apiURL);
//
////
////
////        String text = null;
////        try {
////            HttpResponse response = httpClient.execute(httpGet,
////                    localContext);
////            HttpEntity entity = response.getEntity();
////            text = getASCIIContentFromEntity(entity);
////
////        } catch (Exception e) {
////            text = null;
////            return e.getLocalizedMessage();
////        }
////        return text;
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//
//        String myData = "{\"to\":\"/topics/EXPRESSPAY_000000000000000\",\"data\": {\"message\" : \"hello\",\"message_title\": \"BudgetLoad\",\"sender\":\"EXPRESSPAY\",\"datesent\":\"August%2031,%202016%206:16%20PM\"}}";
//
//
//        URL url;
//        HttpURLConnection urlConnection = null;
//        //String response = "";
//        String text = "";
//        try {
//            // url = new URL("https://fcm.googleapis.com/fcm/send");
//
//            try {
//                //String PartnerID = params[0];
//                //String authcode = GlobalFunctions.getSha1Hex(GlobalVariables.imei.toLowerCase() + "C R E A T E  S E S S I O N");
//                //String apiURL = SESSIONURL + "&IMEI=" + GlobalVariables.imei.toLowerCase() + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";
//
//                // Log.d("URI", apiURL);
//
//                HttpGet httpGet = new HttpGet("https://fcm.googleapis.com/fcm/send" + myData);
//                httpGet.setHeader("Content-Type", "application/json");
//                httpGet.setHeader("Authorization", "key=AIzaSyDJ6eQTKXTYWkTcosHPrwJkKh3LyxXeZ7Y");
//                HttpParams httpParameters = new BasicHttpParams();
//                int timeoutConnection = 60000;
//                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//                int timeoutSocket = 60000;
//                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//
//                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
//                HttpResponse response = httpClient.execute(httpGet);
//                HttpEntity entity = response.getEntity();
//                text = getASCIIContentFromEntity(entity);
//
//            } catch (ConnectTimeoutException e) {
//                text = "timeout";
//            } catch (Exception e) {
//                text = null;
//            }
//
//            //   urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded\"");
//            //   urlConnection.setRequestProperty("Authorization", "key=AIzaSyDJ6eQTKXTYWkTcosHPrwJkKh3LyxXeZ7Y");
//            //   urlConnection.setRequestMethod("POST");
//            //    urlConnection.setDoInput(true);
//            //    urlConnection.setDoOutput(true);
//            //    urlConnection = (HttpURLConnection) url
//            //           .openConnection();
//
//            //InputStream in = urlConnection.getInputStream();
//            // InputStreamReader isw = new InputStreamReader(in);
//
////            OutputStream os = urlConnection.getOutputStream();
////            BufferedWriter writer = new BufferedWriter(
////                    new OutputStreamWriter(os, "UTF-8"));
////            writer.write(myData);
////
////            writer.flush();
////            writer.close();
////            os.close();
////
////            Log.d("Notification", "" + urlConnection);
////
////            urlConnection.connect();
////
////
////            int responseCode = urlConnection.getResponseCode();
////
////            if (responseCode == HttpsURLConnection.HTTP_OK) {
////                String line;
////                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
////                while ((line = br.readLine()) != null) {
////                    response += line;
////                }
////            } else {
////                response = "";
////
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Log.d("Notificaiton", text);
//
//        return text;
//
//    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        try {
            Log.d("Notification", s);

        } catch (Exception e) {
            Log.d("Notification", s);
        }


//        Log.d("NOTIFICATIONRESULT", "dsadhere" + s);
//        myListener.SendSuccessFul("Success" + ";Notification sent. You will receive in a moment.");

//        if (s != null) {
//            db = new DataBaseHandler(context);
//
//            try {
//
//                JSONObject json = new JSONObject(s);
//                JSONObject articles = json.getJSONObject("Notifcation");
//                stringresult = articles.getString("Result");
//                String ResultMessage = articles.getString("Message");
//
//
//
//                if (stringresult.equals("OK")) {
//                    myListener.SendSuccessFul("Success" + ";Notification sent. You will receive in a moment.");
//
//                } else {
//                    myListener.SendSuccessFul("Failed" + ";" + ResultMessage);
//
//                }
//
//
//            } catch (JSONException e) {
//                Log.d("NOTIFICATIONRESULT","dsad"+e.toString() + stringresult);
//            }
//        }


    }


    public interface SendListener {
        void SendSuccessFul(String data);
    }

}
