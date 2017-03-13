package com.budgetload.materialdesign.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.FetchWallet;
import com.budgetload.materialdesign.Common.HideKeyboard;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;


public class HomeFragment extends Fragment implements View.OnClickListener {


    TextView txtLoad;
    ImageView mywalleticon;
    TextView txtMyWallet;

    String SessionID;
    String imei;

    DataBaseHandler db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        HideKeyboard.hideKeyboard(getActivity());

        db = new DataBaseHandler(getActivity());

        imei = getIMEI();

        txtLoad = (TextView) rootView.findViewById(R.id.txtLoad);
        txtLoad.setOnClickListener(this);

        txtMyWallet = (TextView) rootView.findViewById(R.id.txtMyWallet);
        mywalleticon = (ImageView) rootView.findViewById(R.id.walleticon);
        mywalleticon.setOnClickListener(this);

        Cursor crWallet = db.getWallet(db);

        if (crWallet.getCount() > 0) {
            crWallet.moveToFirst();
            do {
                txtMyWallet.setText("" + crWallet.getString(crWallet.getColumnIndex("Balance")) + "");
            } while (crWallet.moveToNext());
        } else {
            txtMyWallet.setText(" 0.00 ");
        }

        return rootView;
    }


    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtLoad:

                txtLoad.setEnabled(false);
                Intent intent = new Intent(getActivity(), TopUpLoad.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.walleticon:

                int constatus = NetworkUtil.getConnectivityStatusString(getActivity());


                if (constatus == 0) {
                    CheckInternet.showConnectionDialog(getActivity());
                } else {

                    mywalleticon.setEnabled(false);

                    CreateSession newsession = new CreateSession(getActivity());

                    newsession.setQueryListener(new CreateSession.QueryListener() {
                        @SuppressWarnings("unchecked")
                        public void QuerySuccessFul(String data) {


                            String[] rawdata = data.split(";");
                            if (rawdata[0].equals("Success")) {
                                SessionID = rawdata[1];
                                wallet();
                            } else {
                                Toast.makeText(getActivity(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                            }

                        }

                    });


                    newsession.execute();


                }

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        txtLoad.setEnabled(true);
        HideKeyboard.hideKeyboard(getActivity());

        if (requestCode == 1) {

            if (resultCode == 2) {
                callTransaction.theMethod();
            }
        }

    }


    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    private GoToTransactions callTransaction;

    public void onAttach(Activity activity) {
        callTransaction = (GoToTransactions) activity;
        super.onAttach(activity);
    }

    public interface GoToTransactions {
        void theMethod();
    }

    public String getIMEI() {

        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getDeviceId();

    }

    public void wallet() {

        FetchWallet fetchWallet = new FetchWallet(getActivity());

        fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

            @SuppressWarnings("unchecked")
            public void QuerySuccessFul(String data) {

                String[] rawdata = data.split(";");

                if (rawdata[0].equals("Success")) {
                    txtMyWallet.setText("P " + rawdata[1] + "");
                    mywalleticon.setEnabled(true);
                    progressDialog.hideDialog();
                } else if (rawdata[0].equals("Deactivated")) {
                   // db.DropAllTable(db);
                    // Intent intent = new Intent(HomeFragment.this, Registration_Spash.this);
                    // Deactivated.showDeactivatedAccount(getActivity(), HomeFragment.class);
                } else {
                    mywalleticon.setEnabled(true);
                    Toast.makeText(getActivity(), "Failed to fetch Wallet from the server. Please try again.", Toast.LENGTH_LONG).show();
                    progressDialog.hideDialog();
                }
            }


        });

        progressDialog.showDialog(getActivity(), "BudgetLoad", "Fetching Wallet... please wait", false);
        fetchWallet.execute(imei, SessionID);

    }

    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    //endregion

}
