package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.DialogError;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Verify extends AppCompatActivity implements View.OnClickListener, Constant {

    //declaration here
    DataBaseHandler db;
    EditText txtcode;
    Button btnVerify;
    Button BtnResend;
    TextView txtcounter;
    CountDownTimer remainingTimeCounter;
    private long startTime = 60000;
    private long newstartTime = 60000;
    private int counter = 1;
    String verificationCode;
    String imei;
    String regmobile = "";
    String regstatus = "";
    String SessionID = "";
    String PartnerID;
    String referrer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //remove auto fucos on the community
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //initialize database
        db = new DataBaseHandler(this);
        //get IMEI
        imei = getIMEI();
        GlobalVariables.imei = imei;
        //get Partner ID from local bd
        PartnerID = db.getPartnerID(db);

        //get other info from local db
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                regmobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                regstatus = cursor.getString(cursor.getColumnIndex("Status"));
                referrer = cursor.getString(cursor.getColumnIndex("Referrer"));
            } while (cursor.moveToNext());
        }

        //initializing objects
        btnVerify = (Button) findViewById(R.id.btnVerify);
        txtcode = (EditText) findViewById(R.id.txtcode);
        btnVerify.setOnClickListener(this);
        BtnResend = (Button) findViewById(R.id.BtnResend);
        BtnResend.setOnClickListener(this);
        txtcounter = (TextView) findViewById(R.id.textView2);

        //verification input listener
        txtcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtcode.getText().length() > 0) {
                    verificationCode = txtcode.getText().toString();
                }
            }
        });

        Intent intent = getIntent();

        if (intent != null) {

            if (intent.getStringExtra("Sender") != null) {

                if (intent.getStringExtra("Sender").equalsIgnoreCase("Budgetload")) {
                    String str = intent.getStringExtra("Message").replaceAll("\\D+", "");
                    txtcode.setText(str);
                }
            }
        }


        MyCountDownTimer();

    }

    @Override
    public void onResume() {
        super.onResume();

        //initialize database
        db = new DataBaseHandler(this);
        //get IMEI
        imei = getIMEI();
        GlobalVariables.imei = imei;
        //get Partner ID from local bd
        PartnerID = db.getPartnerID(db);

        //get other info from local db
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                regmobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                regstatus = cursor.getString(cursor.getColumnIndex("Status"));
                referrer = cursor.getString(cursor.getColumnIndex("Referrer"));
            } while (cursor.moveToNext());
        }
    }

    //region TRIGGERS
    //*****************************
    //TRIGGERS
    //*****************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //db.deletePreRegistration(db);
                //finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //Clicking the RESEND button
            case R.id.BtnResend:
                BtnResend.setEnabled(false);

                //check connection
                int netstatus = NetworkUtil.getConnectivityStatusString(this);
                if (netstatus == 0) { //meaning no connection
                    CheckInternet.showConnectionDialog(this);
                } else { //meaning has connection

                    counter++;
                    BtnResend.setClickable(false);
                    BtnResend.setVisibility(View.GONE);

                    //showing timer
                    newstartTime = startTime * counter;
                    MyCountDownTimer();

                    progressDialog.showDialog(Verify.this, "Verification", "Resending verification code. Please wait...", false);

                    //check session
                    CreateSession newsession = new CreateSession(this);
                    newsession.setQueryListener(new CreateSession.QueryListener() {
                        @SuppressWarnings("unchecked")
                        public void QuerySuccessFul(String data) {
                            String[] rawdata = data.split(";");
                            if (rawdata[0].equals("Success")) {
                                SessionID = rawdata[1];
                                new RegisterMobile().execute();
                            } else {
                                Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                BtnResend.setEnabled(true);
                                progressDialog.hideDialog();
                            }
                        }
                    });

                    newsession.execute(PartnerID);


                }

                break;

            //Click Submit button to verify
            case R.id.btnVerify:
                btnVerify.setEnabled(false);

                //hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


                //checking code
                if (txtcode.length() == 0) {
                    DialogError.alertDialogShow(this, "BudgetLoad", "Please provide a verification code.");
                    btnVerify.setEnabled(true);
                } else {

                    //check connection
                    int status = NetworkUtil.getConnectivityStatusString(this);
                    if (status == 0) {//meaning no connection
                        CheckInternet.showConnectionDialog(this);
                        btnVerify.setEnabled(true);
                    } else { //meaning has connection

                        progressDialog.showDialog(Verify.this, "Verification", "Verifying code. Please wait...", false);
                        //create session
                        CreateSession newsession = new CreateSession(this);
                        newsession.setQueryListener(new CreateSession.QueryListener() {
                            @SuppressWarnings("unchecked")
                            public void QuerySuccessFul(String data) {
                                String[] rawdata = data.split(";");
                                if (rawdata[0].equals("Success")) {
                                    SessionID = rawdata[1];
                                    new VerifyCode().execute();
                                } else {
                                    Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                    btnVerify.setEnabled(true);
                                    progressDialog.hideDialog();
                                }
                            }

                        });
                        newsession.execute(PartnerID);

                    }

                }


                break;


        }
    }

    //endregion


    //region FUNCTIONS
    //*****************************
    //FUNCTIONS
    //*****************************

    private void MyCountDownTimer() {
        remainingTimeCounter = new CountDownTimer(newstartTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                txtcounter.setText("Your verification code will arrive within "
                        + millisUntilFinished / 1000 + " seconds.");
            }

            // If you do not receive your verification code within 0 seconds,
            @Override
            public void onFinish() {
                txtcounter.setVisibility(View.VISIBLE);
                txtcounter.setText("The SMS with code didn't arrive?");
                BtnResend.setClickable(true);
                BtnResend.setVisibility(View.VISIBLE);

            }

        }.start();
    }

    public String getIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }

    //endregion

    //region THREADS
    //*****************************
    //THREADS
    //*****************************

    //for resending code
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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            String text;
            try {

                String myarray = imei + "" + regmobile + "" + Constant.Register + "" + SessionID.toLowerCase();
                String authcode = GlobalFunctions.generateSha(myarray);
                String regAPIURL = REGISTERURL + "&IMEI=" + imei + "&SourceMobTel=" + regmobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "&Referrer=" + referrer + "";
                Log.d("Uri", regAPIURL);

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
            BtnResend.setEnabled(true);

            if (s == null) {
                Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("Register");
                    String sessionresult = articles.getString("Result");
                    String resultCode = articles.getString("ResultCode");
                    if (sessionresult.equals("OK")) {
                        Toast.makeText(getBaseContext(), "Verification code already sent.", Toast.LENGTH_LONG).show();
                    } else {
                        if (resultCode.equals("1007")) {
                            Toast.makeText(getBaseContext(), "Invalid referrer mobile number.", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                }
            }
        }


    }

    //Veify Code
    class VerifyCode extends AsyncTask<Void, Void, String> {

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

            String authcode = GlobalFunctions.getSha1Hex(imei + regmobile + Constant.verify + SessionID.toLowerCase() + verificationCode);

            String apiURL = VERIFYURL + "&IMEI=" + imei + "&SourceMobTel=" + regmobile + "&SessionNo=" + SessionID + "&VCode=" + verificationCode + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(apiURL);

            String text = null;
            Log.d("Uri", apiURL);
            try {
                HttpResponse response = httpClient.execute(httpGet,
                        localContext);
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

            progressDialog.hideDialog();
            btnVerify.setEnabled(true);

            if (s == null) {
                Toast.makeText(getBaseContext(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("Verify");
                    String sessionresult = articles.getString("Result");

                    if (sessionresult.equals("OK")) {
                        Bundle b = new Bundle();
                        b.putString("Mobile", regmobile);
                        GlobalFunctions.fbLogger(Verify.this, "Verification", b, 1);
                        db.updatePreRegistration(db, regmobile, "Profiling");
                        Intent iinent = new Intent(Verify.this, Profile.class);
                        startActivity(iinent);
                        iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ActivityCompat.finishAffinity(Verify.this);

                    } else {
                        Toast.makeText(getBaseContext(), " Invalid verification code.", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {

                }
            }
        }

    }

    //endregion


}


