package com.budgetload.materialdesign.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.ArrayList.PurchaseList;
import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.SendNotification;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.Constant.Indicators;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.GlobalVariables;
import com.budgetload.materialdesign.adapter.PurchaseAdapter;

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
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Purchase extends Fragment implements Constant, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    View rootView;
    ListView lvlpurchases;
    Dialog DetailsDialog;

    DataBaseHandler db;

    //Dialog Elements
    String mobile;
    String imei;
    String email;
    String transactionno;
    String SessionID;
    Boolean isrefreshed = false;
    Boolean isLoading = false;
    DecimalFormat dec;
    Context mcontext;
    TextView fetchingindicator;

    // PurchaseAdapter purchaseAdapter;
    private ArrayList<PurchaseList> purchaseLists;

    private SwipeRefreshLayout swipeRefreshLayout;
    AlertDialog.Builder emailDialog;
    AlertDialog.Builder invalidEmailDialog;

    LinearLayout listpurchases;
    RelativeLayout listempty;
    ScrollView scrollView1;
    String PartnerID;
    AlertDialog alert11;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_purchase, container, false);
        mcontext = getActivity();

        //initialized Database
        db = new DataBaseHandler(getActivity());
        //get IMEI
        imei = getIMEI();
        //get partnerid
        PartnerID = db.getPartnerID(db);
        //initialize purchanse list
        purchaseLists = new ArrayList<PurchaseList>();

        //get other info
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }

        //Initialize Objects
        lvlpurchases = (ListView) rootView.findViewById(R.id.purchasestxnlist);

        dec = new DecimalFormat("#,###,###.####");
        dec.setMinimumFractionDigits(2);

        DetailsDialog = new Dialog(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light));
        DetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DetailsDialog.setContentView(R.layout.pop_topup_details);
        DetailsDialog.setCancelable(true);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        listpurchases = (LinearLayout) rootView.findViewById(R.id.purchasedata);
        listempty = (RelativeLayout) rootView.findViewById(R.id.emptydata);
        scrollView1 = (ScrollView) rootView.findViewById(R.id.scrollView1);
        fetchingindicator = (TextView) rootView.findViewById(R.id.fetchingindicator);

        //Display data (purchases)
        displaydata();

        emailDialog = new AlertDialog.Builder(getActivity());
        emailDialog.setTitle("CONFIRMATION");
        emailDialog.setMessage("A notification with the link of transaction report will be sent to your mobile.");
        //Clicking the OK to send notification (DIALOG)
        emailDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                //Thread
                int constatus = NetworkUtil.getConnectivityStatusString(getActivity());

                if (constatus == 0) {
                    CheckInternet.showConnectionDialog(getActivity());
                } else {

                    CreateSession newsession = new CreateSession(getActivity());
                    newsession.setQueryListener(new CreateSession.QueryListener() {
                        @SuppressWarnings("unchecked")
                        public void QuerySuccessFul(String data) {
                            String[] rawdata = data.split(";");
                            if (rawdata[0].equals("Success")) {
                                SessionID = rawdata[1];
                                sendNotifications();
                            } else {

                                Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    newsession.execute(PartnerID);
                }


            }
        });
        //clicking cancel
        emailDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        invalidEmailDialog = new AlertDialog.Builder(getActivity());
        invalidEmailDialog.setTitle("CONFIRMATION");
        invalidEmailDialog.setMessage("Email address was not set, please update your profile with your correct email address.");
        invalidEmailDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert11 = emailDialog.create();

        lvlpurchases.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == 0) {

                    if (view.getLastVisiblePosition() + 1 >= view.getCount()) {
                        if (!isLoading) {
                            isLoading = true;
                            processSendingNotification();
                            isLoading = false;
                        }
                    }
                }

            }
        });


        Button clickableTextLink = (Button) rootView.findViewById(R.id.button);
        clickableTextLink.setOnClickListener(this);

        return rootView;


    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        //by ann,reinitialize variables
        imei = getIMEI();
        GlobalVariables.imei = imei;
        PartnerID = db.getPartnerID(db);
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }
        if (!getUserVisibleHint()) {
            return;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Uri uriUrl = Uri.parse("http://mybudgetload.com/scr/branches.asp");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
                break;

        }

    }

    @Override
    public void onRefresh() {

        if (!isrefreshed) {
            isrefreshed = true;
            swipeRefreshLayout.setRefreshing(true);

            fetchPurchaseData();

        }
    }

    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************


    public void fetchPurchaseData() {
        try {
            int constatus = NetworkUtil.getConnectivityStatusString(getActivity());
            if (constatus == 0) {
                CheckInternet.showConnectionDialog(getActivity());
                swipeRefreshLayout.setRefreshing(false);
            } else {
                fetchingindicator.setVisibility(View.VISIBLE);
                CreateSession newsession = new CreateSession(getActivity());
                newsession.setQueryListener(new CreateSession.QueryListener() {
                    @SuppressWarnings("unchecked")
                    public void QuerySuccessFul(String data) {
                        String[] rawdata = data.split(";");
                        if (rawdata[0].equals("Success")) {
                            SessionID = rawdata[1];
                            new fetchPurchases().execute();
                        } else {
                            fetchingindicator.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                newsession.execute(PartnerID);
            }
        } catch (Exception e) {
        }

    }

    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public void processSendingNotification() {

        Cursor mycursor = db.getPurchaseData(db);
        if (mycursor.getCount() > 25) {
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonbackground.setTextColor(Color.parseColor("#646464"));

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));

        }

    }

    public void displaydata() {

        try {
            Cursor mycursor = db.getPurchaseData(db);


            if (mycursor.getCount() > 0) {

                listpurchases.setVisibility(View.VISIBLE);
                listempty.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                scrollView1.setVisibility(View.GONE);

                while (mycursor.moveToNext()) {
                    PurchaseList newlist = new PurchaseList();

                    //String formatdate = convertDate(mycursor.getString(mycursor.getColumnIndex("DatePurchase")), "MM/dd/yyyy hh:mm:ss");

                    newlist.setTxnno(mycursor.getString(mycursor.getColumnIndex("TxnNo")));
                    newlist.setAmount(mycursor.getFloat(mycursor.getColumnIndex("Amount")));
                    newlist.setDatepurchase(mycursor.getString(mycursor.getColumnIndex("DatePurchase")));
                    newlist.setBranchid(mycursor.getString(mycursor.getColumnIndex("BranchID")));
                    newlist.setStatus(mycursor.getString(mycursor.getColumnIndex("TxnStatus")));
                    purchaseLists.add(newlist);
                }


                try {
                    PurchaseAdapter purchaseAdapter = new PurchaseAdapter(getActivity(), R.layout.list_topup, purchaseLists);
                    lvlpurchases.setAdapter(purchaseAdapter);
                    //by ann
                    if (Indicators.PurchaseFirstLoadIndicator.equals("true")) {
                        fetchPurchaseData();
                        Indicators.PurchaseFirstLoadIndicator = "false";
                    }
                } catch (Exception e) {

                }

            } else {
                listempty.setVisibility(View.VISIBLE);
                listpurchases.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                scrollView1.setVisibility(View.VISIBLE);
                fetchPurchaseData();
            }
        } catch (Exception e) {
        }

    }


    public void sendNotifications() {


//        JSONObject mymsg = new JSONObject();
//        mymsg.put("message", message);
//        mymsg.put("message_title", "Transfer Credits");
//        mymsg.put("sender", "BudgetLoad");
//        mymsg.put("datesent", newdate);
//
//        JSONObject myobj = new JSONObject();
//
//        myobj.put("to", "/topics/" + topic);
//        myobj.put("data", mymsg);
//
//        SendNotification sendNotification = new SendNotification(getActivity());
//        sendNotification.execute(mobile, SessionID, PartnerID, myobj.toString());

        try {



            String message = "Link for your transactions : http://mybudgetload.com/scr/purchasereport.asp?id=" + Constant.idcode + "&SourceMobile=0" + mobile + "&IMEI=" + imei + "&TxnType=PURCHASE&PartnerID=" + PartnerID + "";
            DateFormat df = new SimpleDateFormat("MMMM dd, yyyy HH:mm a");
            String newdate = df.format(Calendar.getInstance().getTime());
//
            try {
                JSONObject mJson = new JSONObject();
                mJson.put("message_title", "More Transactions");
                mJson.put("message", message);
                mJson.put("sender", "BUDGETLOAD");
                mJson.put("datesent", newdate);
                mJson.put("time_to_live", "200,000");

                JSONObject myobj = new JSONObject();

                myobj.put("to", "/topics/" + PartnerID + "_" + imei);
                myobj.put("data", mJson);

                SendNotification sendNotification = new SendNotification(getActivity());
                sendNotification.execute(mobile, SessionID, PartnerID, myobj.toString());

////                ParsePush push = new ParsePush();
////                push.setChannel(PartnerID + "_" + imei);
////                push.setData(mJson);
////                push.sendInBackground();
//
                Toast.makeText(getActivity(), "Notification sent. You will receive in a moment.", Toast.LENGTH_SHORT).show();
//
//
            } catch (Exception e) {
            }

        } catch (Exception e) {

        }

    }

    public String convertDate(String dateInMilliseconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateString = formatter.format(new Date(Long.parseLong(dateInMilliseconds)));

        return dateString;
    }

    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************

    class fetchPurchases extends AsyncTask<Void, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
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
        protected String doInBackground(Void... params) {

            String text;
            try {

                String authcode = GlobalFunctions.getSha1Hex(imei + mobile + Constant.purhasetxn + SessionID.toLowerCase());
                String apiURL = PURCHASEURL + "&IMEI=" + imei + "&SourceMobTel=" + mobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";


                //Log.d("URI", apiURL);
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
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                swipeRefreshLayout.setRefreshing(false);
                isrefreshed = false;
                fetchingindicator.setVisibility(View.GONE);

                if (s == null) {

                    Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();

                } else {

                    JSONArray json = new JSONArray(s);
                    JSONObject prefixarr = json.getJSONObject(0);
                    JSONObject articles = prefixarr.getJSONObject("PurchanseTransaction");
                    String sessionresult = articles.getString("Result");

                    if (sessionresult.equals("OK")) {
                        JSONObject networkprefix = json.getJSONObject(1);
                        JSONArray purchaseData = networkprefix.getJSONArray("PurchaseData");

                        db.savePurchases(db, purchaseData);
                        purchaseLists.clear();
                        displaydata();

                    }

                }

            } catch (JSONException e)

            {

            }
        }

    }

    //endregion

}
