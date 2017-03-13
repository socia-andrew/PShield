package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.GlobalFunctions;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Community extends AppCompatActivity implements Constant {


    //declaration here
    String selectedCommID = "";
    String selectedCommName = "";
    EditText spinCommunity;
    String PartnerID;
    String SessionID;
    DataBaseHandler db;
    Button btnConfirm;
    String imei;
    String mobile;
    Context mcontext;
    TextView txtCommunity;
    JSONArray commlist;
    private loadCommunityImage mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mcontext = this;
        db = new DataBaseHandler(this); //initializing database

        //getting the IMEI
        imei = getIMEI();
        GlobalVariables.imei = imei;
        //fetch community
        new fetchCommunity().execute();

        //initializing objects
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        txtCommunity = (TextView) findViewById(R.id.txtCommunity);
        spinCommunity = (EditText) findViewById(R.id.edittxtCommunity);

        //clicking confirm button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnConfirm.setEnabled(false); //diabling button

                if (selectedCommID.isEmpty()) {
                    Toast.makeText(mcontext, "Please select a Community", Toast.LENGTH_LONG).show();
                    btnConfirm.setEnabled(true);
                } else {

                    db.savePartnerID(db, selectedCommID, selectedCommName); //save Selected Community

                    //Saving Community Logo
                    if (!selectedCommID.equalsIgnoreCase("remitbox")) {
                        mTask = new loadCommunityImage();
                        new loadCommunityImage().execute();
                        mTask.execute();
                    }

                    Bundle b = new Bundle();
                    b.putString("CommunityID", selectedCommID);
                    b.putString("CommunityName", selectedCommName);
                    GlobalFunctions.fbLogger(Community.this, "Selecting A Community", b,1);

                    //proceed to registering mobile(open new activity)
                    Intent iinent = new Intent(mcontext, Register.class);
                    startActivity(iinent);
                    iinent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    btnConfirm.setEnabled(true);
                    ActivityCompat.finishAffinity(Community.this);
                }
            }
        });


        //Selecting community (opening community list)
        spinCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCommunity.setEnabled(false);

                if (commlist != null) {
                    Intent intent = new Intent(mcontext, CommunityList.class);
                    Bundle b = new Bundle();
                    b.putString("Community", commlist.toString());
                    intent.putExtras(b);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(mcontext, "Community List failed to load. Please close the app and open again.", Toast.LENGTH_LONG).show();
                    btnConfirm.setEnabled(true);
                }


            }


        });


    }


    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //call back after sellecting community
        txtCommunity.setEnabled(true);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                selectedCommID = data.getStringExtra("ComID");
                selectedCommName = data.getStringExtra("ComName");
                PartnerID = data.getStringExtra("ComID");
                spinCommunity.setText(data.getStringExtra("ComName"));
            }
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


    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************

    class fetchCommunity extends AsyncTask<Void, Void, String> {
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
            progressDialog.showDialog(mcontext, "BudgetLoad", "Fetching Community... please wait", false);
        }

        @Override
        protected String doInBackground(Void... params) {

            String text;
            String apiURL;
            try {

                apiURL = OTHERSURL + "&CMD=COMMUNITY&IMEI=" + imei;
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
                text = null;
                Log.d("Exception", e.toString());
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hideDialog();
            txtCommunity.setEnabled(true);
            if (s == null) {
                Toast.makeText(getBaseContext(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            } else {
                try {


                    if (commlist == null) commlist = null;

                    JSONArray json = new JSONArray(s);
                    JSONObject prefixarr = json.getJSONObject(0);
                    JSONObject articles = prefixarr.getJSONObject("Community");
                    String sessionresult = articles.getString("Result");
                    if (sessionresult.equals("OK")) {
                        JSONObject obj = json.getJSONObject(1);
                        commlist = obj.getJSONArray("Community");
                    } else {
                        Toast.makeText(getBaseContext(), " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                }
            }
        }

    }

    public class loadCommunityImage extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);

            if (result != null) {
                Bitmap newimage = Bitmap.createScaledBitmap(result,
                        (int) (result.getWidth() * 1),
                        (int) (result.getHeight() * 1), true);

                storeImage(newimage);
            }

        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            String imageUrl = "http://s3.amazonaws.com/budgetload/" + selectedCommID.toLowerCase() + ".png";
            Bitmap myBitmap = null;

            try {
                InputStream in = new java.net.URL(imageUrl).openStream();
                myBitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                myBitmap = null;
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return myBitmap;
        }

    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("URI",
                    "Error creating image file, please check: ");// e.getMessage());
            return;
        }
        try {

            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("URI", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("URI", "Error accessing file: " + e.getMessage());
        }
    }

    /**
     * Create a File for saving an image
     */
    private File getOutputMediaFile() {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files/Community");


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName = selectedCommID.toLowerCase() + ".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    //endregion


}
