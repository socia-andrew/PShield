package com.budgetload.materialdesign.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.DialogError;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.GlobalVariables;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.commonFunctions;
import com.budgetload.materialdesign.Common.logoutDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity implements OnItemSelectedListener, View.OnClickListener, Constant {

    //declaration here
    DataBaseHandler db;
    logoutDialog logoutDialog;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtMiddleName;
    EditText txtEmail;
    EditText txtaddress;
    EditText txtBday;
    EditText txtoccupation;
    EditText txtinterest;

    String firstname = "NONE";
    String lastname = "NONE";
    String middlename = "NONE";
    String email = "NONE";
    String address = "";
    String beerday = "";
    String occupation = "";
    String interest = "";
    String selectedGender = "";
    String SessionID = "";
    String groupcode = "";
    Button btnProfile;
    public MenuItem mnuProfile;

    Spinner spinGender;
    Spinner spinCommunity;
    String[] Gender;
    String PartnerID;

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    ArrayAdapter<String> spinGenderAdapter;
    ArrayAdapter<String> spinGroupAdapter;
    ArrayList<String> ArrGroupName = new ArrayList<String>();
    ArrayList<String> ArrGroupCode = new ArrayList<String>();

    commonFunctions commFunc;
    Boolean myresult;
    String imei;
    String regmobile;
    String referrer;
    String menuTitle = "SKIP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize database
        db = new DataBaseHandler(this);
        //get IMEI
        imei = getIMEI();
        GlobalVariables.imei = imei;
        //get partner
        PartnerID = db.getPartnerID(db);

        //get other info
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                regmobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                referrer = cursor.getString(cursor.getColumnIndex("Referrer"));
            } while (cursor.moveToNext());
        }

        //initialize objects
        commFunc = new commonFunctions(getBaseContext());
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Gender = getResources().getStringArray(R.array.gender);
        txtFirstName = (EditText) findViewById(R.id.firstname);
        txtLastName = (EditText) findViewById(R.id.lastname);
        txtMiddleName = (EditText) findViewById(R.id.middlename);
        txtEmail = (EditText) findViewById(R.id.emailaddress);
        txtaddress = (EditText) findViewById(R.id.address);
        txtoccupation = (EditText) findViewById(R.id.occupation);
        txtinterest = (EditText) findViewById(R.id.interest);
        txtBday = (EditText) findViewById(R.id.beerday);
        spinGender = (Spinner) findViewById(R.id.gender);
        spinCommunity = (Spinner) findViewById(R.id.groupcode);
        txtBday.setInputType(InputType.TYPE_NULL);
        txtBday.setFocusable(false);
        txtBday.setOnClickListener(this);


        //FirstName input listener
        txtFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                myresult = commFunc.ValidateEmpty(new EditText[]{txtFirstName, txtLastName, txtMiddleName, txtEmail});
                if (myresult) {
                    menuTitle = "SUBMIT";
                } else {
                    menuTitle = "SKIP";
                }
                invalidateOptionsMenu();
            }
        });

        //Lastname input listener
        txtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                myresult = commFunc.ValidateEmpty(new EditText[]{txtFirstName, txtLastName, txtMiddleName, txtEmail});

                if (myresult) {
                    menuTitle = "SUBMIT";
                } else {
                    menuTitle = "SKIP";
                }
                invalidateOptionsMenu();
            }
        });

        //MIDDLENAME input listener
        txtMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                myresult = commFunc.ValidateEmpty(new EditText[]{txtFirstName, txtLastName, txtMiddleName, txtEmail});
                if (myresult) {
                    menuTitle = "SUBMIT";
                } else {
                    menuTitle = "SKIP";
                }
                invalidateOptionsMenu();
            }
        });

        //Email input listener
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                myresult = commFunc.ValidateEmpty(new EditText[]{txtFirstName, txtLastName, txtMiddleName, txtEmail});
                if (myresult) {
                    menuTitle = "SUBMIT";
                } else {
                    menuTitle = "SKIP";
                }
                invalidateOptionsMenu();

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        //get IMEI
        imei = getIMEI();
        GlobalVariables.imei = imei;
        //get partner
        PartnerID = db.getPartnerID(db);

        //get other info
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                regmobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                referrer = cursor.getString(cursor.getColumnIndex("Referrer"));
            } while (cursor.moveToNext());
        }

    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        mnuProfile = menu.findItem(R.id.skip);
        mnuProfile.setTitle(menuTitle);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Clicking SKIP or Submit Button
            case R.id.skip:
                //Skip option
                if (item.getTitle().toString().equalsIgnoreCase("Skip")) {
                    //save proofile in local data
                    db.updatePreRegistration(db, regmobile, "Verified");
                    db.saveProfile(db, firstname, lastname, middlename, email, address, beerday, occupation, interest, selectedGender, referrer, regmobile, groupcode);

                    Bundle b = new Bundle();
                    b.putString("ReturnHero", "No");

                    //open main activity
                    Intent iinent = new Intent(this, MainActivity.class);
                    startActivity(iinent);
                    iinent.putExtras(b);
                    iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ActivityCompat.finishAffinity(this);

                } else { //Submit Option

                    item.setEnabled(false);
                    //checking inputs
                    myresult = commFunc.validate(new EditText[]{txtFirstName, txtLastName, txtMiddleName});

                    if (!myresult) {
                        DialogError.alertDialogShow(this, "BudgetLoad",
                                "Please provide your basic information to continue.");
                        item.setEnabled(true);
                    } else {

                        //getting inputs
                        firstname = txtFirstName.getText().toString();
                        lastname = txtLastName.getText().toString();
                        middlename = txtMiddleName.getText().toString();
                        email = txtEmail.getText().toString();
                        beerday = txtBday.getText().toString();
                        occupation = txtoccupation.getText().toString();
                        interest = txtinterest.getText().toString();
                        address = txtaddress.getText().toString();

                        //check connection status
                        int status = NetworkUtil.getConnectivityStatusString(this);
                        if (status == 0) { //no connection
                            CheckInternet.showConnectionDialog(this);
                            item.setEnabled(true);
                        } else { //has connection
                            //validating email address
                            if (txtEmail.getText().length() > 0) {
                                boolean emailvalidity = isEmailValid(txtEmail.getText().toString());
                                if (emailvalidity == false) {
                                    item.setEnabled(true);
                                    DialogError.alertDialogShow(this, "BudgetLoad", "Invalid Email Address.");
                                } else {
                                    verifySession();
                                }
                            } else {
                                verifySession();
                            }
                        }
                    }

                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        logoutDialog.logoutDialogShow(this, Profile.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
    }

    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void verifySession() {

        progressDialog.showDialog(Profile.this, "Profile", "Saving basic information. Please wait...", false);

        //create session
        CreateSession newsession = new CreateSession(this);
        newsession.setQueryListener(new CreateSession.QueryListener() {
            @SuppressWarnings("unchecked")
            public void QuerySuccessFul(String data) {
                String[] rawdata = data.split(";");
                if (rawdata[0].equals("Success")) {
                    SessionID = rawdata[1];
                    new createNewProfile().execute();
                } else {
                    Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    progressDialog.hideDialog();
                }
            }

        });
        newsession.execute(PartnerID);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public String getIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getDeviceId();

    }


    //endregion


    //region THREADS
    //************************
    //THREADS
    //************************

    class createNewProfile extends AsyncTask<Void, Void, String> {

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


            if (lastname == null) {
                lastname = ".";
            }
            if (firstname == null) {
                firstname = ".";
            }
            if (middlename == null) {
                middlename = ".";
            }
            if (address == null) {
                address = ".";
            }
            if (beerday == null) {
                beerday = "0000-00-00";
            }
            if (email == null) {
                email = ".";
            }
            if (interest == null) {
                interest = ".";
            }
            if (groupcode == null) {
                groupcode = ".";
            }
            if (occupation == null) {
                occupation = ".";
            }
            if (referrer == null) {
                referrer = ".";
            }

            if (selectedGender == null) {
                selectedGender = ".";
            }


        }

        @Override
        protected String doInBackground(Void... params) {

            String text;
            try {

                String myarray = imei + "" + regmobile + "" + Constant.updateprofile + "" + SessionID.toLowerCase();
                String authcode = GlobalFunctions.generateSha(myarray);

                String url = UPDATEURL + "&IMEI=" + imei
                        + "&SourceMobTel=" + regmobile
                        + "&SessionNo=" + SessionID
                        + "&LName=" + URLEncoder.encode(lastname.trim())
                        + "&FName=" + URLEncoder.encode(firstname.trim())
                        + "&MName=" + URLEncoder.encode(middlename.trim())
                        + "&Address=" + URLEncoder.encode(address.trim())
                        + "&BirthDate=" + URLEncoder.encode(beerday.trim())
                        + "&Email=" + URLEncoder.encode(email.trim())
                        + "&Interest=" + URLEncoder.encode(interest.trim())
                        + "&Occupation=" + URLEncoder.encode(occupation.trim())
                        + "&GroupCode=" + URLEncoder.encode(groupcode.trim())
                        + "&Referrer=" + URLEncoder.encode(referrer.trim())
                        + "&Gender=" + URLEncoder.encode(selectedGender)
                        + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";

                //   Log.d("URI",url);

                HttpGet httpGet = new HttpGet(url);
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
                    JSONObject articles = json.getJSONObject("UpdateProfile");
                    String sessionresult = articles.getString("Result");

                    if (sessionresult.equals("OK")) {
                        //update local data
                        db.updatePreRegistration(db, regmobile, "Verified");
                        db.saveProfile(db, firstname, lastname, middlename, email, address, beerday, occupation, interest, selectedGender, referrer, regmobile, groupcode);
                        Bundle b = new Bundle();
                        b.putString("ReturnHero", "No");
                        //open main page
                        Intent intent = new Intent(Profile.this, MainActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);

                        b.putString("FirstName", firstname);
                        b.putString("LastName", lastname);
                        b.putString("MiddleName", middlename);
                        b.putString("Email", email);
                        b.putString("Address", address);
                        b.putString("Bday", beerday);
                        b.putString("Gender", selectedGender);
                        b.putString("Mobile", regmobile);
                        GlobalFunctions.fbLogger(Profile.this, "Profiling", b, 1);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ActivityCompat.finishAffinity(Profile.this);

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
