package com.budgetload.materialdesign.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.budgetload.materialdesign.R;

public class SettingsAbout extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        TextView clickableTextLink = (TextView) findViewById(R.id.termscondition);
        clickableTextLink.setOnClickListener(this);

    }

    //region TRIGGERS
//************************
//TRIGGERS
//************************
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.termscondition:
                Uri uriUrl = Uri.parse("http://mybudgetload.com/scr/termcondition.asp");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
                break;
        }

    }


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
