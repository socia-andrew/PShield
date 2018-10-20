package com.budgetload.materialdesign.activity.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.Deactivated;
import com.budgetload.materialdesign.Common.FetchWallet;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.MainActivity;

import static com.budgetload.materialdesign.Common.GlobalVariables.PartnerID;
import static com.budgetload.materialdesign.Common.GlobalVariables.SessionID;

public class WalletFragment extends Fragment {

    private String walletType;
    TextView mWalletLbl, walletHistory, mWallet;
    View view;

    public static WalletFragment newInstance(String type) {
        WalletFragment walletFragment = new WalletFragment();
        walletFragment.walletType = type;
        return walletFragment;
    }


    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWalletLbl = view.findViewById(R.id.textView12);
        walletHistory = view.findViewById(R.id.textView16);
        mWallet = view.findViewById(R.id.textView14);
        populateWallet();

        final CreateSession newsession = new CreateSession(getActivity());
        newsession.setQueryListener(new CreateSession.QueryListener() {
            @SuppressWarnings("unchecked")
            public void QuerySuccessFul(String data) {
                String[] rawdata = data.split(";");
                if (rawdata[0].equals("Success")) {
                    SessionID = rawdata[1];
                    fetchWallet();
                } else {
                    Toast.makeText(getContext(), "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
        newsession.execute(PartnerID);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void populateWallet() {
        if (walletType.equalsIgnoreCase("wallet")) {
            mWalletLbl.setText("Wallet Balance");
            mWallet.setText(MainActivity.walletBalance);
        }

        if (walletType.equalsIgnoreCase("reward")) {
            mWalletLbl.setText("Rewards");
            mWallet.setText(MainActivity.rewardBalance);
            walletHistory.setVisibility(View.INVISIBLE);

        }

        if (walletType.equalsIgnoreCase("rebate")) {
            mWalletLbl.setText("Rebates");
            mWallet.setText(MainActivity.rebateBalance);
            walletHistory.setVisibility(View.INVISIBLE);
        }
    }

    private void fetchWallet() {

        try {
            FetchWallet fetchWallet = new FetchWallet(getActivity());

            fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {

                    String[] rawdata = data.split(";");

                    if (rawdata[0].equals("Success")) {
                        MainActivity.walletBalance = rawdata[1];
                        MainActivity.rewardBalance = rawdata[4];
                        MainActivity.rebateBalance = rawdata[5];
                        populateWallet();
                        Log.d("Balance", "" + MainActivity.walletBalance + "" + MainActivity.rebateBalance + "" + MainActivity.rewardBalance);
                    } else if (rawdata[0].equalsIgnoreCase("Deactivated")) {
                        MainActivity.db.DropAllTable(MainActivity.db);
                        Deactivated.showDeactivatedAccount(getContext(), getActivity());
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch Wallet from the server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }


            });

            fetchWallet.execute(MainActivity.imei, MainActivity.SessionID, MainActivity.PartnerID);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
