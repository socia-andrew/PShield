package com.budgetload.materialdesign.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.budgetload.materialdesign.R;

public class TopUpError extends AppCompatActivity {


    String brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_error);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Top-Up Confirmation");


        Bundle b = getIntent().getExtras();
        brand = b.getString("Brand").toString();

        TextView message = (TextView) findViewById(R.id.message);
        message.setText(brand + " NETWORK IS CURRENTLY OFFLINE");

        Button btnOkay = (Button) findViewById(R.id.okey);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

}
