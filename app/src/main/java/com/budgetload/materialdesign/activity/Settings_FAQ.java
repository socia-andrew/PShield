package com.budgetload.materialdesign.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.adapter.MyExpandableAdapter;

import java.util.ArrayList;

public class Settings_FAQ extends AppCompatActivity {

    ExpandableListView expandableList;
    MyExpandableAdapter myadapter;
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__faq);
        ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.list);
//        getExpandableListView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        setGroupParents();
        setChildData();


        myadapter = new MyExpandableAdapter(parentItems, childItems);

        myadapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        expandableList.setAdapter(myadapter);
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });


    }


    //region FUNCTIONS
//************************
//FUNCTIONS
//************************
    public void setGroupParents() {
        parentItems.add("What is BudgetLoad?");
        parentItems.add("How to use budgetload?");
        parentItems.add("​What​ is the use of email address during registration?");
        parentItems.add("Where to purchase Load Credit for the Wallet?");
        parentItems.add("How to topup?");
        parentItems.add("What are the Mobile Networks available in BudgetLoad?");
        parentItems.add("How to recover account when lost phone?");
        parentItems.add("How to transfer wallet to other number?");
        parentItems.add("How to deactivate account?");
        parentItems.add("How to keep track of the Purchases of Load Credits? ");
        parentItems.add("How to know if the Top-Up transaction is successful or not?");
        parentItems.add("How to view older transactions?");


    }


    public void setChildData() {

        // 1
        ArrayList<String> child = new ArrayList<String>();
        child.add("BudgetLoad is a quick, simple and secure airtime reloading for mobile network like Globe, Smart and Sun.");
        childItems.add(child);
        // 2
        child = new ArrayList<String>();
        child.add("User can download the BudgetLoad App in Google App Store. After downloading, open the BudgetLoad Application. The first time that the User uses​​ this App, it will ask for a registration of the mobile number. By registering the mobile number, a verification code will be sent via text. User must type the correct verification code in order to start using the BudgetLoad. User must take note that an Internet Connection is a must in order to use the BudgetLoad Application.");
        childItems.add(child);

        child = new ArrayList<String>();
        child.add("By verifying the email address, the user can retrieve its account when phone is lost. The user can deactivate its account. The user can also view older transactions from its credit purchases and Top-Ups. ");
        childItems.add(child);

        //4

        child = new ArrayList<String>();
        child.add("User can purchase load credit from any Remitbox Outlet (HLHUILLIER, PRIMEASIA, GEMMARY), Gaisano Capital, GMart Stores..");
        childItems.add(child);

        //5

        child = new ArrayList<String>();
        child.add("To Top-Up, User must input the correct mobile number, then choose a Load Type, either Regular or Special. If Regular, User must input the amount to be loaded. If Special, User will choose from the list of Special Promo of the Network. Then press the Top-Up button.");
        childItems.add(child);

        //6
        child = new ArrayList<String>();
        child.add("The Mobile Networks available are GLOBE, SMART and SUN.");
        childItems.add(child);

        //7
        child = new ArrayList<String>();
        child.add("Go to Settings and click support or visit www.budgetload.com and click LOST PHONE menu. Click submit button, email verification for account recovery will be sent to your email address.");
        childItems.add(child);

        //8
        child = new ArrayList<String>();
        child.add("Go to Stock Transfer menu and input the target number and amount to be transfered.");
        childItems.add(child);

        //9
        child = new ArrayList<String>();
        child.add("Go to Settings and click DEACTIVATE. Account cannot be deactivated if it still have available credits.");
        childItems.add(child);

        //10
        child = new ArrayList<String>();
        child.add("To keep track of the purchase of Credits, User can go to Transactions and press the Purchase tab. This is where all the purchases of the User is found. 10 transactions will only be displayed.");
        childItems.add(child);
        //11
        child = new ArrayList<String>();
        child.add("To know if the transaction is successful or not, go to Transactions and press the Top-Up Transaction tab. If the transaction is in the list of Top-Ups, then it is ​either ​successful​ or failed​ but if it's not in the list, then the transaction is ​​[either failed or​]​ still ​​​on ​process.​ 10​​ transactions will only be displayed​​.​");
        childItems.add(child);


        child = new ArrayList<String>();
        child.add("User can go to Transactions and press either Top-Up or Purchase tab. Click View More text found at the bottom of the list. Then an email containing a redirection link will be sent to the User's email address. By clicking the link, it will redirect to the report view of the transactions. This is only applicable for Users with verified email address.");
        childItems.add(child);
    }

//endregion

//region THREADS
//************************
//THREADS
//************************

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



}
