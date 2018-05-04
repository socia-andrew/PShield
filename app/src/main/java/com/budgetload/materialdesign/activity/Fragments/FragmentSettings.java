package com.budgetload.materialdesign.activity.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.FetchWallet;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.RequestCredits;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.Community;
import com.budgetload.materialdesign.Common.GlobalVariables;
import com.budgetload.materialdesign.activity.SettingPassword;
import com.budgetload.materialdesign.activity.SettingsAbout;
import com.budgetload.materialdesign.activity.SettingsFileTicket;
import com.budgetload.materialdesign.activity.SettingsProfile;
import com.budgetload.materialdesign.activity.Settings_FAQ;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class FragmentSettings extends Fragment implements View.OnClickListener, Constant {

    //region INITIALIZATION
    View rootView;
    Context mcontext;
    Button profileBtn;
    Button faqBtn;
    Button aboutBtn;
    Button deactivateBtn;
    Button fileticketBtn;
    Button passwordbtn;
    Intent intent;
    AlertDialog alert;
    DataBaseHandler db;
    String regmobile;
    TextView txtwallet;
    ImageView mywalleticon;
    ImageView imgrequestcredits;
    String SessionID;
    String imei;
    String PartnerID;
    int walletoff;
    int walleton;
    //endregion


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        mcontext = getActivity();

        //region INITIALIZATION
        txtwallet = (TextView) rootView.findViewById(R.id.textView);
        mywalleticon = (ImageView) rootView.findViewById(R.id.walleticon);
        mywalleticon.setOnClickListener(this);

        profileBtn = (Button) rootView.findViewById(R.id.profile);
        profileBtn.setOnClickListener(this);

        faqBtn = (Button) rootView.findViewById(R.id.FAQ);
        faqBtn.setOnClickListener(this);

        aboutBtn = (Button) rootView.findViewById(R.id.about);
        aboutBtn.setOnClickListener(this);

        deactivateBtn = (Button) rootView.findViewById(R.id.deactivate);
        deactivateBtn.setOnClickListener(this);

        passwordbtn = (Button) rootView.findViewById(R.id.password);
        passwordbtn.setOnClickListener(this);

        imgrequestcredits = (ImageView) rootView.findViewById(R.id.requestcredits);

        walletoff = R.drawable.walletsmalloff;
        walleton = R.drawable.wallet;


        fileticketBtn = (Button) rootView.findViewById(R.id.fileticket);
        fileticketBtn.setOnClickListener(this);
        imei = getIMEI();
        GlobalVariables.imei = imei;

        //endregion

        //region DATABASE EVENTS

        db = new DataBaseHandler(getActivity());
        PartnerID = db.getPartnerID(db);
        Cursor cursor = db.getIsRegistered(db);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                regmobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }
        Cursor crWallet = db.getWallet(db);

        if (crWallet.getCount() > 0) {
            crWallet.moveToFirst();
            do {
                txtwallet.setText(crWallet.getString(crWallet.getColumnIndex("Balance")));
            } while (crWallet.moveToNext());
        } else {
            txtwallet.setText("0.00");
        }

        //endregion

        imgrequestcredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                RequestCredits.showRequestDialog(mcontext, getActivity(), PartnerID, regmobile);
                v.setEnabled(true);
            }
        });

        return rootView;

    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.profile:

                profileBtn.setEnabled(false);
                //profileBtn.setEnabled(false);
                intent = new Intent(getActivity(), SettingsProfile.class);
                startActivityForResult(intent, 1);
                profileBtn.setEnabled(true);
                break;
            case R.id.password:
                faqBtn.setEnabled(false);
                //profileBtn.setEnabled(false);
                intent = new Intent(getActivity(), SettingPassword.class);
                startActivityForResult(intent, 1);
                faqBtn.setEnabled(true);
                break;

            case R.id.FAQ:

                faqBtn.setEnabled(false);
                //profileBtn.setEnabled(false);
                intent = new Intent(getActivity(), Settings_FAQ.class);
                startActivityForResult(intent, 1);
                faqBtn.setEnabled(true);
                break;

            case R.id.about:

                aboutBtn.setEnabled(false);
                intent = new Intent(getActivity(), SettingsAbout.class);
                startActivityForResult(intent, 1);
                aboutBtn.setEnabled(true);
                break;

            case R.id.fileticket:
                fileticketBtn.setEnabled(false);
                intent = new Intent(getActivity(), SettingsFileTicket.class);
                startActivityForResult(intent, 1);
                fileticketBtn.setEnabled(true);
                break;

            case R.id.walleticon:


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
                                mywalleticon.setEnabled(true);
                                walleticonstat(0);
                                progressDialog.hideDialog();
                                Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }

                    });

                    newsession.execute(PartnerID);

                }

                break;

            case R.id.deactivate:

                deactivateBtn.setEnabled(false);

                if (Double.parseDouble(txtwallet.getText().toString().replace(",", "")) > 0) {
                    deactivateBtn.setEnabled(true);
                    Toast.makeText(getActivity(), "Failed to deactivate. You can't deactivate your account if you still have available credits.", Toast.LENGTH_LONG).show();

                } else {
                    openDialog();

                }

                break;
        }

    }

    //endregion

    //region FUNCTIONS

    //************************
    //FUNCTIONS
    //************************

    public void wallet() {

        FetchWallet fetchWallet = new FetchWallet(getActivity());

        fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

            @SuppressWarnings("unchecked")
            public void QuerySuccessFul(String data) {

                String[] rawdata = data.split(";");

                if (rawdata[0].equals("Success")) {
                    if (txtwallet != null) {
                        txtwallet.setText("" + rawdata[1]);
                        mywalleticon.setEnabled(true);
                        walleticonstat(0);
                        progressDialog.hideDialog();
                    }
                } else {
                    mywalleticon.setEnabled(true);
                    walleticonstat(0);
                    progressDialog.hideDialog();
                    Toast.makeText(getActivity(), "Failed to fetch Wallet from the server. Please try again.", Toast.LENGTH_LONG).show();
                }
            }


        });

        fetchWallet.execute(imei, SessionID, PartnerID);

    }

    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public void walleticonstat(Integer val) {

        if (val.equals(1)) {
            mywalleticon.setBackgroundResource(walletoff);
        } else {
            mywalleticon.setBackgroundResource(walleton);
        }
    }

    public void openDialog() {

        // , R.style.AppCompatAlertDialogStyle

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Choosing 'Deactivate' will clear all of your data and remove your account from all other devices. Please note that you can't deactivate your account if you still have available credits.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deactivateAccount();
            }
        });

        //On pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deactivateBtn.setEnabled(true);
                dialog.cancel();
            }
        });

        // alertDialog.show();

        AlertDialog alert11 = alertDialog.create();
        alert11.show();


        Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonbackground.setTextColor(Color.parseColor("#646464"));

        Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));


    }

    public void deactivateAccount() {

        int status = NetworkUtil.getConnectivityStatusString(getActivity());

        if (status == 0) {
            CheckInternet.showConnectionDialog(getActivity());
        } else {


//            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//            installation.remove("channels");
//            installation.saveEventually(new SaveCallback() {  // or saveInBackground
//                @Override
//                public void done(ParseException e) {
//                    // TODO: add code here
//                }
//            });

            verifySession();

        }


    }

    public void verifySession() {

        CreateSession newsession = new CreateSession(getActivity());

        newsession.setQueryListener(new CreateSession.QueryListener() {
            @SuppressWarnings("unchecked")
            public void QuerySuccessFul(String data) {


                String[] rawdata = data.split(";");
                if (rawdata[0].equals("Success")) {
                    SessionID = rawdata[1];
                    new initateDeactivationProcess().execute();
                } else {

                    Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    deactivateBtn.setEnabled(true);

                }

            }

        });

        newsession.execute(PartnerID);


    }

    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************

    class initateDeactivationProcess extends AsyncTask<Void, Void, String> {

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
            progressDialog.showDialog(getActivity(), "Deactivation", "Deactivating profile information. Please wait...", false);
        }

        @Override
        protected String doInBackground(Void... params) {

            String authcode = GlobalFunctions.getSha1Hex(imei.toLowerCase() + regmobile + "D E A C T I V A T E" + SessionID.toLowerCase());
            String url = DEACTIVATE + "&IMEI=" + imei + "&SourceMobTel=" + regmobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "&ToDo=DEACTIVATE";

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(url);

            Log.d("URI", url);

            String text = null;

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
            //btnProfile.setEnabled(true);

            deactivateBtn.setEnabled(true);

            if (s == null) {

                Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();

            } else {

                try {

                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("DeactivateAccount");
                    String sessionresult = articles.getString("Result");


                    if (sessionresult.equals("OK")) {
                        db.DropAllTable(db);

                        Intent intent = new Intent(getActivity(), Community.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getActivity(), articles.getString("Message"), Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {

                }

            }


        }


    }

    //endregion


}
