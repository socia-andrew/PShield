package com.budgetload.materialdesign.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class Registration_Spash extends AppCompatActivity implements View.OnClickListener {

    //declaration here

    DataBaseHandler db;
    Button btnreg;
    TextView txtlabel;
    TextView txtterms;
    String regcommunity;
    ImageView imgCommunity;
    RelativeLayout bgLayout;
    Context mcontext;
    ImageView poweredby;
    String regstatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_splash);

        mcontext = this;
        db = new DataBaseHandler(mcontext); //initializing database

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        regstatus = db.getRegisteredStatus(db);
        regcommunity = db.getPartnerID(db);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MainActivity", "Key: " + key + " Value: " + value);
            }
        }

        //intializing objects
        btnreg = (Button) findViewById(R.id.button1);
        txtlabel = (TextView) findViewById(R.id.txtlabel);
        txtterms = (TextView) findViewById(R.id.txtTermsAndCondition);
        imgCommunity = (ImageView) findViewById(R.id.imageView1);
        bgLayout = (RelativeLayout) findViewById(R.id.wrapsplash);

        poweredby = (ImageView) findViewById(R.id.imageView12);

        //clicking terms ang conditions
        txtterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.mybudgetload.com/scr/termcondition.asp";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        //clicking on register button
        btnreg.setOnClickListener(this);


//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    android.Manifest.permission.READ_PHONE_STATE)) {
//                Toast.makeText(getBaseContext(),"he",Toast.LENGTH_SHORT).show();
//            }else{
//                ActivityCompat.requestPermissions(this,
//                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
//                        1);
//                    Toast.makeText(getBaseContext(),"he",Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            this.finish();
//        }


    }


    //region APPTRIGGERS
    //*********************
    //TRIGGERS
    //*********************

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    showPermissionDialog();
                }
                return;
            }
        }
    }

    public void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("Unable to start application. Please go to Settings -> Apps/Manage Application -> " +
                "BudgetLoad Community -> Permissions, then enable requested permisions for security purposes. Thank you!");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Registration_Spash.this.finish();
            }
        });
        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();


//        if (!regcommunity.equals("") && !regcommunity.equalsIgnoreCase("remitbox")) {
//
//            File imgFile = new File(Environment.getExternalStorageDirectory()
//                    + "/Android/data/"
//                    + getApplicationContext().getPackageName()
//                    + "/Files/Community/" + regcommunity.toLowerCase() + ".png");
//
//            if (imgFile.exists()) {
//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//                imgCommunity.setImageBitmap(myBitmap);
//                bgLayout.setBackgroundColor(Color.WHITE);
//                txtterms.setTextColor(Color.parseColor("#2B60D0"));
//                poweredby.setVisibility(View.VISIBLE);
//
//            } else {
//
//                String uri = "@drawable/" + regcommunity.toLowerCase();
//                int drawableCommunity = getResources().getIdentifier(uri, null, getPackageName());
//                imgCommunity.setImageResource(drawableCommunity);
//                bgLayout.setBackgroundColor(Color.WHITE);
//                txtterms.setTextColor(Color.parseColor("#2B60D0"));
//                poweredby.setVisibility(View.VISIBLE);
//
//            }
//
//        } else {
//
//            poweredby.setVisibility(View.GONE);
//        }

        checkStatus();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }


    //endregion


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button1:
                Intent iinent = new Intent(this, Community.class);
                startActivity(iinent);
                iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ActivityCompat.finishAffinity(this);
                break;
        }

    }


//region THREADS
//************************
//THREADS
//************************


//endregion


//region FUNCTIONS
//************************
//FUNCTIONS
//************************

    public void checkStatus() {

        //checking where the status ends
        if (regstatus == "" || regstatus == null || regstatus.isEmpty()) { //first open sa app
            int secondsDelayed = 1;

            new Handler().postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                public void run() {
                    btnreg.setVisibility(View.VISIBLE);
                    btnreg.setAlpha(0.1f);
                    btnreg.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(1000);
                    txtlabel.setVisibility(View.VISIBLE);
                    txtlabel.setAlpha(0.1f);
                    txtlabel.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(1000);
                    txtterms.setVisibility(View.VISIBLE);
                    txtterms.setAlpha(0.1f);
                    txtterms.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(1000);
                    poweredby.setVisibility(View.GONE);


                }
            }, secondsDelayed * 500);


        } else if (regstatus.equalsIgnoreCase("ForVerification")) { //registration ends at verification code


            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                public void run() {

                    Intent iinent = new Intent(Registration_Spash.this, Verify.class);
                    startActivity(iinent);
                    iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ActivityCompat.finishAffinity(Registration_Spash.this);

                }
            }, secondsDelayed * 500);


        } else if (regstatus.equalsIgnoreCase("Profiling")) { //ends at profiling


            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                public void run() {

                    Bundle b = new Bundle();
                    b.putString("ReturnHero", "No");

                    Intent iinent = new Intent(Registration_Spash.this, Profile.class);
                    iinent.putExtras(b);
                    startActivity(iinent);
                    iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ActivityCompat.finishAffinity(Registration_Spash.this);

                }
            }, secondsDelayed * 500);


        } else { //meaning has been already register, proceed to main page


            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                public void run() {

                    Bundle b = new Bundle();
                    b.putString("ReturnHero", "No");

                    Intent iinent = new Intent(mcontext, MainActivity.class);
                    iinent.putExtras(b);
                    startActivity(iinent);

                    iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ActivityCompat.finishAffinity(Registration_Spash.this);

                }
            }, secondsDelayed * 2000);


        }

    }

//endregion


}