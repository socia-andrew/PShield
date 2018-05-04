package com.budgetload.materialdesign.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.MainActivity;

import org.apache.commons.lang3.text.WordUtils;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.budgetload.materialdesign.Common.GlobalVariables.PartnerID;
import static com.budgetload.materialdesign.Common.GlobalVariables.SessionID;
import static com.budgetload.materialdesign.Common.GlobalVariables.imei;

/**
 * Created by andrewlaurienrsocia on 05/10/2016.
 */

public class RequestCredits implements Constant {

    static Context mContext;
    static EditText editAmount;
    static EditText editMobile;
    static Spinner mySpinner;
    static String CommunityID;
    static String mymobile;
    static String myrequestor;


    // Constructor
    public RequestCredits(Context context) {
        this.mContext = context;
    }

    public static void showRequestDialog(final Context context, final Activity activity, final String partnerid, final String requestor) {


        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_request_credits);

        mContext = context;
        myrequestor = requestor;

        editMobile = (EditText) dialog.findViewById(R.id.editMobile);
        editAmount = (EditText) dialog.findViewById(R.id.editAmount);
        mySpinner = (Spinner) dialog.findViewById(R.id.spinComm);

        mySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(context, mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        CommunityID = mySpinner.getSelectedItem().toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


        editMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    mymobile = editMobile.getText().toString();
                    try {
                        int status = NetworkUtil.getConnectivityStatusString(activity);
                        if (status == 0) {
                            CheckInternet.showConnectionDialog(activity);
                            progressDialog.hideDialog();
                        } else {
                            final CreateSession newsession = new CreateSession(activity);
                            newsession.setQueryListener(new CreateSession.QueryListener() {
                                @SuppressWarnings("unchecked")
                                public void QuerySuccessFul(String data) {
                                    String[] rawdata = data.split(";");
                                    if (rawdata[0].equals("Success")) {
                                        SessionID = rawdata[1];
                                        new checkCommunity().execute();
                                    } else {
                                        Toast.makeText(activity, "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            newsession.execute(PartnerID);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button dialogButton = (Button) dialog.findViewById(R.id.btnRequest);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMobile.getText().length() == 0 || editMobile.getText().length() == 0) {
                    Toast.makeText(context, "Invalid data", Toast.LENGTH_SHORT).show();
                } else {
                    if (editMobile.getText().length() < 11 && Integer.parseInt(editAmount.getText().toString()) != 0) {
                        Toast.makeText(context, "Invalid Mobile", Toast.LENGTH_SHORT).show();
                    } else if (editMobile.getText().length() == 11 && editAmount.getText().length() == 0) {//Integer.parseInt(editAmount.getText().toString()) == 0
                        Toast.makeText(context, "Invalid Amount", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Integer.parseInt(editAmount.getText().toString()) != 0) {
                            String myrequestor = "0" + requestor;
                            Log.d("Data", myrequestor);
                            Log.d("Data", editMobile.getText().toString());
                            if (!myrequestor.equalsIgnoreCase(editMobile.getText().toString())) {
                                DateFormat df = new SimpleDateFormat("MMMM dd, yyyy HH:mm a");
                                String newdate = df.format(Calendar.getInstance().getTime());
                                try {
                                    String rsender = "";
                                    if (!MainActivity.user.wholename.isEmpty() && MainActivity.user.wholename.length() > 3) {
                                        rsender = WordUtils.capitalize(MainActivity.user.wholename);
                                    } else {
                                        rsender = requestor;
                                    }
                                    JSONObject mymsg = new JSONObject();
                                    mymsg.put("message", rsender + " is requesting for " + editAmount.getText().toString() + ".00" + " credits.");
                                    mymsg.put("message_title", "Request Credits");
                                    mymsg.put("sender", "BudgetLoad");
                                    mymsg.put("rmobile", "0" + requestor);
                                    mymsg.put("datesent", newdate);
                                    mymsg.put("time_to_live", "200,000");
                                    JSONObject myobj = new JSONObject();
                                    myobj.put("to", "/topics/" + partnerid + "_" + editMobile.getText().toString());
                                    myobj.put("data", mymsg);
                                    Bundle b = new Bundle();
                                    b.putString("receiver", editMobile.getText().toString());
                                    GlobalFunctions.fbLogger(activity, "RequestCredits", b, 1);
                                    SendNotification sendNotification = new SendNotification(activity);
                                    sendNotification.execute(myrequestor, SessionID, CommunityID, myobj.toString());
                                    Toast.makeText(context, "Request successfully sent.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "Cannot send request to own number.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Invalid Amount.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        dialog.show();
    }


    public static class checkCommunity extends AsyncTask<Void, Void, String> {

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
                String apiURL = OTHERSURL + "&CMD=RECEIVERCOMMUNITY&IMEI=" + imei + "&TargetMobile=" + mymobile + "&PartnerID=" + PartnerID + "&SourceMobile=" + myrequestor + "";
                Log.d("URI", apiURL);
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
                Log.d("Exception", e.toString());
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String res = "";

            Log.d("Data", s);

            JSONArray commlist;

            if (s == null) {
            } else {
                try {
                    JSONArray json = new JSONArray(s);
                    JSONObject prefixarr = json.getJSONObject(0);
                    JSONObject articles = prefixarr.getJSONObject("Community");
                    String sessionresult = articles.getString("Result");
                    if (sessionresult.equals("OK")) {
                        JSONObject obj = json.getJSONObject(1);
                        commlist = obj.getJSONArray("Community");
                        if (commlist.length() > 1) {

                            List<String> list;
                            list = new ArrayList<String>();

                            mySpinner.setVisibility(View.VISIBLE);

                            for (int i = 0; i < commlist.length(); i++) {
                                JSONObject myobj = commlist.getJSONObject(i);
                                list.add(myobj.getString("NetworkID"));
                            }

                            ArrayAdapter<String> adapter;
                            adapter = new ArrayAdapter<String>(mContext,
                                    R.layout.spinner_item, list);
                            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mySpinner.setAdapter(adapter);

                        } else if (commlist.length() == 1) {
                            JSONObject c = commlist.getJSONObject(0);
                            // mySpinner.setVisibility(View.VISIBLE);
                            CommunityID = c.getString("NetworkID");
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    if (res.equals("OK")) {
                    }
                }
            }
        }
    }


}



