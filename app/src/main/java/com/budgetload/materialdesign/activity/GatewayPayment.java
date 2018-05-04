package com.budgetload.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.budgetload.materialdesign.model.PaymentModel;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.adapter.OutletAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GatewayPayment extends AppCompatActivity implements Constant {

    Context mcontext;
    View rootView;
    String method;

    JSONArray jsonArray;
    public static ArrayList<PaymentModel> List = new ArrayList<>();

    OutletAdapter adapter;
    OutletAdapter.OnItemClickListener onItemClickListener;
    RecyclerView mrecyclerview;

    LinearLayoutManager mLinearLayoutManager;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    String Outlet, modeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_payment);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mcontext = this;

        Intent intent = getIntent();
        method = intent.getStringExtra("Method");


        mrecyclerview = (RecyclerView) findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLinearLayoutManager = new LinearLayoutManager(mcontext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        onItemClickListener = new OutletAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Outlet = ((TextView) v
                        .findViewById(R.id.txtgateway))
                        .getText().toString();
                modeid = ((TextView) v
                        .findViewById(R.id.txtmodeid))
                        .getText().toString();

                MainActivity.paymentmode = modeid;

                Intent intent = new Intent(mcontext, creditsAmount.class);

                Bundle b = new Bundle();
                b.putString("Outlet", Outlet);
                //  b.putString("Mode", modeid);
                b.putString("Gateway", method);
                intent.putExtras(b);
                startActivity(intent);
                v.setEnabled(true);
                finish();
            }
        };


        displayGate();


    }

    //region TRIGGERS

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    //endregion

    //region FUNCTIONS

    public void displayGate() {

        List.clear();

        switch (method) {

            case "onlinebank":

                try {
                    jsonArray = new JSONArray(Constant.onlinebank);
                } catch (JSONException e) {
                }

                break;

//            case "remittance":
//
//                try {
//                    jsonArray = new JSONArray(Constant.remittance);
//                } catch (JSONException e) {
//                }
//
//                break;

        }


        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                PaymentModel paymentModel = new PaymentModel(jsonObject.getString("Outlet"), jsonObject.getString("Mode"), 0, "none", 0, "none");
                List.add(paymentModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new OutletAdapter(mcontext, List);

        mrecyclerview.setAdapter(adapter);
        mrecyclerview.setLayoutManager(mLinearLayoutManager);
        mrecyclerview.setLayoutManager(mStaggeredLayoutManager);

        adapter.setOnItemClickListener(onItemClickListener);


    }

    //endregion

    //region THREADS

    //endregion

    //region INTERFACE

    //endregion
}
