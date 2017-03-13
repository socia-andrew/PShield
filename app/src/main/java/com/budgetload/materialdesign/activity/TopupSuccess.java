package com.budgetload.materialdesign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.budgetload.materialdesign.R;

import java.text.DecimalFormat;

public class TopupSuccess extends AppCompatActivity {

    String brand;
    String tartgetmobile;
    String producttype;
    String productamount;

    DecimalFormat dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Top-Up Confirmation");

        dec = new DecimalFormat("#,###,###.####");
        dec.setMinimumFractionDigits(2);


        Bundle b = getIntent().getExtras();
        brand = b.getString("Brand").toString();
        tartgetmobile = b.getString("TargetMobile").toString();
        producttype = b.getString("ProductType").toString();
        productamount = b.getString("ProductAmount").toString();


        TextView telco = (TextView) findViewById(R.id.telco);
        TextView amountval = (TextView) findViewById(R.id.amountval);
        TextView mobileno = (TextView) findViewById(R.id.mobileno);

        telco.setText(brand + " - " + producttype);
        amountval.setText("P " + dec.format(Double.parseDouble(productamount)) + "");
        mobileno.setText(tartgetmobile);

        Button topupagain = (Button) findViewById(R.id.topupagain);
        topupagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        Button gotransaction = (Button) findViewById(R.id.gotransaction);
        gotransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "goTransaction");
                setResult(4, returnIntent);
                finish();
            }
        });


    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************


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
