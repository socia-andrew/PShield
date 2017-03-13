package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.NetworkUtil;
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
import java.net.URLEncoder;

public class SettingsFileTicket extends AppCompatActivity implements OnItemSelectedListener, Constant {

    Context mcontext;
    String imei;
    DataBaseHandler db;
    String mobile;
    String SessionID;
    String[] title;
    EditText message;
    TextView txtTicket;
    FloatingActionButton fab;
    String txtmessage;
    String txttitle;
    String selectedTitle;
    Spinner spinTicket;
    String PartnerID;
    String firstname;
    String lastname;

    ArrayAdapter<String> spinTicketAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_file_ticket);

        mcontext = this;
        imei = getIMEI();
        db = new DataBaseHandler(this);
        PartnerID = db.getPartnerID(db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        title = getResources().getStringArray(R.array.titleticket);
        txtTicket = (TextView) findViewById(R.id.txtspinner);
        //spinTicket = (Spinner) findViewById(R.id.spinner);
        message = (EditText) findViewById(R.id.editText4);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }


        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                txtmessage = message.getText().toString();
                txttitle = txtTicket.getText().toString();
                if (txttitle.equals("")) {
                    Toast.makeText(getBaseContext(), "Please select topic", Toast.LENGTH_LONG).show();
                } else if (txtmessage.equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill up input message.", Toast.LENGTH_LONG).show();
                } else {
                    VerifySession();
                }
            }
        });


        txtTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Ticket.class);
                startActivityForResult(intent, 1);
            }
        });


        Cursor dr = db.getProfile(db);

        if (dr.getCount() > 0) {
            dr.moveToFirst();
            do {

                firstname = dr.getString(2);
                lastname = dr.getString(4);

            } while (dr.moveToNext());
        }

    }

    // region FUNCTIONS
//********************
//FUNCTIONS
//**********
    public void VerifySession() {
        //Thread

        progressDialog.showDialog(mcontext, "File a Ticket", "Processing ticket. Please wait...", false);

        int constatus = NetworkUtil.getConnectivityStatusString(this);

        if (constatus == 0) {
            CheckInternet.showConnectionDialog(this);
        } else {
            CreateSession newsession = new CreateSession(this);
            newsession.setQueryListener(new CreateSession.QueryListener() {
                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {
                    String[] rawdata = data.split(";");
                    if (rawdata[0].equals("Success")) {
                        SessionID = rawdata[1];
                        new processFileTicket().execute();
                    } else {
                        progressDialog.hideDialog();
                        Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            newsession.execute(PartnerID);
        }

    }

    public String getIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getDeviceId();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                txtTicket.setText(data.getStringExtra("TicketTitle"));

            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
//END REGION FUNCTIONS

    //region TRIGGERS
//************************
//TRIGGERS
//************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //endregion

    //region THREADS
//************************
//THREADS
//************************
    class processFileTicket extends AsyncTask<Void, Void, String> {

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
                        + Constant.miscellaneous
                        + SessionID.toLowerCase());
                String apiURL = OTHERSURL
                        + "&IMEI="
                        + imei + "&SourceMobTel="
                        + mobile + "&SessionNo="
                        + SessionID.toLowerCase() + "&AuthCode=" + authcode
                        + "&PartnerID=" + PartnerID + "&CMD=FILETICKET&Message="
                        + URLEncoder.encode(txtmessage) + "&FirstName="
                        + URLEncoder.encode(firstname) + "&LastName="
                        + URLEncoder.encode(lastname) + "&Title="
                        + URLEncoder.encode(txttitle) + "";


                //   Log.d("URI",apiURL);

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
            progressDialog.hideDialog();
            String message;

            try {

                if (s == null) {
                    Toast.makeText(getBaseContext(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("Ticketing");
                    String sessionresult = articles.getString("Result");

                    message = articles.getString("Message");


                    if (sessionresult.equals("OK")) {
                        SettingsFileTicket.this.finish();
                        Toast.makeText(getBaseContext(), "Ticket successfully sent", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

                    }


                }
            } catch (JSONException e) {

                e.printStackTrace();

            }
        }
    }
}