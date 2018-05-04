package com.budgetload.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.Deactivated;
import com.budgetload.materialdesign.Common.FetchWallet;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.RequestCredits;
import com.budgetload.materialdesign.Common.commonFunctions;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.Constant.Indicators;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import static com.budgetload.materialdesign.R.id.txtpuramount;
import static com.budgetload.materialdesign.Common.GlobalVariables.PartnerID;
import static com.budgetload.materialdesign.Common.GlobalVariables.SessionID;
import static com.budgetload.materialdesign.Common.GlobalVariables.imei;

public class creditsConfirmation extends AppCompatActivity implements Constant {

    //region DECLARATION

    Context mcontext;
    TextView txtamount, txtfee, txttotalamount, mywallet;
    String gateway, BudgetLoadRef;
    Double totalamount, amount, fee;
    String mobile;
    DataBaseHandler db;
    commonFunctions commonFunctions;
    ImageView imgrequestcredits;
    ImageView imgWallet;
    DecimalFormat formatter = new DecimalFormat("#,###,###.00");
    Button btnPurchase;
    Button btncheck;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_confirmation);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //region INITIALIZATION
        mcontext = this;
        db = new DataBaseHandler(mcontext);
        commonFunctions = new commonFunctions(mcontext);

        mywallet = (TextView) findViewById(R.id.txtMyWallet);
        mywallet.setText(MainActivity.walletvalue);
        imgrequestcredits = (ImageView) findViewById(R.id.requestcredits);
        imgWallet = (ImageView) findViewById(R.id.walleticon);
        //get other info
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }

        //get partnerID
        PartnerID = db.getPartnerID(db);

        txtamount = (TextView) findViewById(txtpuramount);
        txtfee = (TextView) findViewById(R.id.txtpaymentfee);
        txttotalamount = (TextView) findViewById(R.id.txtTotalAmount);

        //endregion

        Bundle bundle = getIntent().getExtras();
        gateway = bundle.getString("Gateway");
        amount = bundle.getDouble("Amount");
        fee = bundle.getDouble("Fee");
        //Outlet = bundle.getString("Outlet");

        txtamount.setText("" + formatter.format(amount));
        txtfee.setText("" + formatter.format(fee));

        totalamount = amount + fee;

        txttotalamount.setText("" + formatter.format(totalamount));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                int status = NetworkUtil.getConnectivityStatusString(mcontext);
//                if (status == 0) {
//                    CheckInternet.showConnectionDialog(mcontext);
//                    progressDialog.hideDialog();
//                } else {
//                    if (MainActivity.user != null) {
//                        if (commonFunctions.isEmailValid(MainActivity.user.email)) {
//                            progressDialog.showDialog(mcontext, "Purchase Credits", "Processing request.Please wait...", false);
//                            CreateSession newsession = new CreateSession(creditsConfirmation.this);
//                            newsession.setQueryListener(new CreateSession.QueryListener() {
//                                @SuppressWarnings("unchecked")
//                                public void QuerySuccessFul(String data) {
//                                    String[] rawdata = data.split(";");
//                                    if (rawdata[0].equals("Success")) {
//                                        SessionID = rawdata[1];
//                                        new purchaseRequest().execute();
//                                        //new fetchAPIDetails().execute();
//                                    } else {
//                                        Toast.makeText(mcontext, "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
//                                        progressDialog.hideDialog();
//                                    }
//                                }
//                            });
//                            newsession.execute(PartnerID);
//                        } else {
//                            Toast.makeText(mcontext, "Please update your email to continue.", Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(mcontext, "Fetching profile info please wait.", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
        btnPurchase = (Button) findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int status = NetworkUtil.getConnectivityStatusString(mcontext);
                if (status == 0) {
                    CheckInternet.showConnectionDialog(mcontext);
                    progressDialog.hideDialog();
                } else {
                    if (MainActivity.user != null) {
                        if (commonFunctions.isEmailValid(MainActivity.user.email)) {
                            progressDialog.showDialog(mcontext, "Purchase Credits", "Processing request.Please wait...", false);
                            CreateSession newsession = new CreateSession(creditsConfirmation.this);
                            newsession.setQueryListener(new CreateSession.QueryListener() {
                                @SuppressWarnings("unchecked")
                                public void QuerySuccessFul(String data) {
                                    String[] rawdata = data.split(";");
                                    if (rawdata[0].equals("Success")) {
                                        SessionID = rawdata[1];
                                        new purchaseRequest().execute();
                                        //new fetchAPIDetails().execute();
                                    } else {
                                        Toast.makeText(mcontext, "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                        progressDialog.hideDialog();
                                    }
                                }
                            });
                            newsession.execute(PartnerID);
                        } else {
                            Toast.makeText(mcontext, "Please update your email to continue.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(mcontext, "Fetching profile info please wait.", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        imgrequestcredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                RequestCredits.showRequestDialog(mcontext, creditsConfirmation.this, PartnerID, MainActivity.user.mobile);
                v.setEnabled(true);
            }
        });

        imgWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallet();
            }
        });
    }

    //region Functions

    public void wallet() {

        progressDialog.showDialog(mcontext, "BudgetLoad", "Fetching Wallet... please wait", false);

        try {
            FetchWallet fetchWallet = new FetchWallet(creditsConfirmation.this);
            fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {

                    String[] rawdata = data.split(";");

                    if (rawdata[0].equals("Success")) {
                        if (mywallet != null) {
                            try {
                                mywallet.setText("" + rawdata[1] + "");
                                imgWallet.setEnabled(true);
                                walleticonstat(0);
                                progressDialog.hideDialog();
                                Indicators.WalletFetchIndicator = "false";
                            } catch (Exception e) {
                                progressDialog.hideDialog();
                            }

                        }
                    } else if (rawdata[0].equalsIgnoreCase("Deactivated")) {
                        db.DropAllTable(db);
                        Deactivated.showDeactivatedAccount(mcontext, creditsConfirmation.this);
                    } else {
                        imgWallet.setEnabled(true);
                        walleticonstat(0);
                        progressDialog.hideDialog();
                        Toast.makeText(mcontext, "Failed to fetch Wallet from the server. Please try again.", Toast.LENGTH_LONG).show();
                    }

                }


            });


            fetchWallet.execute(imei, SessionID, PartnerID);
        } catch (Exception e) {
            progressDialog.hideDialog();
        }


    }

    public void walleticonstat(Integer val) {
        if (val.equals(1)) {
            imgWallet.setBackgroundResource(MainActivity.walletoff);
        } else {
            imgWallet.setBackgroundResource(MainActivity.walleton);
        }
    }

    //endregion

    //region TRIGGERS
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //endregion

    //region THREADS
    public class purchaseRequest extends AsyncTask<Void, Void, String> {

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
        protected String doInBackground(Void... params) {
            String text;

            String email, paymentmode, paymentmethod;

            email = MainActivity.user.email;
            paymentmode = MainActivity.paymentmode;
            paymentmethod = MainActivity.paymentmethod;

            try {
                String authcode = GlobalFunctions.getSha1Hex(imei
                        .toLowerCase()
                        + mobile
                        + Constant.purrequest
                        + SessionID.toLowerCase());
                String apiURL = PURCHASEREQUEST
                        + "&IMEI=" + imei + "&SourceMobTel="
                        + mobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID="
                        + PartnerID + "&Amount=" + amount
                        + "&GateWAY=" + Constant.defaultgateway
                        + "&PaymentFEE=" + fee + "&email=" + URLEncoder.encode(email) + "&mode=" + paymentmode + "&paymentgateway=" + URLEncoder.encode(paymentmethod);
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
                e.printStackTrace();
                text = null;
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                if (s == null) {
                    progressDialog.hideDialog();
                    Toast.makeText(mcontext, " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                } else {

                    try {

                        JSONObject json = new JSONObject(s);
                        JSONObject articles = json.getJSONObject("PURCHASE");
                        String sessionresult = articles.getString("Result");
                        String resultcode = articles.getString("ResultCode");

                        BudgetLoadRef = articles.getString("ReferenceNumber");
                        String Url = articles.getString("PARAMSTRING");

                        //String paraurl[] = articles.getString("PARAMSTRING").split("?");
                        //Log.d("Data", paraurl[0]);

                        if (sessionresult.equals("OK")) {
                            progressDialog.hideDialog();


//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DRAGONPAY + Url));
//                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(browserIntent);
//                            finish();

                            //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                            //Intent browserIntent = new Intent(creditsConfirmation.this, MyWebView.class);
                            // browserIntent.putExtra("MyURl", Url);
                            // browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            //startActivity(browserIntent);
                            //finish();

                            // new fetchAPIDetails().execute();

                            Intent browserIntent = new Intent(creditsConfirmation.this, MyWebView.class);
                            browserIntent.putExtra("MyURl", Url);
                            startActivity(browserIntent);
                            finish();

//                            Intent intent = new Intent();
//                            intent.setComponent(new ComponentName("com.google.android.browser","com.google.android.browser.BrowserActivity"));
//                            intent.setAction("android.intent.action.VIEW");
//                            intent.addCategory("android.intent.category.BROWSABLE");
//                            Uri uri = Uri.parse(Url);
//                            intent.setData(uri);

                        } else {
                            progressDialog.hideDialog();
                            if (resultcode.equals("1008")) {
                                db.DropAllTable(db);
                                Deactivated.showDeactivatedAccount(mcontext, creditsConfirmation.this);
                            }
                        }
                    } catch (JSONException e) {
                        progressDialog.hideDialog();
                        Toast.makeText(mcontext, " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
            }


        }
    }

//    public class fetchAPIDetails extends AsyncTask<Void, Void, String> {
//
//        protected String getASCIIContentFromEntity(HttpEntity entity)
//                throws IllegalStateException, IOException {
//            InputStream in = entity.getContent();
//            StringBuffer out = new StringBuffer();
//            int n = 1;
//            while (n > 0) {
//                byte[] b = new byte[4096];
//                n = in.read(b);
//                if (n > 0)
//                    out.append(new String(b, 0, n));
//            }
//            return out.toString();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String text;
//            try {
//                String authcode = GlobalFunctions.getSha1Hex(imei
//                        .toLowerCase()
//                        + mobile
//                        + Constant.miscellaneous
//                        + SessionID.toLowerCase());
//                String apiURL = OTHERSURL
//                        + "&IMEI="
//                        + imei + "&SourceMobTel="
//                        + mobile + "&SessionNo="
//                        + SessionID + "&AuthCode=" + authcode
//                        + "&PartnerID=" + PartnerID + "&cmd=" + Constant.cmdfetch;
//                Log.d("apiURL", apiURL);
//                HttpGet httpGet = new HttpGet(apiURL);
//                HttpParams httpParameters = new BasicHttpParams();
//                int timeoutConnection = 60000;
//                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//                int timeoutSocket = 60000;
//                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
//                HttpResponse response = httpClient.execute(httpGet);
//                HttpEntity entity = response.getEntity();
//                text = getASCIIContentFromEntity(entity);
//            } catch (Exception e) {
//                e.printStackTrace();
//                text = null;
//            }
//            return text;
//
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.d("Data", "" + s);
//            try {
//                if (s != null) {
//                    try {
//                        JSONArray json = new JSONArray(s);
//                        JSONObject prefixarr = json.getJSONObject(0);
//                        JSONObject articles = prefixarr.getJSONObject("DETAILS");
//                        String result = articles.getString("Result");
//                        if (result.equalsIgnoreCase("ok")) {
//                            JSONObject mydetails = json.getJSONObject(1);
//                            JSONObject details = mydetails.getJSONObject("API");
//                            String name = details.getString("Name");
//                            String id = details.getString("ID");
//                            String mydescription = "Credits for BudgetLoad";
//
//                            String mydigest = GlobalFunctions.getSha1Hex(name + ":" + BudgetLoadRef + ":" + txttotalamount.getText().toString() + ":" + "PHP" + ":" + mydescription + ":" + MainActivity.user.email + ":" + id);
//
//                            String params = "";
//                            if (MainActivity.paymentgateway.equalsIgnoreCase("remittance")) {
//                                params = "&merchantid=" + name + "&txnid=" + BudgetLoadRef + "&amount=" + txttotalamount.getText().toString() + "&ccy=PHP&description=" + mydescription + "&email=" + MainActivity.user.email + "&procid=" + MainActivity.paymentmode + "&digest=" + mydigest;
//                            } else {
//                                params = "&merchantid=" + name + "&txnid=" + BudgetLoadRef + "&amount=" + txttotalamount.getText().toString() + "&ccy=PHP&description=" + mydescription + "&email=" + MainActivity.user.email + "&mode=" + MainActivity.paymentmode + "&digest=" + mydigest;
//
//                            }
//                            String Url = DRAGONPAY + params;
//                            Log.d("Data", Url);
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
//                            //Intent browserIntent = new Intent(creditsConfirmation.this, MyWebView.class);
//                            browserIntent.putExtra("MyURl", Url);
//                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(browserIntent);
//                            finish();
//                        }
//                        progressDialog.hideDialog();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    //endregion

}

