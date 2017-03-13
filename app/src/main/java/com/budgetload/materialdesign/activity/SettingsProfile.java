package com.budgetload.materialdesign.activity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.HideKeyboard;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsProfile extends AppCompatActivity implements Constant, View.OnClickListener {

    EditText firstname;
    EditText middlenameval;
    EditText lastnameval;
    EditText mobileval;
    EditText emailval;
    EditText addressval;
    EditText bdayval;
    Spinner genderval;
    Spinner groupcodeval;
    EditText occupationval;
    EditText interestval;
    EditText dealertypeval;
    EditText referrerval;
    EditText communityval;
    TextView fullname;
    TextView txtgroupcode;
    RelativeLayout fnamerow;
    RelativeLayout mnamerow;
    RelativeLayout lnamerow;

    String txteditfirst = "";
    String txtmiddlename = "";
    String txteditlast = "";
    String txteditemail = "";
    String txteditaddress = "";
    String txtbday = "";
    String txtgender = "";
    String txtoccupation = "";
    String txtinterest = "";
    String txtdealertype = "";
    String txtreferrer = "";
    String txtgroup = "";
    String SessionID;
    String groupcode;
    String imei;

    String previousGroupCode;

    DataBaseHandler db;
    String options = "edit";
    boolean emailvalidity = false;
    String regmobile;
    ImageView myImageView;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    Context mcontext;
    private Menu menu;
    String[] Gender;
    String selectedGender;
    String referrer;
    TableRow header, header2;

    ArrayAdapter<String> spinGenderAdapter;
    ArrayAdapter<String> spinGroupAdapter;


    ArrayList<String> ArrGroupName = new ArrayList<String>();
    ArrayList<String> ArrGroupCode = new ArrayList<String>();
    String PartnerID;

    EditText inputpass;
    AlertDialog confimation;
    AlertDialog.Builder builder;
    String PasswordStatus = "";
    String confimpass = "";
    String CurrentPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize database
        db = new DataBaseHandler(this);
        //get other info in local
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                regmobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                referrer = cursor.getString(cursor.getColumnIndex("Referrer"));
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

        //for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //COnfirmation for topUp
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        inputpass = new EditText(this);
        inputpass.setHint("Password");
        inputpass.setPadding(60, 10, 60, 10);
        inputpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputpass, 10, 100, 10, 20);
        builder.setCancelable(false);


        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                confimpass = GlobalFunctions.getSha1Hex(inputpass.getText().toString());
                updateProfile();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MenuItem item = menu.findItem(R.id.edit);
                item.setEnabled(true);
                inputpass.setText("");
            }
        });
        confimation = builder.create();


        //intializing objects
        txtgroupcode = (TextView) findViewById(R.id.groupcode);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        PartnerID = db.getPartnerID(db);
        //setting value of profile
        Gender = getResources().getStringArray(R.array.gender);
        firstname = (EditText) findViewById(R.id.editfirstname);
        middlenameval = (EditText) findViewById(R.id.editmiddlename);
        lastnameval = (EditText) findViewById(R.id.editlastname);
        fullname = (TextView) findViewById(R.id.fullname);
        mobileval = (EditText) findViewById(R.id.mobileval);
        emailval = (EditText) findViewById(R.id.emailval);
        addressval = (EditText) findViewById(R.id.addressval);
        bdayval = (EditText) findViewById(R.id.bdayval);
        genderval = (Spinner) findViewById(R.id.genderval);
        occupationval = (EditText) findViewById(R.id.occupationval);
        interestval = (EditText) findViewById(R.id.interestval);
        dealertypeval = (EditText) findViewById(R.id.dealerval);
        referrerval = (EditText) findViewById(R.id.refval);
        groupcodeval = (Spinner) findViewById(R.id.groupval);
        communityval = (EditText) findViewById(R.id.commuval);
        myImageView = (ImageView) findViewById(R.id.image_view);
        header = (TableRow) findViewById(R.id.header1);
        header2 = (TableRow) findViewById(R.id.header2);
        fnamerow = (RelativeLayout) findViewById(R.id.fnamewrap);
        mnamerow = (RelativeLayout) findViewById(R.id.mnamewrap);
        lnamerow = (RelativeLayout) findViewById(R.id.lnamewrap);

        mcontext = this;
        bdayval.setInputType(InputType.TYPE_NULL);
        bdayval.setFocusable(false);
        bdayval.setOnClickListener(this);
        imei = getIMEI();
        GlobalVariables.imei = imei;
        falseEditView();

        //Setup gender Spinner
        spinGenderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Gender);
        spinGenderAdapter.setDropDownViewResource(R.layout.spinner_item);
        genderval.setAdapter(spinGenderAdapter);

        genderval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderval.setSelection(position);
                ((TextView) genderval.getSelectedView()).setTextColor(Color.parseColor("#5e7bb8"));
                selectedGender = Gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrGroupName.add("Select a Group");
        ArrGroupCode.add("");


        //displaying profile
        openProfileView();


        groupcodeval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                groupcodeval.setSelection(position);
                ((TextView) groupcodeval.getSelectedView()).setTextColor(Color
                        .parseColor("#5e7bb8"));
                groupcode = ArrGroupCode.get(position);
                previousGroupCode = ArrGroupCode.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                bdayval.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR) - 20, newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        String firstLetter = "";
        if (txteditfirst.equalsIgnoreCase("None") || txteditfirst.equalsIgnoreCase(".")) {
            firstLetter = "#";
            fullname.setVisibility(View.GONE);
        } else {
            firstLetter = String.valueOf(fullname.getText().charAt(0));
            fullname.setVisibility(View.VISIBLE);
        }

        ColorGenerator generator = ColorGenerator.MATERIAL;
        // generate random color
        TextDrawable drawable = TextDrawable.builder()//Color.parseColor("#2B60D0")
                .beginConfig()
                .withBorder(4) /* thickness in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(firstLetter.toUpperCase(), Color.parseColor("#2f63cd")); // radius in px
        myImageView.setImageDrawable(drawable);

        setSession();


    }


    //region TRIGGERS
//************************
//TRIGGERS
//************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                if (options.equals("edit")) {
                    item.setTitle("DONE");
                    openEditView();
                    options = "done";
                    fullname.setVisibility(View.GONE);
                    fnamerow.setVisibility(View.VISIBLE);
                    mnamerow.setVisibility(View.VISIBLE);
                    lnamerow.setVisibility(View.VISIBLE);
                    myImageView.setVisibility(View.GONE);
                    header.setVisibility(View.GONE);
                    header2.setVisibility(View.GONE);
                } else {

                    if (PasswordStatus.equals("on")) {
                        inputpass.setVisibility(View.VISIBLE);
                    } else {
                        inputpass.setVisibility(View.GONE);
                    }

                    confimation.setMessage("Are you sure you want to update profile?");
                    confimation.show();//show confimation modal
                    confimation.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#646464"));
                    confimation.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#19BD4E"));

                    item.setEnabled(false);
                    item.setTitle("EDIT");
                    options = "edit";
                    fullname.setVisibility(View.VISIBLE);
                    fnamerow.setVisibility(View.GONE);
                    mnamerow.setVisibility(View.GONE);
                    lnamerow.setVisibility(View.GONE);
                    myImageView.setVisibility(View.VISIBLE);
                    header.setVisibility(View.VISIBLE);
                    header2.setVisibility(View.VISIBLE);


                }
                return true;
            case android.R.id.home:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings_profile, menu);
        this.menu = menu;
        return true;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bdayval:
                HideKeyboard.hideKeyboard(SettingsProfile.this);
                fromDatePickerDialog.show();


                fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", fromDatePickerDialog);
                fromDatePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#646464"));

                fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", fromDatePickerDialog);
                fromDatePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#19BD4E"));


                break;
        }

    }


    //endregion


    //region FUNCTIONS
//************************
//FUNCTIONS
//************************


    public String getIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getDeviceId();

    }

    private void openProfileView() {
        try {
            Cursor dr = db.getProfile(db);
            int i = 0;

            //Toast.makeText(getBaseContext(), "" + dr.getCount() + "", Toast.LENGTH_LONG).show();

            if (dr.getCount() > 0) {
                dr.moveToFirst();
                do {
                    //here
                    if (dr.getString(2).equalsIgnoreCase(".") || dr.getString(2).equalsIgnoreCase("None")) {
                        firstname.setText("None");
                        txteditfirst = "None";
                    } else {
                        firstname.setText(dr.getString(2));
                        txteditfirst = dr.getString(2);
                    }
                    if (dr.getString(3).equalsIgnoreCase(".") || dr.getString(3).equalsIgnoreCase("None")) {
                        middlenameval.setText("None");
                    } else {
                        middlenameval.setText(dr.getString(3));
                    }
                    if (dr.getString(4).equalsIgnoreCase(".") || dr.getString(4).equalsIgnoreCase("None")) {
                        lastnameval.setText("None");
                    } else {
                        lastnameval.setText(dr.getString(4));
                    }
                    fullname.setText(dr.getString(2) + " " + dr.getString(3) + " " + dr.getString(4) + "");

                    bdayval.setText(dr.getString(6));
                    if (dr.getString(7).equalsIgnoreCase(".") || dr.getString(7).equalsIgnoreCase("")) {
                        emailval.setText("None");
                    } else {
                        emailval.setText(dr.getString(7));
                    }
                    if (dr.getString(8).equalsIgnoreCase(".") || dr.getString(8).equalsIgnoreCase("")) {
                        addressval.setText("None");
                    } else {
                        addressval.setText(dr.getString(8));
                    }
                    if (dr.getString(9).equalsIgnoreCase(".") || dr.getString(9).equalsIgnoreCase("")) {
                        occupationval.setText("None");
                    } else {
                        occupationval.setText(dr.getString(9));
                    }
                    if (dr.getString(10).equalsIgnoreCase(".") || dr.getString(10).equalsIgnoreCase("")) {
                        interestval.setText("None");
                    } else {
                        interestval.setText(dr.getString(10));
                    }
                    if (dr.getString(11).equalsIgnoreCase(".") || dr.getString(11).equalsIgnoreCase("None")) {
                        referrerval.setText("None");
                    } else {
                        referrerval.setText(dr.getString(11));
                    }
                    mobileval.setText("+63" + regmobile);
                    dealertypeval.setText(dr.getString(13));

                    txtgroupcode.setText(dr.getString(14));
                    communityval.setText(PartnerID);

                    previousGroupCode = dr.getString(14);


                    if (!(dr.getString(5) == null)) {
                        if (dr.getString(5).equalsIgnoreCase("Male")) {
                            genderval.setSelection(1);
                        } else if (dr.getString(5).equalsIgnoreCase("FeMale")) {
                            genderval.setSelection(2);
                        }
                    } else if (dr.getString(5) == ".") {
                        genderval.setSelection(0);
                    } else {
                        genderval.setSelection(0);
                    }


                    groupcodeval.setSelection(ArrGroupCode.indexOf(dr.getString(14)));


                } while (dr.moveToNext());
            }
        } catch (Exception e) {
        }

    }

    private void openEditView() {

        try {
            firstname.setEnabled(true);
            middlenameval.setEnabled(true);
            lastnameval.setEnabled(true);
            emailval.setEnabled(true);
            addressval.setEnabled(true);
            bdayval.setEnabled(true);
            genderval.setEnabled(true);
            occupationval.setEnabled(true);
            interestval.setEnabled(true);
            groupcodeval.setEnabled(false);
            txtgroupcode.setEnabled(false);

        } catch (Exception e) {
        }


    }

    private void falseEditView() {
        try {
            firstname.setEnabled(false);
            middlenameval.setEnabled(false);
            lastnameval.setEnabled(false);
            mobileval.setEnabled(false);
            communityval.setEnabled(false);
            emailval.setEnabled(false);
            addressval.setEnabled(false);
            bdayval.setEnabled(false);
            genderval.setEnabled(false);
            occupationval.setEnabled(false);
            interestval.setEnabled(false);
            groupcodeval.setEnabled(false);
            dealertypeval.setEnabled(false);
            referrerval.setEnabled(false);
            txtgroupcode.setEnabled(false);
        } catch (Exception e) {
        }


    }


    private void updateProfile() {

        try {

            if ((PasswordStatus.equals("on") && confimpass.equals(CurrentPassword)) || PasswordStatus.equals("off") || PasswordStatus.equals("")) {
                txteditfirst = firstname.getText().toString();
                txtmiddlename = middlenameval.getText().toString();
                txteditlast = lastnameval.getText().toString();
                txteditemail = emailval.getText().toString();
                txteditaddress = addressval.getText().toString();
                txtbday = bdayval.getText().toString();
                txtgender = genderval.getSelectedItem().toString();
                txtoccupation = occupationval.getText().toString();
                txtinterest = interestval.getText().toString();
                //txtgroup = groupcodeval.getSelectedItem().toString();
                txtdealertype = dealertypeval.getText().toString();
                txtreferrer = referrerval.getText().toString();
                groupcode = txtgroupcode.getText().toString();
                if (txteditfirst.equals("") || txteditfirst.equals(".")) {
                    txteditfirst = "NONE";
                }
                if (txtmiddlename.equals("") || txtmiddlename.equals(".")) {
                    txtmiddlename = "NONE";
                }
                if (txteditlast.equals("") || txteditlast.equals(".")) {
                    txteditlast = "NONE";
                }
                if (txteditaddress.equals("")) {
                    txteditaddress = ".";
                }
                if (txtgender.equals("Select Gender")) {
                    txtgender = ".";
                }
                if (txteditemail.equals("NONE")) {
                    txteditemail = ".";
                }

                int status = NetworkUtil.getConnectivityStatusString(this);

                if (status == 0) {
                    CheckInternet.showConnectionDialog(this);
                    MenuItem item = menu.findItem(R.id.edit);
                    item.setEnabled(true);
                } else {
                    verifySession();

                }
            } else {
                Toast.makeText(getBaseContext(), "Password did not match", Toast.LENGTH_SHORT).show();
                MenuItem item = menu.findItem(R.id.edit);
                item.setEnabled(true);
            }

        } catch (Exception e) {
        }

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

    public void setSession() {

        try {
            CreateSession newsession = new CreateSession(this);

            newsession.setQueryListener(new CreateSession.QueryListener() {
                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {


                    String[] rawdata = data.split(";");
                    if (rawdata[0].equals("Success")) {
                        SessionID = rawdata[1];
                        new fetchProfile().execute();
                    } else {

                        Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                        MenuItem item = menu.findItem(R.id.edit);
                        item.setEnabled(true);
                        falseEditView();

                    }

                }

            });

            newsession.execute(PartnerID);
        } catch (Exception e) {
        }

    }

    public void verifySession() {
        try {
            progressDialog.showDialog(SettingsProfile.this, "Profile", "Saving profile information. Please wait...", false);
            CreateSession newsession = new CreateSession(this);
            newsession.setQueryListener(new CreateSession.QueryListener() {
                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {


                    String[] rawdata = data.split(";");
                    if (rawdata[0].equals("Success")) {
                        SessionID = rawdata[1];
                        new UpdateProfile().execute();
                    } else {
                        progressDialog.hideDialog();
                        Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                        MenuItem item = menu.findItem(R.id.edit);
                        item.setEnabled(true);
                        falseEditView();

                    }

                }

            });

            newsession.execute(PartnerID);
        } catch (Exception e) {
        }

    }
    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    class UpdateProfile extends AsyncTask<Void, Void, String> {

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
                String authcode = GlobalFunctions.getSha1Hex(imei + regmobile + Constant.updateprofile + SessionID.toLowerCase());
                String url = UPDATEURL + "&IMEI=" + imei
                        + "&SourceMobTel=" + regmobile
                        + "&SessionNo=" + SessionID
                        + "&Address=" + URLEncoder.encode(txteditaddress.trim())
                        + "&BirthDate=" + URLEncoder.encode(txtbday.trim())
                        + "&LName=" + URLEncoder.encode(txteditlast.trim())
                        + "&FName=" + URLEncoder.encode(txteditfirst.trim())
                        + "&MName=" + URLEncoder.encode(txtmiddlename.trim())
                        + "&Email=" + URLEncoder.encode(txteditemail.trim())
                        + "&Interest=" + URLEncoder.encode(txtinterest.trim())
                        + "&Occupation=" + URLEncoder.encode(txtoccupation.trim())
                        + "&GroupCode=" + URLEncoder.encode(groupcode.trim())
                        + "&Gender=" + URLEncoder.encode(txtgender)
                        + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";

                Log.d("URI", url);
                HttpGet httpGet = new HttpGet(url);
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
                progressDialog.hideDialog();
                MenuItem item = menu.findItem(R.id.edit);
                item.setEnabled(true);

                if (s == null) {

                    Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();

                } else {

                    try {

                        JSONObject json = new JSONObject(s);
                        JSONObject articles = json.getJSONObject("UpdateProfile");
                        String sessionresult = articles.getString("Result");


                        if (sessionresult.equals("OK")) {
                            if (!previousGroupCode.equalsIgnoreCase("ALL")) {
                                // ParseUtils.unSubscribeParse(previousGroupCode);
                            }
                            if (!groupcode.equalsIgnoreCase(".") || !groupcode.equalsIgnoreCase("ALL")) {//!txtgroup.equalsIgnoreCase("none") || || !txtgroup.equalsIgnoreCase("Select a Group")
                                //  ParseUtils.subscribetoParse(mcontext, PartnerID + "_" + groupcode);

                            }

                            MainActivity.user.firstname = txteditfirst;
                            MainActivity.user.middlename = txtmiddlename;
                            MainActivity.user.lastname = txteditlast;
                            MainActivity.user.email = txteditemail;


                            db.updateProfile(db, txteditfirst, txteditlast, txteditemail, txteditaddress, txtbday, txtgender, txtoccupation, txtinterest, groupcode, txtmiddlename, txtdealertype, txtreferrer);
                            falseEditView();
                            openProfileView();

                            String firstLetter = "";

                            if (!txteditfirst.equalsIgnoreCase("NONE")) {
                                firstLetter = String.valueOf(fullname.getText().charAt(0));
                                fullname.setVisibility(View.VISIBLE);
                            } else {
                                firstLetter = "#";
                                fullname.setVisibility(View.VISIBLE);

                            }

                            ColorGenerator generator = ColorGenerator.MATERIAL;
                            // generate random color
                            TextDrawable drawable = TextDrawable.builder()//Color.parseColor("#2B60D0")
                                    .beginConfig()
                                    .withBorder(4) /* thickness in px */
                                    .bold()
                                    .toUpperCase()
                                    .endConfig()
                                    .buildRound(firstLetter.toUpperCase(), Color.parseColor("#2f63cd")); // radius in px
                            myImageView.setImageDrawable(drawable);

                        } else {

                            Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                            falseEditView();
                        }


                    } catch (JSONException e) {

                    }

                }
            } catch (Exception e) {
            }


        }


    }

    class fetchProfile extends AsyncTask<Void, Void, String> {

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

                String authcode = GlobalFunctions.getSha1Hex(imei + regmobile + Constant.getprofile + SessionID.toLowerCase());
                String apiURL = PROFILEURL + "&IMEI=" + imei + "&SourceMobTel=" + regmobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";

                //   Log.d("URI", apiURL);
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

                    Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();

                } else {

                    JSONObject json = new JSONObject(s);
                    JSONObject articles = json.getJSONObject("Profile");
                    String result = articles.getString("Result");

                    if (result.trim().equals("OK")) {


                        String fname = articles.getString("FirstName");
                        String lname = articles.getString("LastName");
                        String email = articles.getString("Email");
                        String address = articles.getString("Address");
                        String mname = articles.getString("MiddleName");
                        String gender = articles.getString("Gender");
                        String interest = articles.getString("Interest");
                        String occupation = articles.getString("Occupation");
                        String groupcode = articles.getString("GroupCode");
                        String dealertype = articles.getString("DealerType");
                        String referrer = articles.getString("Referrer");
                        String beerday = articles.getString("BirthDate");
                        String MessagingStatus = articles.getString("MessagingStatus");

                        Cursor cursor = db.getProfile(db);
                        String prevgroupcode = "";

                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                prevgroupcode = cursor.getString(cursor.getColumnIndex("GroupCode"));
                            }
                        }

                        if (MessagingStatus.equals("1")) {
//                            ParseUtils.unSubscribeParse(prevgroupcode);
//                            ParseUtils.subscribetoParse(mcontext, groupcode);
//                            ParseUtils.subscribetoParse(mcontext, PartnerID + "_ALL");
//                            ParseUtils.subscribetoParse(mcontext, PartnerID + "_" + groupcode);
                        } else {
//                            ParseUtils.unSubscribeParse(groupcode);
//                            ParseUtils.unSubscribeParse(PartnerID + "_ALL");
//                            ParseUtils.unSubscribeParse(PartnerID + "_" + groupcode);
                        }


                        db.saveReturnedHero(db, fname, lname, email, address, mname, gender, interest, occupation, groupcode, dealertype, referrer, beerday, regmobile);


                    } else {
                        Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {

            }
        }


    }

}

//endregion

