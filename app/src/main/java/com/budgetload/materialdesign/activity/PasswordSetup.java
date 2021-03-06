package com.budgetload.materialdesign.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.GlobalVariables;
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

public class PasswordSetup extends AppCompatActivity {
    String menuTitle = "SUBMIT";
    public MenuItem mnuPasswordSetup;
    DataBaseHandler db;
    String SessionID;
    String PartnerID;
    String imei;
    EditText pass;
    EditText confirmpass;
    String passval ;
    String confirmpassval ;
    String mobile;
    String shaPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //initialize database
        db = new DataBaseHandler(this);


        //get IMEI
        imei = getIMEI();
        GlobalVariables.imei = imei;
        //get partner
        PartnerID = db.getPartnerID(db);
        //get mobile
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }

    }


    //region TRIGGERS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_passwordsetup, menu);
        mnuPasswordSetup = menu.findItem(R.id.submit);
        mnuPasswordSetup.setTitle(menuTitle);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Submit Button
            case R.id.submit:
                processSettingUpPassword();
                return true;

            case android.R.id.home:
                    //db.deletePreRegistration(db);
                    finish();
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //endregion

    //region FUNCTIONS

    public void processSettingUpPassword(){
         pass = (EditText) findViewById(R.id.password);
         confirmpass = (EditText) findViewById(R.id.confirmpass);
         passval = pass.getText().toString().trim();
         confirmpassval = confirmpass.getText().toString().trim();

        //check if edit text are not empty
        if(!passval.equals("") && !confirmpassval.equals("")){
            //check if password and confirm password is the same
            if(passval.equals(confirmpassval)) {
                //check if has email address
                String emailadd="";
                Cursor cursor = db.getProfile(db);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                         emailadd = cursor.getString(cursor.getColumnIndex("EmailAddress"));
                    }
                    if(!emailadd.trim().equals("") && !emailadd.trim().equals(".") &&!emailadd.trim().equals("NONE")  ){
                        VerifySession();
                    }else{
                        Toast.makeText(getBaseContext(),"Email was not set, please set your email.",500).show();
                    }
                }


            }else{
                Toast.makeText(getBaseContext(),"Password did not match. ",500).show();
            }

        }else{
            Toast.makeText(getBaseContext(),"All fields are mandatory",500).show();
        }


    }

    public void VerifySession(){
        progressDialog.showDialog(PasswordSetup.this, "Profile", "Saving password. Please wait...", false);

        //create session
        CreateSession newsession = new CreateSession(this);
        newsession.setQueryListener(new CreateSession.QueryListener() {
            @SuppressWarnings("unchecked")
            public void QuerySuccessFul(String data) {
                String[] rawdata = data.split(";");
                if (rawdata[0].equals("Success")) {
                    SessionID = rawdata[1];
                    new savePassword().execute();
                } else {
                    Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    progressDialog.hideDialog();
                }
            }

        });
        newsession.execute(PartnerID);
    }

    public String getIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getDeviceId();

    }

    //endregion

    //region THREAD

    class savePassword extends AsyncTask<Void, Void, String> {

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
             shaPass = GlobalFunctions.getSha1Hex(passval);
            try{
                String authcode = GlobalFunctions.getSha1Hex(imei.toLowerCase()+ mobile+ "M I S C E L L A N E O U S"+ SessionID.toLowerCase());
                String apiURL = Constant.OTHERSURL+ "&IMEI="+ imei + "&SourceMobTel="+ mobile + "&SessionNo="+ SessionID.toLowerCase() + "&AuthCode=" + authcode
                        + "&PartnerID=" + PartnerID + "&CMD=SETUPPASSWORD&Password="+ shaPass;


            //    Log.d("URI", apiURL);

                HttpGet httpGet = new HttpGet(apiURL);
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 3000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 5000;
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
            progressDialog.hideDialog();

            if (s == null) {
                Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("PasswordSetup");
                    String result = articles.getString("Result");


                    if (result.equals("OK")) {
                        Toast.makeText(getBaseContext(), "Password was successfully saved.", Toast.LENGTH_LONG).show();
                        //save local
                        db.SavePassword(db, shaPass);
                        //clear
                        pass.setText("");
                        confirmpass.setText("");
                        shaPass="";
                        //close activity
                        finish();

                    } else {
                        Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {

                }

            }

        }



    }

    //endregion





}
