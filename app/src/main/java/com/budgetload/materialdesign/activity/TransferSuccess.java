package com.budgetload.materialdesign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.budgetload.materialdesign.Constant.Indicators;
import com.budgetload.materialdesign.R;

import java.text.DecimalFormat;

public class TransferSuccess extends AppCompatActivity {

    String Amount;
    String tartgetmobile;

    DecimalFormat dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Indicators.SuccessTransferIndicator = "true";

        toolbar.setTitle("Transfer Confirmation");

        Bundle b = getIntent().getExtras();
        Amount = b.getString("Amount").toString();
        tartgetmobile = b.getString("TargetMobile").toString();


        TextView amountval = (TextView) findViewById(R.id.amountval);
        TextView mobileno = (TextView) findViewById(R.id.mobileno);

        amountval.setText(Amount);
        mobileno.setText(tartgetmobile);


        //Stock Transfer Again AGAIN BUTTON
        Button stocktransferagain = (Button) findViewById(R.id.stocktransferagain);
        stocktransferagain.setOnClickListener(new View.OnClickListener() {
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
                setResult(3, returnIntent);
                finish();
            }
        });

    }

}
