package com.budgetload.materialdesign.activity.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.DummyActivity;

public class MainFragment extends Fragment implements View.OnClickListener {

    View view;
    //private ViewPager viewPager;
    //private TabsPagerAdapter mAdapter;
    private String[] tabs = {"Wallet", "Rewards", "Rebates"};
    public static WalletFragment walletFragment;
    public static WalletFragment rewardFragment;
    public static WalletFragment rebateFragment;
    FrameLayout frmWallet, frmReward, frmRebate;

    ImageView imgTopUp; //imageView13;
    ImageView imgTransaction; //imageView14;
    ImageView imgBuyCredits; //imageView15;
    ImageView imgTransfer;//imageView16;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_view, container, false);

        walletFragment = WalletFragment.newInstance("wallet");
        rewardFragment = WalletFragment.newInstance("reward");
        rebateFragment = WalletFragment.newInstance("rebate");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frmWallet = view.findViewById(R.id.frmWallet);
        frmReward = view.findViewById(R.id.frmReward);
        frmRebate = view.findViewById(R.id.frmRebate);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fmWallet = fragmentManager.beginTransaction();
        FragmentTransaction fmReward = fragmentManager.beginTransaction();
        FragmentTransaction fmRebate = fragmentManager.beginTransaction();
        fmWallet.add(R.id.frmWallet, walletFragment);
        fmWallet.commit();

        fmReward.add(R.id.frmReward, rewardFragment);
        fmReward.commit();

        fmRebate.add(R.id.frmRebate, rebateFragment);
        fmRebate.commit();

        imgTopUp = view.findViewById(R.id.imageView13);
        imgTransaction = view.findViewById(R.id.imageView14);
        imgBuyCredits = view.findViewById(R.id.imageView15);
        imgTransfer = view.findViewById(R.id.imageView16);

        imgTopUp.setOnClickListener(this);
        imgTransaction.setOnClickListener(this);
        imgBuyCredits.setOnClickListener(this);
        imgTransfer.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {

        Fragment fragment = null;

        switch (view.getId()) {
            case R.id.imageView13:
                //fragment = new FragmentTopUp();
                Intent intent1 = new Intent(getContext(), DummyActivity.class);
                intent1.putExtra("fragment", "topup");
                startActivity(intent1);
                break;
            case R.id.imageView14:
                //fragment = new FragmentTransactions();
                Intent intent2 = new Intent(getContext(), DummyActivity.class);
                intent2.putExtra("fragment", "trxn");
                startActivity(intent2);
                break;
            case R.id.imageView15:
                //fragment = new FragmentCredits();
                Intent intent3 = new Intent(getContext(), DummyActivity.class);
                intent3.putExtra("fragment", "buy");
                startActivity(intent3);
                break;
            case R.id.imageView16:
                Intent intent4 = new Intent(getContext(), DummyActivity.class);
                intent4.putExtra("fragment", "transfer");
                startActivity(intent4);
                //fragment = new FragmentStockTransfer();
                break;
        }


    }




}
