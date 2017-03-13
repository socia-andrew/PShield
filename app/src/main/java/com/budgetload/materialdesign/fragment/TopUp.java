package com.budgetload.materialdesign.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.ArrayList.TopupList;
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
import com.budgetload.materialdesign.adapter.TopupAdapter;

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

public class TopUp extends Fragment implements Constant, SwipeRefreshLayout.OnRefreshListener {

    //declaration here
    View rootView;
    ListView lvtopuptxnlist;
    Dialog DetailsDialog;
    DataBaseHandler db;
    //Dialog Elements
    TextView txtTxn;
    TextView txtdatetime;
    TextView txtamount;
    TextView txtdescription;
    TextView txtprebalance;
    TextView txtpostbalance;
    TextView txtdiscount;
    TextView txtmobile;
    TextView txtstatus;
    ImageView myImage;
    String mobile;
    String imei;
    //updated by ann
    TextView totalpending;
    String TotalPending = "0";
    LinearLayout listtopup;
    RelativeLayout listempty;
    String transactionno;
    String SessionID;
    String email;
    Boolean isrefreshed = false;
    Boolean isLoading = false;
    DecimalFormat dec;
    Context mcontext;

    //TopupAdapter topupAdapter;
    private ArrayList<TopupList> topupLists;
    private SwipeRefreshLayout swipeRefreshLayout;

    AlertDialog.Builder emailDialog;
    AlertDialog.Builder invalidEmailDialog;

    ScrollView scrollView1;
    TopupAdapter topupAdapter;
    String PartnerID;
    AlertDialog alert11;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_topup, container, false);
        mcontext = getActivity();

        //initialize database
        db = new DataBaseHandler(getActivity());

        //Get IMEI
        imei = getIMEI();
        //get partner ID
        PartnerID = db.getPartnerID(db);

        //get other info
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }

        //intialize objects
        lvtopuptxnlist = (ListView) rootView.findViewById(R.id.topuptxnlist);

        dec = new DecimalFormat("#,###,###.####");
        dec.setMinimumFractionDigits(2);

        DetailsDialog = new Dialog(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light));
        DetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DetailsDialog.setContentView(R.layout.pop_topup_details);
        DetailsDialog.setCancelable(true);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        txtTxn = (TextView) DetailsDialog.findViewById(R.id.txnno);
        txtstatus = (TextView) DetailsDialog.findViewById(R.id.txnstatval);
        txtmobile = (TextView) DetailsDialog.findViewById(R.id.mobilenoval);
        txtamount = (TextView) DetailsDialog.findViewById(R.id.amountval);
        txtdatetime = (TextView) DetailsDialog.findViewById(R.id.detailsdatehour);
        txtdescription = (TextView) DetailsDialog.findViewById(R.id.descval);
        txtprebalance = (TextView) DetailsDialog.findViewById(R.id.prevbalval);
        txtpostbalance = (TextView) DetailsDialog.findViewById(R.id.postbalval);
        txtdiscount = (TextView) DetailsDialog.findViewById(R.id.discountval);
        myImage = (ImageView) DetailsDialog.findViewById(R.id.imageView1);


        emailDialog = new AlertDialog.Builder(getActivity());
        emailDialog.setTitle("CONFIRMATION");
        emailDialog.setMessage("A notification with the link of transaction report will be sent to your mobile.");

        //Clicking OK on the promp dialog to send notification
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
        //clicking cancel on the dialog
        emailDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        alert11 = emailDialog.create();


        invalidEmailDialog = new AlertDialog.Builder(getActivity());
        invalidEmailDialog.setTitle("CONFIRMATION");
        invalidEmailDialog.setMessage("Email address was not set, please update your profile with your correct email address.");
        invalidEmailDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        DetailsDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                lvtopuptxnlist.setEnabled(true);
            }
        });

        listtopup = (LinearLayout) rootView.findViewById(R.id.topupdata);
        listempty = (RelativeLayout) rootView.findViewById(R.id.emptydata);
        scrollView1 = (ScrollView) rootView.findViewById(R.id.scrollView1);


        lvtopuptxnlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                lvtopuptxnlist.setEnabled(false);
                transactionno = ((TextView) view.findViewById(R.id.txnno)).getText().toString();
                displayTxnDetails();
            }
        });

        lvtopuptxnlist.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) { //meaning it reach the last
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

        displayData();


        return rootView;

    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //onResume();
        } else if (isVisibleToUser && isResumed()) {
            onResume();
        } else {
        }
    }

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
    public void onRefresh() {
        // TODO Auto-generated method stub

        if (!isrefreshed) {

            isrefreshed = true;
            swipeRefreshLayout.setRefreshing(true);

            fetchTopUpData();

        }


    }

    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void fetchTopUpData() {
        int constatus = NetworkUtil.getConnectivityStatusString(getActivity());

        //by ann. this will show the indicator of fetching data
        Indicators.TopUpIndicator = "false";
        Indicators.TopUpFirstLoadIndicator = "false";
        try {
            totalpending = (TextView) rootView.findViewById(R.id.numberpending);
            totalpending.setText("Fetching Data...");
            totalpending.setVisibility(View.VISIBLE);

        } catch (Exception e) {
        }

        if (constatus == 0) {
            CheckInternet.showConnectionDialog(getActivity());
            totalpending.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            CreateSession newsession = new CreateSession(getActivity());
            newsession.setQueryListener(new CreateSession.QueryListener() {
                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {
                    String[] rawdata = data.split(";");
                    if (rawdata[0].equals("Success")) {

                        SessionID = rawdata[1];

                        String network;

                        Cursor cursor = db.getNetwork(db);
                        while (cursor.moveToNext()) {
                            network = cursor.getString(cursor.getColumnIndex("Network"));
                            if (!network.equalsIgnoreCase("PREFIX")) {

                                new fetchTransaction().execute(network);
                            }
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();

                        //by ann,
                        Indicators.TopUpIndicator = "true";
                        try {
                            totalpending = (TextView) rootView.findViewById(R.id.numberpending);
                            totalpending.setText("Fetching Data Failed. Drag down to refresh.");
                            swipeRefreshLayout.setRefreshing(false);

                        } catch (Exception e) {
                        }
                    }
                }
            });
            newsession.execute(PartnerID);
        }
    }

    public void processSendingNotification() {

        Cursor mycursor = db.getTopUpData(db);
        if (mycursor.getCount() > 5) {
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonbackground.setTextColor(Color.parseColor("#646464"));

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));
        }
    }

    public void showPending() {

        //updated by ann
        if (!TotalPending.equals("0")) {
            scrollView1.setVisibility(View.GONE);
            try {
                totalpending = (TextView) rootView.findViewById(R.id.numberpending);
                totalpending.setVisibility(View.VISIBLE);
                totalpending.setText(TotalPending + " Pending Transaction/s. Drag down to refresh.");
            } catch (Exception e) {
            }

        } else {
            totalpending = (TextView) rootView.findViewById(R.id.numberpending);
            totalpending.setVisibility(View.GONE);

        }

    }

    public void displayData() {

        try {
            Cursor mycursor = db.getTopUpData(db);
            topupLists = new ArrayList<TopupList>();

            if (topupAdapter != null) {
                topupAdapter = null;
            }


            if (mycursor.getCount() > 0) {


                listtopup.setVisibility(View.VISIBLE);
                listempty.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                scrollView1.setVisibility(View.GONE);

                while (mycursor.moveToNext()) {
                    TopupList newlist = new TopupList();
                    String formatdate = convertDate(mycursor.getString(mycursor.getColumnIndex("DateTimeIN")), "MM/dd/yyyy hh:mm:ss");
                    newlist.setTxnid(mycursor.getString(mycursor.getColumnIndex("TxnNo")));
                    newlist.setAmount(mycursor.getFloat(mycursor.getColumnIndex("Amount")));
                    newlist.setDatetime(formatdate);
                    newlist.setMobile(mycursor.getString(mycursor.getColumnIndex("TargetMobile")));
                    newlist.setStatus(mycursor.getString(mycursor.getColumnIndex("TxnStatus")));
                    newlist.setBlRefNo(mycursor.getString(mycursor.getColumnIndex("BudgetLoadRef")));
                    topupLists.add(newlist);
                }

                // by ann. this indicator used to check if the user have recent transaction
                if (Indicators.TopUpIndicator.equals("true") || Indicators.TopUpFirstLoadIndicator.equals("true")) {
                    fetchTopUpData();
                }

                if (!TotalPending.equals("0")) {
                    showPending();
                }


            } else {
                listempty.setVisibility(View.VISIBLE);
                listtopup.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                scrollView1.setVisibility(View.VISIBLE);
                fetchTopUpData();

            }

            try {

                topupAdapter = new TopupAdapter(getActivity(), R.layout.list_topup, topupLists);
                lvtopuptxnlist.setAdapter(topupAdapter);
            } catch (Exception e) {

            }

        } catch (Exception e) {
        }


    }

    public void displayTxnDetails() {

        try {
            String mynetwork = "";

            Cursor mycursor = db.getTopUpDetails(db, transactionno);


            if (mycursor.getCount() > 0) {
                listtopup.setVisibility(View.VISIBLE);
                listempty.setVisibility(View.GONE);
                mycursor.moveToFirst();
                do {
                    String formatdate = convertDate(mycursor.getString(mycursor.getColumnIndex("DateTimeIN")), "MM/dd/yyyy hh:mm:ss");

                    txtTxn.setText(mycursor.getString(mycursor.getColumnIndex("TxnNo")));
                    txtdatetime.setText(formatdate);
                    //txtdatetime.setText(mycursor.getString(mycursor.getColumnIndex("DateTimeIN")));
                    txtamount.setText(dec.format(Double.parseDouble(mycursor.getString(mycursor.getColumnIndex("Amount")))));
                    txtdescription.setText(mycursor.getString(mycursor.getColumnIndex("ProductDescription")));
                    txtprebalance.setText(dec.format(Double.parseDouble(mycursor.getString(mycursor.getColumnIndex("PrevBalance")))));
                    txtpostbalance.setText(dec.format(Double.parseDouble(mycursor.getString(mycursor.getColumnIndex("PostBalance")))));
                    txtdiscount.setText(dec.format(Double.parseDouble(mycursor.getString(mycursor.getColumnIndex("Discount")))));
                    txtstatus.setText(mycursor.getString(mycursor.getColumnIndex("TxnStatus")));
                    txtmobile.setText(mycursor.getString(mycursor.getColumnIndex("TargetMobile")));
                    mynetwork = mycursor.getString(mycursor.getColumnIndex("NetworkID"));
                } while (mycursor.moveToNext());

            } else {
                listtopup.setVisibility(View.VISIBLE);
                listempty.setVisibility(View.GONE);
            }

            if (mynetwork.equalsIgnoreCase("GLOBE")) {
                myImage.setImageResource(R.drawable.globe);
            } else if (mynetwork.equalsIgnoreCase("SUN")) {
                myImage.setImageResource(R.drawable.sun);
            } else {//(mynetwork.equalsIgnoreCase("SMART"))
                myImage.setImageResource(R.drawable.smart);
            }


            DetailsDialog.show();

        } catch (Exception e) {
        }


    }

    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }


    public void sendNotifications() {

        try {


            String message = "Link for your transactions : http://mybudgetload.com/scr/topupreport.asp?id=" + Constant.idcode + "&SourceMobile=0" + mobile + "&IMEI=" + imei + "&TxnType=SMART&PartnerID=" + PartnerID + "";

            DateFormat df = new SimpleDateFormat("MMMM dd, yyyy HH:mm a");
            String newdate = df.format(Calendar.getInstance().getTime());

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
//                ParsePush push = new ParsePush();
//                push.setChannel(PartnerID + "_" + imei);
//                push.setData(mJson);
//                push.sendInBackground();

                SendNotification sendNotification = new SendNotification(getActivity());
                sendNotification.execute(mobile, SessionID, PartnerID, myobj.toString());

                Toast.makeText(getActivity(), "Notification sent. You will receive in a moment.", Toast.LENGTH_SHORT).show();

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

    class fetchTransaction extends AsyncTask<String, Void, String> {

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

            String brand = params[0];
            String text;

            try {


                String authcode = GlobalFunctions.getSha1Hex(imei + mobile + Constant.topuptxn + SessionID.toLowerCase());
                String apiURL = TXNHISTURL + "&IMEI=" + imei + "&SourceMobTel=" + mobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&Network=" + brand + "&PartnerID=" + PartnerID + "";


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
            }
            return text + ";" + brand;

        }

        @Override
        protected void onPostExecute(String s) {

            // swipeRefreshLayout.setRefreshing(false);
            isrefreshed = false;

            String[] mydata = s.split(";");

            if (mydata[0] == null) {
                Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    JSONArray json = new JSONArray(mydata[0]);
                    JSONObject prefixarr = json.getJSONObject(0);
                    JSONObject articles = prefixarr.getJSONObject("TopUpTransaction");
                    String sessionresult = articles.getString("Result");
                    //updated by ann
                    TotalPending = articles.getString("TotalPending");

                    if (sessionresult.equals("OK")) {

                        JSONObject networkprefix = json.getJSONObject(1);
                        JSONArray topupdata = networkprefix.getJSONArray("TopUpData");
                        db.saveTopTxn(db, topupdata, mydata[1]);

                        topupLists.clear();
                        displayData();
                        showPending();
                        swipeRefreshLayout.setRefreshing(false);
                        Indicators.WalletFetchIndicator = "true"; //set to true to re query wallet when go to topup

                    } else {

                        //by ann
                        Indicators.TopUpIndicator = "true";
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            totalpending = (TextView) rootView.findViewById(R.id.numberpending);
                            totalpending.setText("Fetching Data Failed. Drag down to refresh.");

                        } catch (Exception e) {
                        }
                    }
                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

        }
    }


    //endregion


}
