package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;

public class Notification_Details extends AppCompatActivity {

    TextView title;
    TextView message;
    TextView sender;
    TextView txtdate;
    ImageView imageView;
    TextView txtid;
    DataBaseHandler db;
    Button bTransfer;
    String mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        String notifid = b.getString("NotificationID").toString();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DataBaseHandler(this);


        title = (TextView) findViewById(R.id.txttitle);
        message = (TextView) findViewById(R.id.txtmessage);
        sender = (TextView) findViewById(R.id.sender);
        txtdate = (TextView) findViewById(R.id.txtdate);
        imageView = (ImageView) findViewById(R.id.image_view);
        txtid = (TextView) findViewById(R.id.txtid);
        bTransfer = (Button) findViewById(R.id.bTransfer);

        Cursor cursor = db.getNotificationDetails(db, notifid);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                title.setText(cursor.getString(cursor.getColumnIndex("Title")));
                message.setText(cursor.getString(cursor.getColumnIndex("Message")));

                txtdate.setText(cursor.getString(cursor.getColumnIndex("DateSent")));
                txtid.setText(notifid);
                mobile = cursor.getString(cursor.getColumnIndex("Extra1"));

                String firstLetter = "B";
                TextDrawable drawable;

//            String firstLetter = String.valueOf(sender.getText().toString().charAt(0));
//            TextDrawable drawable = TextDrawable.builder()
//                    .buildRound(firstLetter, Color.parseColor("#2B60D0")); // radius in px
//            imageView.setImageDrawable(drawable);

                if (cursor.getString(cursor.getColumnIndex("Sender")).length() > 0) {
                    firstLetter = String.valueOf(cursor.getString(cursor.getColumnIndex("Sender")).toString().charAt(0));
                    drawable = TextDrawable.builder()
                            .buildRound(firstLetter, Color.parseColor("#2B60D0")); // radius in px
                    sender.setText(cursor.getString(cursor.getColumnIndex("Sender")));
                } else {
                    drawable = TextDrawable.builder()
                            .buildRound("B", Color.parseColor("#2B60D0"));
                    sender.setText(mobile);
                }

                imageView.setImageDrawable(drawable);

            }




        }

        if (title.getText().toString().equalsIgnoreCase("Request Credits")) {
            bTransfer.setVisibility(View.VISIBLE);
        }

        bTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("mobile", mobile);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });

    }


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

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************


    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    //endregion


}
