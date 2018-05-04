package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.DialogError;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.GlobalVariables;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.commonFunctions;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.Constant.Constant;
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


public class Register extends AppCompatActivity implements View.OnClickListener, Constant {

    EditText txtMobile, txtprefix, txtreferrer;
    Button btnRegister;

    String SessionID;
    commonFunctions commFunc;
    Boolean myresult;

    DataBaseHandler db;

    String imei;
    String mobile;
    String referrer;
    Context mcontext;

    TextView popSubmit;
    TextView popCancel;
    EditText popRefcode;
    EditText txtDefault;
    String defaultreferrer;

    String PartnerID;
    Dialog dialog;

    AlertDialog confimation;
    AlertDialog.Builder builder;
    TextView txtmessage;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //reg ioninitializing database
        db = new DataBaseHandler(this);
        //endregion

        //region initializing common functions
        commFunc = new commonFunctions(getBaseContext());
        mcontext = this;

        //getting IMEI
        imei = getIMEI();
        GlobalVariables.imei = imei;

        //Toast.makeText(getBaseContext(), imei, Toast.LENGTH_SHORT).show();

        //getting partner from localdb
        PartnerID = db.getPartnerID(db);

        //endregion

        //region declaring Objects
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        txtMobile = (EditText) findViewById(R.id.txtmobile);
        txtprefix = (EditText) findViewById(R.id.txtprefix);
        txtreferrer = (EditText) findViewById(R.id.txtreferrer);
        txtreferrer.setInputType(InputType.TYPE_NULL);
        txtreferrer.setOnClickListener(this);

        txtMobile.setHint(Html.fromHtml(
                "<font size=\"5\">" + "Mobile" + "</font>" +
                        "<italic><small>" + " (Own number to register.)" + "</small></italic>"));

        dialog = new Dialog(mcontext);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_referrer);

        popSubmit = (TextView) dialog.findViewById(R.id.popSubmit);
        popCancel = (TextView) dialog.findViewById(R.id.popCancel);
        popRefcode = (EditText) dialog.findViewById(R.id.poprefmobileno);


        //endregion


        TelephonyManager tMgr = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        if (mPhoneNumber != null) {

            if (mPhoneNumber.length() > 11) {
                txtMobile.setText(mPhoneNumber.substring(mPhoneNumber.length() - 10));
                mobile = txtMobile.getText().toString();
            } else {
                txtMobile.setText(mPhoneNumber);
                mobile = mPhoneNumber;
            }


        }


        //region INLINE TRIGGERS

        //mobile input listener
        txtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtMobile.getText().length() > 0) {
                    mobile = txtMobile.getText().toString();
                }
            }
        });

        //Click listening for cancel in dialog
        popCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        //Click Submit button from dialog to get the referrer
        popSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valrefcode = popRefcode.getText().toString();
                if (valrefcode.length() < 11) {
                    DialogError.alertDialogShow(mcontext, "BudgetLoad",
                            "Invalid Mobile Number.");
                } else {
                    txtreferrer.setText(valrefcode);
                    dialog.cancel();
                }
            }
        });


        //COnfirmation for topUp
        builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Confirmation");
        txtmessage = new TextView(mcontext);
        txtmessage.setPadding(60, 10, 60, 10);
        txtmessage.setTextSize(22f);
        builder.setView(txtmessage, 10, 100, 10, 20);
        builder.setCancelable(false);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                if (txtreferrer.getText().length() > 0) { //refferer is not empty
                    if (txtreferrer.getText().length() == 11) { //check length of referrer

                        progressDialog.showDialog(mcontext, "Registration", "Verifying mobile number. Please wait...", false);

                        CreateSession newsession = new CreateSession(Register.this);
                        newsession.setQueryListener(new CreateSession.QueryListener() {
                            @SuppressWarnings("unchecked")
                            public void QuerySuccessFul(String data) {
                                String[] rawdata = data.split(";");
                                if (rawdata[0].equalsIgnoreCase("Success")) {
                                    SessionID = rawdata[1];
                                    new RegisterMobile().execute();
                                } else {
                                    Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                    btnRegister.setEnabled(true);
                                    progressDialog.hideDialog();
                                }
                            }

                        });
                        newsession.execute(PartnerID);

                    } else {
                        Toast.makeText(getBaseContext(), "Invalid referral mobile number.", Toast.LENGTH_LONG).show();
                        btnRegister.setEnabled(true);
                        progressDialog.hideDialog();
                    }
                } else { //meaning referrer is empty ( use default referrer)

                    progressDialog.showDialog(mcontext, "Registration", "Verifying mobile number. Please wait...", false);

                    CreateSession newsession = new CreateSession(Register.this);
                    newsession.setQueryListener(new CreateSession.QueryListener() {
                        @SuppressWarnings("unchecked")
                        public void QuerySuccessFul(String data) {
                            String[] rawdata = data.split(";");
                            if (rawdata[0].equals("Success")) {
                                SessionID = rawdata[1];
                                new RegisterMobile().execute();
                            } else {
                                Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                btnRegister.setEnabled(true);
                                progressDialog.hideDialog();
                            }

                        }

                    });

                    newsession.execute(PartnerID);


                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confimation.dismiss();
                txtmessage.setText("");
                btnRegister.setEnabled(true);
            }
        });
        confimation = builder.create();

        //endregion

        //region Fetch Referrer

        if (PartnerID.equalsIgnoreCase("remitbox")) {

            CreateSession newsession = new CreateSession(Register.this);
            newsession.setQueryListener(new CreateSession.QueryListener() {
                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {
                    String[] rawdata = data.split(";");
                    if (rawdata[0].equals("Success")) {
                        SessionID = rawdata[1];
                        new fetchMainReferrer().execute();
                    }
                }

            });
            newsession.execute(PartnerID);
        }

        //endregion


    }

    @Override
    public void onResume() {
        super.onResume();

        // imei = getIMEI();
        // GlobalVariables.imei = imei;

        //getting partner from localdb
        //  PartnerID = db.getPartnerID(db);

    }

    //region TRIGGERS
    //******************
    //TRIGGERS
    //******************


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //SUBMIT button triggers
            case R.id.btnRegister:
                //hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                btnRegister.setEnabled(false);
                boolean mystatus = true;
                myresult = commFunc.validate(new EditText[]{txtMobile}); //validate mobile
                referrer = txtreferrer.getText().toString(); //getting the refferer

                if (!myresult) { //meaning result from checking is incorrect
                    btnRegister.setEnabled(true);
                    mystatus = false;
                    DialogError.alertDialogShow(this, "BudgetLoad",
                            "Please provide a correct mobile number.");
                }
                //check referrer
                if (PartnerID.equalsIgnoreCase("gcsync")) {
                    if (referrer == null) {
                        referrer = defaultreferrer;
                    }
                }

                if (!PartnerID.equalsIgnoreCase("gcsync")) {
                    if (referrer == null || referrer.equalsIgnoreCase("")) {
                        btnRegister.setEnabled(true);
                        mystatus = false;
                        DialogError.alertDialogShow(this, "BudgetLoad",
                                "Please provide a correct referrer number.");
                    }
                }

                if (mystatus) {
                    int status = NetworkUtil.getConnectivityStatusString(this); //check internet connection
                    if (status == 0) { //no connection
                        btnRegister.setEnabled(true);
                        CheckInternet.showConnectionDialog(this);
                    } else { //meaning has connection

                        if (txtMobile.getText().length() < 10) {
                            btnRegister.setEnabled(true);
                            DialogError.alertDialogShow(this, "BudgetLoad",
                                    "Invalid Mobile Number.");
                        } else {


                            if (PartnerID.equalsIgnoreCase("gcsync")) {
                                txtmessage.setText("Are you sure you want to register your mobile number " + Html.fromHtml("0" + mobile) + "  to BudgetLoad?");
                            } else {
                                txtmessage.setText("Are you sure you want to register your mobile number " + Html.fromHtml("0" + mobile) + " and referrer as " + Html.fromHtml(referrer) + " to BudgetLoad?");
                            }

                            confimation.show();
                            confimation.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#646464"));
                            confimation.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#19BD4E"));


                        }

                    }
                }


                break;

            case R.id.txtreferrer:
                dialog.show();
                break;


        }

    }
    //endregion

    //region FUNCTIONS
    //***********************
    //Functions
    //***********************


    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


    //endregion

    //region THREADS
    //***********************
    //THREADS
    //***********************

    class RegisterMobile extends AsyncTask<Void, Void, String> {

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
            try {

                String myarray = imei + "" + mobile + "" + Constant.Register + "" + SessionID.toLowerCase();


                String authcode = GlobalFunctions.generateSha(myarray);
                //String authcode = GlobalFunctions.getSha1Hex(imei + mobile + "R E G I S T E R" + SessionID.toLowerCase());
                String regAPIURL = REGISTERURL + "&IMEI=" + imei + "&SourceMobTel=" + mobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "&Referrer=" + referrer + "";
                Log.d("URI", regAPIURL);
                HttpGet httpGet = new HttpGet(regAPIURL);
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
            progressDialog.hideDialog();

            btnRegister.setEnabled(true);
            if (s == null) {
                Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("Register");
                    String sessionresult = articles.getString("Result");
                    String resultCode = articles.getString("ResultCode");
                    if (sessionresult.equals("OK")) {

                        db.savePreRegistration(db, mobile, referrer);

                        Bundle myb = new Bundle();

                        //meaning first time registering the app or has different mobile and IMEI- procceed to verification
                        if (resultCode.equals("0000")) {
                            myb.putString("Mobile", mobile);
                            myb.putString("isFirstTime", "Yes");
                            GlobalFunctions.fbLogger(Register.this, "Registration", myb, 1);
                            Intent iinent = new Intent(Register.this, Verify.class);
                            iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(iinent);
                            finish();
                        }
                        //meaning already has account in the app -  procceed to main page
                        if (resultCode.equals("0001")) {
                            db.updatePreRegistration(db, mobile, "Verified");
                            Bundle b = new Bundle();
                            b.putString("ReturnHero", "Yes");
                            myb.putString("isFirstTime", "No");
                            myb.putString("Mobile", mobile);
                            GlobalFunctions.fbLogger(Register.this, "Registration", myb, 1);
                            //open main activity
                            Intent iinent = new Intent(Register.this, MainActivity.class);
                            iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            iinent.putExtras(b);
                            startActivity(iinent);
                            finish();
                        }

                    } else {



                        if (resultCode.equals("1008")) {
                            Toast.makeText(getBaseContext(), "You are already registered to ActiveLoad Community.", Toast.LENGTH_LONG).show();
                        }

                        if (resultCode.equals("1006")) {
                            Toast.makeText(getBaseContext(), "Invalid mobile number.", Toast.LENGTH_LONG).show();
                        }
                        //if (resultCode.equals("1007")) {
                        txtreferrer.setText("");
                        //Toast.makeText(getBaseContext(), articles.getString("Message"), Toast.LENGTH_LONG).show();
                        //} else
                        //    Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                }
            }
        }


    }

    class fetchMainReferrer extends AsyncTask<Void, Void, String> {


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
            try {
                String myarray = imei + "" + mobile + "" + Constant.miscellaneous + "" + SessionID.toLowerCase();
                String authcode = GlobalFunctions.generateSha(myarray);
                String regAPIURL = OTHERSURL + "&IMEI=" + imei + "&SourceMobTel=" + mobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "&CMD=MAINREFERRER";
                Log.d("URI", regAPIURL);
                HttpGet httpGet = new HttpGet(regAPIURL);
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
            if (s == null) {
                Toast.makeText(getBaseContext(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("MainReferrer");
                    String sessionresult = articles.getString("Result");
                    if (sessionresult.equals("OK")) {
                        String subdmain = articles.getString("Referrer");
                        if (subdmain.length() == 11) {
                            txtreferrer.setText(subdmain);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    //endregion


}
