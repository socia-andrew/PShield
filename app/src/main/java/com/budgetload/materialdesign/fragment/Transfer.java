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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.ArrayList.TransferList;
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
import com.budgetload.materialdesign.adapter.TransferAdapter;

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

public class Transfer extends Fragment implements Constant, SwipeRefreshLayout.OnRefreshListener {


    View rootView;
    ListView lvtransfer;
    Dialog DetailsDialog;

    TextView statusval;
    TextView txnval;
    TextView datetimeval;
    TextView senderval;
    TextView receiverval;
    TextView amountval;
    TextView retprevstockval;
    TextView retprevconsumedval;
    TextView retprevavailableval;
    TextView prebalval;
    TextView postbalval;
    TableRow prebalrow;
    TableRow postbalrow;
    Context mcontext;
    String email;
    TextView fetchingindicator;
    TextView sendercommunity;
    TextView receivercommunity;
    TextView receivername;

    DataBaseHandler db;

    String imei;
    String mobile;
    String transactionno;
    String SessionID;

    DecimalFormat dec;
    // TransferAdapter transferAdapter;
    private ArrayList<TransferList> transferlist;
    Boolean isrefreshed = false;
    Boolean isLoading = false;

    private SwipeRefreshLayout swipeRefreshLayout;
    AlertDialog.Builder emailDialog;
    AlertDialog.Builder invalidEmailDialog;
    ScrollView scrollView1;
    LinearLayout listtransfer;
    RelativeLayout listempty;
    String PartnerID;
    AlertDialog alert11;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_transfer, container, false);
        lvtransfer = (ListView) rootView.findViewById(R.id.stocktransferlist);

        mcontext = getActivity();
        DetailsDialog = new Dialog(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light));
        DetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DetailsDialog.setContentView(R.layout.pop_transfer_details);
        DetailsDialog.setCancelable(true);

        DetailsDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                lvtransfer.setEnabled(true);
            }
        });


        statusval = (TextView) DetailsDialog.findViewById(R.id.statusval);
        txnval = (TextView) DetailsDialog.findViewById(R.id.txnno);
        datetimeval = (TextView) DetailsDialog.findViewById(R.id.detailsdatehour);
        senderval = (TextView) DetailsDialog.findViewById(R.id.senderval);
        receiverval = (TextView) DetailsDialog.findViewById(R.id.receiverval);
        amountval = (TextView) DetailsDialog.findViewById(R.id.amountval);
        retprevstockval = (TextView) DetailsDialog.findViewById(R.id.retprevstockval);
        retprevconsumedval = (TextView) DetailsDialog.findViewById(R.id.retprevconsumedval);
        retprevavailableval = (TextView) DetailsDialog.findViewById(R.id.retprevavailableval);
        prebalval = (TextView) DetailsDialog.findViewById(R.id.prebalval);
        postbalval = (TextView) DetailsDialog.findViewById(R.id.postbalval);
        prebalrow = (TableRow) DetailsDialog.findViewById(R.id.row10);
        postbalrow = (TableRow) DetailsDialog.findViewById(R.id.row11);
        sendercommunity = (TextView) DetailsDialog.findViewById(R.id.sendercommunityval);
        receivercommunity = (TextView) DetailsDialog.findViewById(R.id.receivercommunityval);
        receivername = (TextView) DetailsDialog.findViewById(R.id.txtreceviername);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        db = new DataBaseHandler(getActivity());
        imei = getIMEI();
        PartnerID = db.getPartnerID(db);
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }

        listtransfer = (LinearLayout) rootView.findViewById(R.id.transferdata);
        listempty = (RelativeLayout) rootView.findViewById(R.id.emptydata);
        scrollView1 = (ScrollView) rootView.findViewById(R.id.scrollView1);
        fetchingindicator = (TextView) rootView.findViewById(R.id.fetchingindicator);


        displayTranser();


        dec = new DecimalFormat("###,###,###,###.####");
        dec.setMinimumFractionDigits(2);


        lvtransfer.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                lvtransfer.setEnabled(false);
                transactionno = ((TextView) view.findViewById(R.id.txnid)).getText().toString();
                displayTxnDetails();


            }

        });

        emailDialog = new AlertDialog.Builder(getActivity());
        emailDialog.setTitle("CONFIRMATION");
        emailDialog.setMessage("A notification with the link of transaction report will be sent to your mobile.");
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

        lvtransfer.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == 0) {

                    if (view.getLastVisiblePosition() + 1 >= view.getCount()) {
                        if (!isLoading) {
                            isLoading = true;
                            checkEmailAddress();
                            isLoading = false;
                        }
                    }
                }

            }
        });


        return rootView;

    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

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

            fetchTransferData();

        }
    }

    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void fetchTransferData() {
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
                            new fetchTransfer().execute();
                        } else {
                            Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                            fetchingindicator.setVisibility(View.GONE);

                        }
                    }
                });
                newsession.execute(PartnerID);
            }


        } catch (Exception e) {
        }


    }

    public void displayTranser() {

        try {
            Cursor cursor = db.getTransfer(db);
            transferlist = new ArrayList<TransferList>();

            if (cursor.getCount() > 0) {
                listtransfer.setVisibility(View.VISIBLE);
                listempty.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                scrollView1.setVisibility(View.GONE);
                while (cursor.moveToNext()) {
                    TransferList newlist = new TransferList();

                    String formatdate = convertDate(cursor.getString(cursor.getColumnIndex("DateTimeIN")), "MM/dd/yyyy hh:mm:ss");

                    newlist.setTxnNo(cursor.getString(cursor.getColumnIndex("TxnNo")));
                    newlist.setDatetimeIN(formatdate);
                    newlist.setSender(cursor.getString(cursor.getColumnIndex("Sender")));
                    newlist.setReceiver(cursor.getString(cursor.getColumnIndex("Receiver")));
                    newlist.setamount(cursor.getFloat(cursor.getColumnIndex("Amount")));
                    newlist.setprevstock(cursor.getFloat(cursor.getColumnIndex("RetailerPrevStock")));
                    newlist.setprevconsume(cursor.getFloat(cursor.getColumnIndex("RetailerPrevConsumed")));
                    newlist.setprevavailable(cursor.getFloat(cursor.getColumnIndex("RetailerPrevAvailable")));
                    newlist.setprebalance(cursor.getFloat(cursor.getColumnIndex("PrevBalance")));
                    newlist.setPostbalance(cursor.getFloat(cursor.getColumnIndex("PostBalance")));
                    newlist.setSenderCommunity(cursor.getString(cursor.getColumnIndex("SenderCommunity")));
                    newlist.setReceiverCommunity(cursor.getString(cursor.getColumnIndex("ReceiverCommunity")));


                    transferlist.add(newlist);
                }

                try {
                    TransferAdapter transferAdapter = new TransferAdapter(getActivity(), R.layout.list_transfer, transferlist);

                    lvtransfer.setAdapter(transferAdapter);

                    //by ann
                    if (Indicators.TransferFirstLoadLoadIndicator.equals("true")) {
                        fetchTransferData();
                        Indicators.TransferFirstLoadLoadIndicator = "false";
                    }

                } catch (Exception e) {

                }


            } else {

                listempty.setVisibility(View.VISIBLE);
                listtransfer.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                scrollView1.setVisibility(View.VISIBLE);
                fetchTransferData();

            }
        } catch (Exception e) {

        }

    }

    public void displayTxnDetails() {

        try {
            Cursor mycursor = db.getTransferDetails(db, transactionno);
            //Toast.makeText(getActivity(), "details" + mycursor.getCount() + "", Toast.LENGTH_LONG).show();
            if (mycursor.getCount() > 0) {
                mycursor.moveToFirst();
                do {
                    String formatdate = convertDate(mycursor.getString(mycursor.getColumnIndex("DateTimeIN")), "MM/dd/yyyy hh:mm:ss");
                    txnval.setText(mycursor.getString(mycursor.getColumnIndex("TxnNo")));
                    datetimeval.setText(formatdate);
                    senderval.setText(mycursor.getString(mycursor.getColumnIndex("Sender")));
                    receiverval.setText(mycursor.getString(mycursor.getColumnIndex("Receiver")));
                    amountval.setText("P " + dec.format(mycursor.getFloat(mycursor.getColumnIndex("Amount"))) + "");
                    retprevstockval.setText("P" + dec.format(mycursor.getFloat(mycursor.getColumnIndex("RetailerPrevStock"))) + "");
                    retprevconsumedval.setText("P" + dec.format(mycursor.getFloat(mycursor.getColumnIndex("RetailerPrevConsumed"))) + "");
                    retprevavailableval.setText("P" + dec.format(mycursor.getFloat(mycursor.getColumnIndex("RetailerPrevAvailable"))) + "");
                    prebalval.setText(dec.format(mycursor.getFloat(mycursor.getColumnIndex("PrevBalance"))));
                    postbalval.setText(dec.format(mycursor.getFloat(mycursor.getColumnIndex("PostBalance"))));
                    sendercommunity.setText(mycursor.getString(mycursor.getColumnIndex("SenderCommunity")));
                    receivercommunity.setText(mycursor.getString(mycursor.getColumnIndex("ReceiverCommunity")));
                    receivername.setText(mycursor.getString(mycursor.getColumnIndex("ReceiverName")));


                    //get last 10 digits of sender number
                    String substr = mycursor.getString(mycursor.getColumnIndex("Sender")).substring(mycursor.getString(mycursor.getColumnIndex("Sender")).length() - 10);

                    if (substr.equals(mobile)) {
                        prebalrow.setVisibility(View.VISIBLE);
                        postbalrow.setVisibility(View.VISIBLE);
                    } else {
                        prebalrow.setVisibility(View.GONE);
                        postbalrow.setVisibility(View.GONE);
                    }

                } while (mycursor.moveToNext());
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
            String message = "Link for your transactions : http://mybudgetload.com/scr/stocktransferreport.asp?id=" + Constant.idcode + "&SourceMobile=0" + mobile + "&IMEI=" + imei + "&TxnType=STOCKTRANSFER&PartnerID=" + PartnerID + "";
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

                SendNotification sendNotification = new SendNotification(getActivity());
                sendNotification.execute(mobile, SessionID, PartnerID, myobj.toString());

//                ParsePush push = new ParsePush();
//                push.setChannel(PartnerID + "_" + imei);
//                push.setData(mJson);
//                push.sendInBackground();

                Toast.makeText(getActivity(), "Notification sent. You will receive in a moment.", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
            }

        } catch (Exception e) {
        }


    }

    public void checkEmailAddress() {

        Cursor mycursor = db.getTopUpData(db);
        if (mycursor.getCount() > 25) {
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonbackground.setTextColor(Color.parseColor("#646464"));

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));

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

    class fetchTransfer extends AsyncTask<Void, Void, String> {

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
        protected String doInBackground(Void... params) {

            String text;
            try {

                String authcode = GlobalFunctions.getSha1Hex(imei + mobile + Constant.transfertxn + SessionID.toLowerCase());
                String apiURL = STOCKTRANSFERHISTORY + "&IMEI=" + imei + "&SourceMobTel=" + mobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";

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
                    Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                } else {

                    try {


                        JSONArray json = new JSONArray(s);
                        JSONObject prefixarr = json.getJSONObject(0);
                        JSONObject articles = prefixarr.getJSONObject("StockTransferTransaction");
                        String sessionresult = articles.getString("Result");


                        if (sessionresult.equals("OK")) {


                            JSONObject networkprefix = json.getJSONObject(1);
                            JSONArray topupdata = networkprefix.getJSONArray("StockTransferData");
                            db.saveTransferTxn(db, topupdata);

                            displayTranser();

                        }

                    } catch (JSONException e) {

                    }

                }
            } catch (Exception e) {
            }

        }
    }


    //endregion

}
