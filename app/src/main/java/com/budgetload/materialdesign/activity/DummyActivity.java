package com.budgetload.materialdesign.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.Fragments.FragmentCredits;
import com.budgetload.materialdesign.activity.Fragments.FragmentStockTransfer;
import com.budgetload.materialdesign.activity.Fragments.FragmentTopUp;
import com.budgetload.materialdesign.activity.Fragments.FragmentTransactions;

public class DummyActivity extends AppCompatActivity {

    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        frameLayout = (FrameLayout) findViewById(R.id.dummyFrame);

        Bundle bundle = getIntent().getExtras();

        FragmentManager dummyFm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction dummyFt = dummyFm.beginTransaction();
        Fragment fragment = null;
        if(bundle.getString("fragment").equalsIgnoreCase("topup")){
            dummyFt.replace(R.id.dummyFrame,new FragmentTopUp()).commit();
        }
        if(bundle.getString("fragment").equalsIgnoreCase("trxn")){
            dummyFt.replace(R.id.dummyFrame,new FragmentTransactions()).commit();
        }
        if(bundle.getString("fragment").equalsIgnoreCase("buy")){
            dummyFt.replace(R.id.dummyFrame,new FragmentCredits()).commit();
        }
        if(bundle.getString("fragment").equalsIgnoreCase("transfer")){
            dummyFt.replace(R.id.dummyFrame,new FragmentStockTransfer()).commit();
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

}
