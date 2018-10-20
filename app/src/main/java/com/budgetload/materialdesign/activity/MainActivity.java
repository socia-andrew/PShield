package com.budgetload.materialdesign.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.GlobalVariables;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.logoutDialog;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.Constant.Indicators;
import com.budgetload.materialdesign.Constant.PreloadedData;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.Fragments.FragmentSettings;
import com.budgetload.materialdesign.activity.Fragments.Fragment_Notifications;
import com.budgetload.materialdesign.activity.Fragments.MainFragment;
import com.budgetload.materialdesign.model.User;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

//FragmentTopUp.GoToTransactions,
//,  FragmentStockTransfer.GoToTransactions
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        Constant {

    //Declaring objects and variables
    //get the name for profile

    //initializing database
    public static DataBaseHandler db;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    public static String imei;
    public static String regmobile;
    Context mcontext;
    String globesignature, NPGlobe;
    String sunsignature, NPSun;
    String smartsignature, NPSmart;
    String prefixsignature, NPPrefix;
    public static String SessionID;
    public static String PartnerID;
    TextView txtmobile;
    ImageView myimage;
    String firstLetter = "#";
    String name = "";
    String title = "";
    public static User user;
    public static String paymentmode = "";
    public static String paymentgateway = "";
    public static String paymentmethod = "";
    public static String walletvalue = "0.00";

    public static int walletoff;
    public static int walleton;

    public static String rebateBalance, rewardBalance, walletBalance = "0";

    private static final int REQUEST_READ_PHONE_STATE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent startingIntent = this.getIntent();
        Bundle pushData = startingIntent.getBundleExtra("push");
        if (pushData != null) {
            final AppEventsLogger logger = AppEventsLogger.newLogger(this);
            logger.logPushNotificationOpen(pushData, startingIntent.getAction());
        }

        //remove auto fucos
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //initializing database
        db = new DataBaseHandler(this);
        db.getReadableDatabase();
        mcontext = this;

        walletoff = R.drawable.walletsmalloff;
        walleton = R.drawable.wallet;

        //get IMEI
        checkIMEIPermission();
        GlobalVariables.imei = imei;

        //Get partner ID
        PartnerID = db.getPartnerID(db);
        user = db.getUser();

        //get other info
        Cursor cursor = db.getIsRegistered(db);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                regmobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }


        Bundle b = new Bundle();

        b = getIntent().getExtras();


        if (b != null)
            if (!b.getString("ReturnHero").equalsIgnoreCase("No") || b.getString("ReturnHero") != null) {
                preLoadData();
            }


        //get the profile
        int status = NetworkUtil.getConnectivityStatusString(this);
        if (status == 0) { //no connection
            CheckInternet.showConnectionDialog(this);
        } else { //has connection proceed
            verifySession();
        }

        //initializing objects
        txtmobile = (TextView) findViewById(R.id.mobileno);
        myimage = (ImageView) findViewById(R.id.imageView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        //set initial profile
        initializedNavProfile();

        //Setting default view fragment (was set to topup)
        if (Indicators.NotificationIndicator.equals("true")) { //meaning it receive notification
            displayView(0);
            Indicators.NotificationIndicator = "false";
        } else {
            displayView(1);
        }

    }


    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // set the toolbar title
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onStart() {

        super.onStart();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onBackPressed() {
        logoutDialog.logoutDialogShow(this, MainActivity.this);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        try {
            Fragment fragment = null;
            title = getString(R.string.app_name);
            switch (position) {
                case 0:
                    fragment = new Fragment_Notifications();
                    title = "Notifications";
                    break;
                case 1:

                    fragment = new MainFragment();
                    title = "Home";
                    break;
                case 2:

                    fragment = new FragmentSettings();
                    title = "Settings";
                    break;
//                case 3:
//                    fragment = new FragmentSettings();
//                    title = "Settings";
//                    break;
//                case 1:
//                    fragment = new FragmentTopUp();
//                    title = "Sell Load";
//                    break;
//                case 2:
//                    fragment = new FragmentStockTransfer();
//                    title = "Transfer Credits";
//                    break;
//                case 3:
//                    fragment = new FragmentCredits();
//                    title = "Buy Credits";
//                    break;
//                case 4:
//                    fragment = new FragmentContact();
//                    title = "Contacts";
//                    break;
//                case 5:
//                    fragment = new FragmentTransactions();
//                    title = "Transactions";
//                    break;
//                case 6:
//                    fragment = new FragmentSettings();
//                    title = "Settings";
//                    break;
//                case 7:
//                    title = "Help Center";
                // create an intent
                //Intent i = new Intent(this, com.hipmob.android.HipmobCore.class);
                // REQUIRED: set the appid to the key you're provided
                //i.putExtra(HipmobCore.KEY_APPID, "b7987df80c184656877a8bae7e5e4bfe");
                // REQUIRED: pass the host user identifier.
                //i.putExtra(HipmobCore.KEY_USERID, imei);
                // launch the chat window
                //startActivityForResult(i, 5);
                //break;
                default:
                    break;
            }


            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }
            // set the toolbar title
            getSupportActionBar().setTitle(title);
//            } else {
//                fragment = new HomeFragment();
//                title = "Top-Up";
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container_body, fragment);
//                fragmentTransaction.commit();
//
//                // set the toolbar title
//                getSupportActionBar().setTitle(title);
//
//            }
        } catch (Exception e) {

        }

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
                regmobile = cursor.getString(cursor.getColumnIndex("Mobile"));
            } while (cursor.moveToNext());
        }

        //remove auto fucos
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void verifySession() {
        CreateSession newsession = new CreateSession(this);
        newsession.setQueryListener(new CreateSession.QueryListener() {
            @SuppressWarnings("unchecked")
            public void QuerySuccessFul(String data) {
                String[] rawdata = data.split(";");
                if (rawdata[0].equals("Success")) {
                    SessionID = rawdata[1];
                    // getSignatures(); //get signature sa cloud , e remove na daw ni
                    new fetchProfile().execute(); //get profile
                } else {
                    Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

        });
        newsession.execute(PartnerID);
    }

    public String getIMEI() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } else {
            checkIMEIPermission();
        }

        return null;
    }

    public void checkIMEIPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
            //getting the IMEI
            imei = getIMEI();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkIMEIPermission();
                } else {
                    checkIMEIPermission();
                }
                break;

            default:
                break;
        }
    }


//    @Override
//    public void theMethod() {
//        displayView(5);
//    }

    public void initializedNavProfile() {
        Cursor mycursor = db.getProfile(db);
        if (mycursor.getCount() > 0) {
            mycursor.moveToFirst();
            do {
                name = mycursor.getString(mycursor.getColumnIndex("FirstName"));
                if (!name.equalsIgnoreCase("NONE") && name.length() > 2) {
                    firstLetter = String.valueOf(name.charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.parseColor("#2B60D0"))
                            .endConfig()
                            .buildRound(firstLetter, Color.parseColor("#ffffff")); // radius in px
                    myimage.setImageDrawable(drawable);
                    txtmobile.setText(name);
                } else {
                    txtmobile.setText("0" + regmobile);
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.parseColor("#2B60D0"))
                            .endConfig()
                            .buildRound(firstLetter, Color.parseColor("#ffffff")); // radius in px
                    myimage.setImageDrawable(drawable);
                }
            } while (mycursor.moveToNext());
        } else {
            txtmobile.setText("63" + regmobile);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.parseColor("#2B60D0"))
                    .endConfig()
                    .buildRound(firstLetter, Color.parseColor("#ffffff")); // radius in px
            myimage.setImageDrawable(drawable);
        }

    }

    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************

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

            String myarray = imei + "" + regmobile + "" + Constant.getprofile + "" + SessionID.toLowerCase();
            String authcode = GlobalFunctions.generateSha(myarray);
            String apiURL = PROFILEURL + "&IMEI=" + imei + "&SourceMobTel=" + regmobile + "&SessionNo=" + SessionID + "&AuthCode=" + authcode + "&PartnerID=" + PartnerID + "";

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(apiURL);

            // Log.d("URI", apiURL);

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
                            if (!prevgroupcode.equalsIgnoreCase("ALL")) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(PartnerID + "_" + prevgroupcode);
                            }

                            FirebaseMessaging.getInstance().subscribeToTopic(PartnerID + "_ALL");
                            FirebaseMessaging.getInstance().subscribeToTopic(PartnerID + "_" + imei);
                            FirebaseMessaging.getInstance().subscribeToTopic(PartnerID + "_" + groupcode);
                            FirebaseMessaging.getInstance().subscribeToTopic(PartnerID + "_0" + regmobile);
                            try {
                                Bundle bundle = getIntent().getExtras();
                                for (String key : bundle.keySet()) {
                                    Log.d("myApplication", key + " is a key in the bundle");
                                }
                                //Toast.makeText(this, "Working", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.d("FireMessage", e.getMessage() + " Err");
                            }

                        } else {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(PartnerID + "_ALL");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(PartnerID + "_" + imei);
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(PartnerID + "_" + groupcode);
                            FirebaseMessaging.getInstance().subscribeToTopic(PartnerID + "_0" + regmobile);
                        }

                        //save profile
                        db.saveReturnedHero(db, fname, lname, email, address, mname, gender, interest, occupation, groupcode, dealertype, referrer, beerday, regmobile);

                        user = db.getUser();

                        //setting value to navigation profile
                        initializedNavProfile();

                    } else {
                        Toast.makeText(getBaseContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {

            }
        }


    }

    //endregion

    //region PRELOADEDDATA
    //FUNCTIONS


    public void preLoadData() {


        try {

            Cursor sunc = db.getProduct(db, "SUN");
            Cursor globec = db.getProduct(db, "GLOBE");
            Cursor smartc = db.getProduct(db, "SMART");
            int prefixcount = db.getPrefixCount(db);
            JSONArray prefixarry = new JSONArray(PreloadedData.prefix);
            if (prefixcount != prefixarry.length()) {
                db.DeletePrefix(db);
                db.saveNetPrefix(db, prefixarry);
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();

            JSONArray globearry = new JSONArray(PreloadedData.globestr);
            JSONArray sunarry = new JSONArray(PreloadedData.sunstr);
            JSONArray smartarry = new JSONArray(PreloadedData.smartstr);
            JSONArray productsigarry = new JSONArray(PreloadedData.productsignature);
            JSONArray alloweNumber = new JSONArray(PreloadedData.allowedNumber);


            if (!preferences.contains("AllowedNumbers")) {
                for (int i = 0; i < alloweNumber.length(); i++) {
                    Log.d("Save", "here");
                    db.saveAllowedNumber(db, alloweNumber.getJSONObject(i));
                }
                editor.putString("AllowedNumbers", "Yes");
                editor.apply();
            }


            if (!preferences.contains("IsSmarProductUpdated2")) {
                db.deleteProdCode(db);
                db.saveSunProductCode(db, sunarry, PartnerID);
                db.saveGlobeProductCode(db, globearry, PartnerID);
                db.saveSmartProductCode(db, smartarry, PartnerID);
                for (int i = 0; i < productsigarry.length(); i++) {
                    JSONObject obj = productsigarry.getJSONObject(i);
                    String network = obj.getString("Network");
                    String signature = obj.getString("Signature");
                    db.saveSignature(db, network, signature);
                }
                editor.putString("IsSmarProductUpdated2", "Yes");
                editor.apply();
            } else {


                if (sunc.getCount() == 0 || smartc.getCount() == 0) {

                    db.deleteProdCode(db);
                    db.saveSunProductCode(db, sunarry, PartnerID);
                    //db.saveGlobeProductCode(db, globearry, PartnerID);
                    db.saveSmartProductCode(db, smartarry, PartnerID);
                    for (int i = 0; i < productsigarry.length(); i++) {
                        JSONObject obj = productsigarry.getJSONObject(i);
                        String network = obj.getString("Network");
                        String signature = obj.getString("Signature");
                        db.saveSignature(db, network, signature);
                    }


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //endregion

}