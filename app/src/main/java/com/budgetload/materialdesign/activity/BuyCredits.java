package com.budgetload.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.ArrayList.PaymentModel;
import com.budgetload.materialdesign.Common.Deactivated;
import com.budgetload.materialdesign.Common.FetchWallet;
import com.budgetload.materialdesign.Common.RequestCredits;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.Constant.Indicators;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.adapter.PaymentMethodAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.budgetload.materialdesign.R.id.txtMyWallet;
import static com.budgetload.materialdesign.activity.GlobalVariables.PartnerID;
import static com.budgetload.materialdesign.activity.GlobalVariables.SessionID;
import static com.budgetload.materialdesign.activity.GlobalVariables.imei;

public class BuyCredits extends Fragment implements Constant {


    //region INITIALIZATION
    Context mcontext;
    View rootView;
    PaymentMethodAdapter adapter;
    PaymentMethodAdapter.OnItemClickListener onItemClickListener;
    JSONArray jsonArray;
    public static ArrayList<PaymentModel> List = new ArrayList<>();
    RecyclerView mrecyclerview;
    LinearLayoutManager mLinearLayoutManager;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    TextView mywallet;
    ImageView imgrequestcredits;
    String gateway, methodID, modeid;
    ImageView imgWallet;
    DataBaseHandler db;
    //endregion

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_buy_credits, container, false);
        mcontext = getActivity();


        mrecyclerview = (RecyclerView) rootView.findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mLinearLayoutManager = new LinearLayoutManager(mcontext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mywallet = (TextView) rootView.findViewById(txtMyWallet);
        mywallet.setText(MainActivity.walletvalue);
        imgrequestcredits = (ImageView) rootView.findViewById(R.id.requestcredits);
        imgWallet = (ImageView) rootView.findViewById(R.id.walleticon);


        db = new DataBaseHandler(mcontext);

        onItemClickListener = new PaymentMethodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                gateway = ((TextView) v
                        .findViewById(R.id.txtgateway))
                        .getText().toString();
                methodID = ((TextView) v.findViewById(R.id.txtmethod)).getText().toString();
                modeid = ((TextView) v.findViewById(R.id.txtmode)).getText().toString();
                String payrates = ((TextView) v.findViewById(R.id.txtpaymentrate)).getText().toString();

                MainActivity.paymentmode = modeid;
                MainActivity.paymentgateway = gateway;
                MainActivity.paymentmethod = methodID;


                Intent intent = new Intent(mcontext, creditsAmount.class);
                intent.putExtra("Gateway", gateway);
                intent.putExtra("Method", methodID);
                intent.putExtra("Rate", payrates);
                startActivity(intent);

                //Toast.makeText(getContext(), gateway, Toast.LENGTH_SHORT).show();

//                if (gateway.equalsIgnoreCase("remittance")) {
//                    Intent intent = new Intent(mcontext, GatewayPayment.class);
//                    intent.putExtra("Gateway", gateway);
//                    intent.putExtra("Method", methodID);
//                    startActivity(intent);
//                } else if (StringUtils.containsIgnoreCase
//                        (gateway, "bank")) {
//                    Intent intent = new Intent(mcontext, creditsAmount.class);
//                    intent.putExtra("Gateway", gateway);
//                    intent.putExtra("Method", methodID);
//                    startActivity(intent);
//
//                } else {//(gateway.equalsIgnoreCase("7-Eleven")) {
//                    Intent intent = new Intent(mcontext, creditsAmount.class);
//                    intent.putExtra("Gateway", gateway);
//                    MainActivity.paymentgateway = "remittance";
//                    intent.putExtra("Method", methodID);
//                    startActivity(intent);
//                }
            }

        }

        ;

        imgrequestcredits.setOnClickListener(new View.OnClickListener()

                                             {
                                                 @Override
                                                 public void onClick(View v) {
                                                     v.setEnabled(false);
                                                     RequestCredits.showRequestDialog(mcontext, getActivity(), PartnerID, MainActivity.user.mobile);
                                                     v.setEnabled(true);
                                                 }
                                             }

        );

        imgWallet.setOnClickListener(new View.OnClickListener()

                                     {

                                         @Override
                                         public void onClick(View v) {
                                             wallet();
                                         }
                                     }

        );

        display();


        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    //region TRIGGERS


    //endregion

    //region FUNCTIONS

    public void wallet() {

        progressDialog.showDialog(mcontext, "BudgetLoad", "Fetching Wallet... please wait", false);

        try {
            FetchWallet fetchWallet = new FetchWallet(getActivity());
            fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {

                    String[] rawdata = data.split(";");

                    if (rawdata[0].equals("Success")) {
                        if (mywallet != null) {
                            try {
                                mywallet.setText("" + rawdata[1] + "");
                                imgWallet.setEnabled(true);
                                walleticonstat(0);
                                progressDialog.hideDialog();
                                Indicators.WalletFetchIndicator = "false";
                            } catch (Exception e) {
                                progressDialog.hideDialog();
                            }

                        }
                    } else if (rawdata[0].equalsIgnoreCase("Deactivated")) {
                        db.DropAllTable(db);
                        Deactivated.showDeactivatedAccount(mcontext, getActivity());
                    } else {
                        imgWallet.setEnabled(true);
                        walleticonstat(0);
                        progressDialog.hideDialog();
                        Toast.makeText(mcontext, "Failed to fetch Wallet from the server. Please try again.", Toast.LENGTH_LONG).show();
                    }

                }


            });


            fetchWallet.execute(imei, SessionID, PartnerID);
        } catch (Exception e) {
            progressDialog.hideDialog();
        }


    }

    public void walleticonstat(Integer val) {
        if (val.equals(1)) {
            imgWallet.setBackgroundResource(MainActivity.walletoff);
        } else {
            imgWallet.setBackgroundResource(MainActivity.walleton);
        }
    }

    public void display() {


        List.clear();


        try {
            jsonArray = new JSONArray(Constant.gateway);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                PaymentModel paymentModel = new PaymentModel(jsonObject.getString("Gateway"), jsonObject.getString("Mode"),
                        jsonObject.getInt("Photo"), jsonObject.getString("method"), jsonObject.getInt("Rate"), jsonObject.getString("Delivery"));
                List.add(paymentModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new PaymentMethodAdapter(mcontext, List);

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
