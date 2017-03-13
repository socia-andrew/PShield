package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.Deactivated;
import com.budgetload.materialdesign.Common.DialogError;
import com.budgetload.materialdesign.Common.FetchWallet;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.HideKeyboard;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.RequestCredits;
import com.budgetload.materialdesign.Common.SendNotification;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.Constant.Indicators;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.google.android.gms.ads.AdView;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StockTransfer extends Fragment implements View.OnClickListener, Constant {

    //region INITIALIZATION
    EditText txtamount;
    EditText txttargetmobile;
    ImageView imgcontact;
    TextView txtMyWallet;
    ImageView mywalleticon;
    ImageView imgrequestcredits;
    EditText community;
    String networkprfix;
    String brand;
    String imei;
    String mobile;
    String targetmobile;
    String amount;
    String referenceno;
    String SessionID;
    String PartnerID;
    int walletoff;
    int walleton;
    String selectedCommID;
    String selectedCommName;
    int imgClear;
    View rootView;
    Context mcontext;
    DataBaseHandler db;
    FloatingActionButton fab;
    JSONArray commlist;
    EditText inputpass;
    AlertDialog confimation;
    AlertDialog.Builder builder;
    String PasswordStatus = "";
    String confimpass = "";
    String CurrentPassword = "";
    String sendername = "";
    AlertDialog.Builder alertDialog;
    //endregion
    AdView mAdView1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stocktransfer, container, false);

        //Initializing Database
        db = new DataBaseHandler(getActivity());
        mcontext = getActivity();

//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView1 = (AdView) rootView.findViewById(R.id.adView1);
//        mAdView1.loadAd(adRequest);

        //Get partner ID
        PartnerID = db.getPartnerID(db);

        //Get IMEI
        imei = getIMEI();

        //get Other info
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }

        //getPassword Status
        Cursor c = db.getPassword(db);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                PasswordStatus = c.getString(c.getColumnIndex("PasswordStatus"));
                CurrentPassword = c.getString(c.getColumnIndex("Password"));
            } while (c.moveToNext());
        }

        //region Initializing Object
        txtMyWallet = (TextView) rootView.findViewById(R.id.txtMyWallet);
        mywalleticon = (ImageView) rootView.findViewById(R.id.walleticon);
        mywalleticon.setOnClickListener(this);
        txttargetmobile = (EditText) rootView.findViewById(R.id.txttargetmobile);
        txtamount = (EditText) rootView.findViewById(R.id.txtAmount);
        imgcontact = (ImageView) rootView.findViewById(R.id.mycontact);
        imgcontact.setOnClickListener(this);
        imgClear = R.drawable.xbutton;
        imgrequestcredits = (ImageView) rootView.findViewById(R.id.requestcredits);

        walletoff = R.drawable.walletsmalloff;
        walleton = R.drawable.wallet;

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setCancelable(false);
        //endregion


        //region COnfirmation for Stock Transfer
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation");
        inputpass = new EditText(getActivity());
        inputpass.setHint("Password");
        inputpass.setPadding(60, 10, 60, 10);
        inputpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputpass, 10, 100, 10, 20);
        builder.setCancelable(false);


        builder.setPositiveButton("Transfer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                confimpass = GlobalFunctions.getSha1Hex(inputpass.getText().toString());
                targetmobile = txttargetmobile.getText().toString();
                amount = txtamount.getText().toString();
                verifySession();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fab.setEnabled(true);
                inputpass.setText("");

            }
        });
        confimation = builder.create();


        //endregion

        //region INLINE TRIGGERS

        txttargetmobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (count > 0) {

                    txttargetmobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgClear, 0);


                } else {
                    txttargetmobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 11) {

                    txtamount.setText("");

                    Character num = txttargetmobile.getText().toString().charAt(0);
                    if (!num.toString().equals("0")) {
                        txtamount.setVisibility(View.GONE);
                        txttargetmobile.setText("");
                        community.setVisibility(View.GONE);
                        community.setText("");
                        Toast.makeText(getActivity(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                    } else {
                        networkprfix = txttargetmobile.getText().toString().substring(1, 4);
                        brand = db.getNetworkPrefix(db, networkprfix);
                        checkTargetMobileCommunity(); //update
                        if (brand.length() == 0) {
                            Toast.makeText(getActivity(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                            txtamount.setVisibility(View.GONE);
                        } else {
                            HideKeyboard.hideKeyboard(getActivity());

                        }
                    }

                }

            }
        });
        community = (EditText) rootView.findViewById(R.id.community);

        //seting wallet value
        Cursor crWallet = db.getWallet(db);
        if (crWallet.getCount() > 0) {
            crWallet.moveToFirst();
            do {
                try {
                    txtMyWallet.setText("" + crWallet.getString(crWallet.getColumnIndex("Balance")) + "");
                } catch (Exception e) {
                }


            } while (crWallet.moveToNext());
        } else {
            try {
                txtMyWallet.setText("0.00 ");
            } catch (Exception e) {
            }

        }

        Bundle mybundle = getArguments();

        if (mybundle != null) {
            String mobile = mybundle.getString("mobile").toString();
            if (mobile.length() > 0) {
                try {
                    HideKeyboard.hideKeyboard(getActivity());
                    txttargetmobile.setText(mobile);
                } catch (Exception e) {
                }

            }
        }

        //Target Mobile listener
        txttargetmobile.setOnTouchListener(new View.OnTouchListener() {

            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (txttargetmobile.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                        int leftEdgeOfRightDrawable = txttargetmobile.getRight()
                                - txttargetmobile.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                        leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.EdtPadding);
                        if (event.getRawX() >= leftEdgeOfRightDrawable) {
                            txttargetmobile.setText("");
                            txtamount.setText("");
                            txtamount.setVisibility(View.GONE);
                            community.setText("");
                            community.setVisibility(View.GONE);
                            fab.setVisibility(View.GONE);
                            return true;
                        }

                    }

                }
                return false;
            }

        });

        txtamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    try {
                        if (Integer.parseInt(txtamount.getText().toString()) > 0) {
                            fab.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                    }

                }

            }
        });

        //Selecting community (opening community list)
        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commlist != null) {
                    Intent intent = new Intent(getActivity(), CommunityList.class);
                    Bundle b = new Bundle();
                    b.putString("Community", commlist.toString());
                    intent.putExtras(b);
                    startActivityForResult(intent, 1);

                } else {
                    Toast.makeText(getActivity(), "Community List failed to load. Please close the app and open again.", Toast.LENGTH_LONG).show();

                }
            }


        });

        Cursor cr_profile = db.getProfile(db);

        while (cr_profile.moveToNext()) {


            if (!cr_profile.getString(cr_profile.getColumnIndex("FirstName")).equalsIgnoreCase("None")) {
                sendername = cr_profile.getString(cr_profile.getColumnIndex("FirstName"));
            }


            if (!cr_profile.getString(cr_profile.getColumnIndex("MiddleName")).equalsIgnoreCase("None")) {
                sendername = sendername + " " + cr_profile.getString(cr_profile.getColumnIndex("MiddleName"));
            }


            if (!cr_profile.getString(cr_profile.getColumnIndex("LastName")).equalsIgnoreCase("None")) {
                sendername = sendername + " " + cr_profile.getString(cr_profile.getColumnIndex("LastName"));
            }


        }

        imgrequestcredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                RequestCredits.showRequestDialog(mcontext, getActivity(), PartnerID, mobile);
                v.setEnabled(true);
            }
        });
        //endregion


        return rootView;
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

        txttargetmobile = (EditText) rootView.findViewById(R.id.txttargetmobile);
        txttargetmobile.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(txttargetmobile, 0);
            }
        }, 200);
    }


    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.fab:

                try {
                    fab.setEnabled(false);

                    Character num = txttargetmobile.getText().toString().charAt(0);
                    if (!num.toString().equals("0")) {
                        txtamount.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                        fab.setEnabled(true);
                    } else {

                        if (txtamount.getText().length() > 0) {

                            if (Integer.parseInt(txtamount.getText().toString()) > 0) {
                                confirmdialog();
                            } else {
                                DialogError.alertDialogShow(getActivity(), "BudgetLoad", "Invalid amount");
                                fab.setEnabled(true);

                            }
                        } else {
                            DialogError.alertDialogShow(getActivity(), "BudgetLoad", "Please provide a valid amount.");
                            fab.setEnabled(true);
                        }
                    }
                } catch (Exception e) {
                }


                break;

            case R.id.mycontact:
                imgcontact.setEnabled(false);
                Intent myintent = new Intent(getActivity(), ContactList.class);
                startActivityForResult(myintent, 5);
                break;


            case R.id.walleticon:

                try {
                    int constatus = NetworkUtil.getConnectivityStatusString(getActivity());

                    if (constatus == 0) {
                        CheckInternet.showConnectionDialog(getActivity());
                    } else {

                        mywalleticon.setEnabled(false);
                        walleticonstat(1);
                        progressDialog.showDialog(getActivity(), "BudgetLoad", "Fetching Wallet... please wait", false);
                        CreateSession newsession = new CreateSession(getActivity());

                        newsession.setQueryListener(new CreateSession.QueryListener() {
                            @SuppressWarnings("unchecked")
                            public void QuerySuccessFul(String data) {

                                String[] rawdata = data.split(";");
                                if (rawdata[0].equals("Success")) {
                                    SessionID = rawdata[1];
                                    wallet();
                                } else {
                                    Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                    walleticonstat(0);
                                    progressDialog.hideDialog();

                                }

                            }

                        });


                        newsession.execute(PartnerID);


                    }
                } catch (Exception e) {
                }


                break;


        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imgcontact.setEnabled(true);

        if (requestCode == 5) {

            if (resultCode == Activity.RESULT_OK) {
                String mobile = data.getStringExtra("Contact");
                String newmobile = mobile.substring(Math.max(0, mobile.length() - 10));
                txttargetmobile.setText("0" + newmobile);
            }
        }

        if (requestCode == 1 && Indicators.SuccessTransferIndicator.equals("true")) {
            Indicators.SuccessTransferIndicator = "false";
            txttargetmobile.setText("");
            txtamount.setText("");
            txtamount.setVisibility(View.GONE);
            community.setText("");
            community.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            if (resultCode == 3) {
                callTransaction.theMethod();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.stocktransfer);
            }
        }

        //call back after selecting community
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                selectedCommID = data.getStringExtra("ComID");
                selectedCommName = data.getStringExtra("ComName");
                community.setText(data.getStringExtra("ComName"));

                txtamount.setVisibility(View.VISIBLE);


            }
        }


    }

    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void verifySession() {

        try {

            if ((PasswordStatus.equals("on") && confimpass.equals(CurrentPassword)) || PasswordStatus.equals("off") || PasswordStatus.equals("")) {
                progressDialog.showDialog(getActivity(), "Transfer", "Processing Stock Transfer. Please wait...", false);
                int status = NetworkUtil.getConnectivityStatusString(getActivity());

                if (status == 0) {
                    CheckInternet.showConnectionDialog(getActivity());
                    progressDialog.hideDialog();
                } else {

                    CreateSession newsession = new CreateSession(getActivity());
                    newsession.setQueryListener(new CreateSession.QueryListener() {
                        @SuppressWarnings("unchecked")
                        public void QuerySuccessFul(String data) {

                            String[] rawdata = data.split(";");
                            if (rawdata[0].equals("Success")) {
                                SessionID = rawdata[1];
                                new processStockTransfer().execute();
                            } else {
                                fab.setEnabled(true);
                                progressDialog.hideDialog();
                                Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                            }

                        }

                    });

                    newsession.execute(PartnerID);

                }
            } else {
                fab.setEnabled(true);
                Toast.makeText(getActivity(), "Password is incorrect.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            progressDialog.hideDialog();
        }


    }

    public String getIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getDeviceId();

    }

    public void confirmdialog() {
        try {
            String message = "Are you sure you want to Transfer " + txtamount.getText().toString() + " wallet credits to " + txttargetmobile.getText().toString() + "?";


            if (PasswordStatus.equals("on")) {
                inputpass.setVisibility(View.VISIBLE);
            } else {
                inputpass.setVisibility(View.GONE);
            }

            confimation.setMessage(message);
            confimation.show();
            confimation.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#646464"));
            confimation.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#19BD4E"));

        } catch (Exception e) {
        }

    }

    private GoToTransactions callTransaction;

    public void onAttach(Activity activity) {
        callTransaction = (GoToTransactions) activity;
        super.onAttach(activity);
    }

    public interface GoToTransactions {
        void theMethod();
    }

    public void walleticonstat(Integer val) {

        if (val.equals(1)) {
            mywalleticon.setBackgroundResource(walletoff);
        } else {
            mywalleticon.setBackgroundResource(walleton);
        }
    }

    public void wallet() {

        try {
            FetchWallet fetchWallet = new FetchWallet(getActivity());
            fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {

                    String[] rawdata = data.split(";");

                    if (rawdata[0].equals("Success")) {
                        if (txtMyWallet != null) {
                            txtMyWallet.setText("" + rawdata[1] + "");
                            mywalleticon.setEnabled(true);
                            walleticonstat(0);
                            progressDialog.hideDialog();
                        }
                    } else if (rawdata[0].equalsIgnoreCase("Deactivated")) {
                        db.DropAllTable(db);
                        Deactivated.showDeactivatedAccount(getActivity(), getActivity());
                    } else {
                        progressDialog.hideDialog();
                        mywalleticon.setEnabled(true);
                        walleticonstat(0);
                        Toast.makeText(getActivity(), "Failed to fetch Wallet from the server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }


            });

            fetchWallet.execute(imei, SessionID, PartnerID);

        } catch (Exception e) {
        }

    }


    //update

    /************************************
     * CHECKING Community of target mobile
     *************************************/
    private void checkTargetMobileCommunity() {
        try {
            int status = NetworkUtil.getConnectivityStatusString(getActivity());
            if (status == 0) {
                CheckInternet.showConnectionDialog(getActivity());
                progressDialog.hideDialog();
            } else {
                CreateSession newsession = new CreateSession(getActivity());
                newsession.setQueryListener(new CreateSession.QueryListener() {
                    @SuppressWarnings("unchecked")
                    public void QuerySuccessFul(String data) {
                        String[] rawdata = data.split(";");
                        if (rawdata[0].equals("Success")) {
                            SessionID = rawdata[1];
                            new processCheckingComminity().execute();
                        } else {
                            fab.setEnabled(true);
                            progressDialog.hideDialog();
                            Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }

                });

                newsession.execute(PartnerID);

            }
        } catch (Exception e) {
            progressDialog.hideDialog();
        }


    }


    class processCheckingComminity extends AsyncTask<Void, Void, String> {
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
            txttargetmobile = (EditText) rootView.findViewById(R.id.txttargetmobile);
            targetmobile = txttargetmobile.getText().toString();
            progressDialog.showDialog(getActivity(), "BudgetLoad", "Fetching Receivers Community... please wait", false);
        }

        @Override
        protected String doInBackground(Void... params) {

            String text;

            try {
                String apiURL = OTHERSURL + "&CMD=RECEIVERCOMMUNITY&IMEI=" + imei + "&TargetMobile=" + targetmobile + "&PartnerID=" + PartnerID + "&SourceMobile=" + mobile + "";

                //    Log.d("URI", apiURL);
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
                Log.d("Exception", e.toString());
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hideDialog();
            String res = "";

            if (s == null) {
                Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray json = new JSONArray(s);
                    JSONObject prefixarr = json.getJSONObject(0);
                    JSONObject articles = prefixarr.getJSONObject("Community");
                    String sessionresult = articles.getString("Result");
                    if (sessionresult.equals("OK")) {
                        res = "OK";

                        JSONObject obj = json.getJSONObject(1);
                        commlist = obj.getJSONArray("Community");

                        if (commlist.length() > 1) {
                            community.setVisibility(View.VISIBLE);
                            txtamount.setVisibility(View.GONE);
                        } else if (commlist.length() == 1) {

                            txtamount.setVisibility(View.VISIBLE);
                            JSONObject c = commlist.getJSONObject(0);
                            selectedCommID = c.getString("NetworkID");

                        }
                    } else {
                        Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    if (res.equals("OK")) {
                        Toast.makeText(getActivity(), " Retailer does not exist.", Toast.LENGTH_LONG).show();
                    }

                }
            }
        }

    }


    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************

    class processStockTransfer extends AsyncTask<Void, Void, String> {


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
                String authcode = GlobalFunctions.getSha1Hex(imei
                        .toLowerCase()
                        + mobile
                        + Constant.transfer
                        + SessionID.toLowerCase());
                String apiURL = STOCKTRANSFER + "&IMEI=" + imei + "&SourceMobTel=" + mobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode
                        + "&TargetMobTel=" + targetmobile + "&Amount=" + amount + "&PartnerID=" + PartnerID + "&TargetPartnerID=" + selectedCommID + "&Password=" + confimpass + "&PasswordStatus=" + PasswordStatus + "";

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
            super.onPostExecute(s);


            try {
                fab.setEnabled(true);

                if (s == null) {
                    Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    progressDialog.hideDialog();
                } else {

                    try {
                        JSONObject json = new JSONObject(s);
                        JSONObject articles = json.getJSONObject("StockTransfer");
                        String sessionresult = articles.getString("Result");
                        referenceno = articles.getString("ReferenceNumber");
                        String message = articles.getString("Message");
                        String resultcode = articles.getString("ResultCode");
                        if (sessionresult.equals("OK")) {

                            CreateSession newsession = new CreateSession(getActivity());
                            newsession.setQueryListener(new CreateSession.QueryListener() {
                                @SuppressWarnings("unchecked")
                                public void QuerySuccessFul(String data) {
                                    String[] rawdata = data.split(";");
                                    if (rawdata[0].equals("Success")) {
                                        SessionID = rawdata[1];
                                        new commitTransfer().execute();
                                    } else {
                                        fab.setEnabled(true);
                                        progressDialog.hideDialog();
                                        Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                    }

                                }

                            });

                            newsession.execute(PartnerID);

                        } else {

                            progressDialog.hideDialog();

                            if (resultcode.equals("1009")) {

                            } else if (resultcode.equals("1011")) {
                                DialogError.alertDialogShow(getActivity(), "BudgetLoad", message);
                            } else {
                                DialogError.alertDialogShow(getActivity(), "BudgetLoad", message);
                            }

                        }


                    } catch (JSONException e) {
                        progressDialog.hideDialog();
                        Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (Exception e) {
                progressDialog.hideDialog();
                Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            }


        }
    }

    class commitTransfer extends AsyncTask<Void, Void, String> {

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

                String authcode = GlobalFunctions.getSha1Hex(imei + mobile + Constant.commitransfer + SessionID.toLowerCase());
                String apiURL = COMMITSTOCKTRANSFER + "&IMEI=" + imei + "&SourceMobTel=" + mobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&TargetMobTel=" + targetmobile + "&ReferenceNo=" + referenceno + "&PartnerID=" + PartnerID + "&TargetPartnerID=" + selectedCommID + "";

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


            //Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

            //Log.d("Data", s);

            try {
                if (s == null) {
                    Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    progressDialog.hideDialog();
                } else {

                    try {


                        JSONObject json = new JSONObject(s);
                        JSONObject articles = json.getJSONObject("CommitStockTransfer");
                        String sessionresult = articles.getString("Result");
                        // String resultmessage = articles.getString("Message");
                        String targetimei = articles.getString("TARGETIMEI");


                        if (sessionresult.equals("OK")) {

                            Bundle b = new Bundle();
                            b.putString("Mobile", mobile);
                            b.putString("Brand", brand);
                            b.putString("Amount", amount);
                            b.putString("TargetMobile", targetmobile);

                            GlobalFunctions.fbLogger(getActivity(), "Transfer", b, 1);

                            //try {

                            DateFormat df = new SimpleDateFormat("MMMM dd, yyyy HH:mm a");
                            String newdate = df.format(Calendar.getInstance().getTime());


                            String message = "";
                            String topic = selectedCommID + "_" + targetimei;
                            //String title = "\"Transfer Credits";

                            if (sendername.isEmpty() || sendername.trim().length() <= 5) {
                                message = "You have received " + amount + " credits from " + mobile + ".";

                            } else {

                                message = "You have received " + amount + " credits from " + sendername + ".";
                            }

                            JSONObject mymsg = new JSONObject();
                            mymsg.put("message", message);
                            mymsg.put("message_title", "Transfer Credits");
                            mymsg.put("sender", "BudgetLoad");
                            mymsg.put("datesent", newdate);
                            mymsg.put("time_to_live", "200,000");

                            JSONObject myobj = new JSONObject();

                            myobj.put("to", "/topics/" + topic);
                            myobj.put("data", mymsg);

                            SendNotification sendNotification = new SendNotification(getActivity());
                            sendNotification.execute(mobile, SessionID, PartnerID, myobj.toString());
                            progressDialog.hideDialog();


                            Intent intent = new Intent(getActivity(), TransferSuccess.class);
                            intent.putExtras(b);
                            startActivityForResult(intent, 1);


                            new fetchTransfer().execute();

                        }
                    } catch (JSONException e) {
                        Log.d("Here1", "" + e.getMessage());
                        progressDialog.hideDialog();
                        Log.d("Here1", "Here");
                        Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (Exception e) {
                Log.d("Here2", "" + e.getMessage());
                Log.d("Here2", "Here");
                progressDialog.hideDialog();
                Toast.makeText(getActivity(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            }

        }
    }

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
            return text;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
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

                            //save transactions
                            db.saveTransferTxn(db, topupdata);

                            //Fetch wallet
                            FetchWallet fetchWallet = new FetchWallet(getActivity());
                            fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

                                @SuppressWarnings("unchecked")
                                public void QuerySuccessFul(String data) {
                                    String[] rawdata = data.split(";");

                                    if (rawdata[0].equals("Success")) {
                                        if (txtMyWallet != null) {
                                            txtMyWallet.setText("" + rawdata[1] + "");
                                        }
                                    } else {
                                        //  Toast.makeText(getActivity(), "Failed to fetch Wallet from the server. Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                }


                            });

                            fetchWallet.execute(imei, SessionID, PartnerID);

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
