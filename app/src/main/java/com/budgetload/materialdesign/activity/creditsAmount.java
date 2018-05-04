package com.budgetload.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.Deactivated;
import com.budgetload.materialdesign.Common.FetchWallet;
import com.budgetload.materialdesign.Common.RequestCredits;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.Constant.Indicators;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;

import java.text.DecimalFormat;

import static com.budgetload.materialdesign.Common.GlobalVariables.PartnerID;
import static com.budgetload.materialdesign.Common.GlobalVariables.SessionID;
import static com.budgetload.materialdesign.Common.GlobalVariables.imei;


public class creditsAmount extends AppCompatActivity {

    Context mcontext;
    String outletname;
    String gateway;
    ImageView imgrequestcredits;
    ImageView imgWallet;
    TextView txtPaymentMethod, txtFee, mywallet;
    EditText txtPurchaseAmount;
    DecimalFormat formatter = new DecimalFormat("#,###,###.00");
    DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_amount);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mcontext = this;
        db = new DataBaseHandler(mcontext);
        mywallet = (TextView) findViewById(R.id.txtMyWallet);
        mywallet.setText(MainActivity.walletvalue);
        imgrequestcredits = (ImageView) findViewById(R.id.requestcredits);
        imgWallet = (ImageView) findViewById(R.id.walleticon);

        txtPaymentMethod = (TextView) findViewById(R.id.txtpm);
        txtPurchaseAmount = (EditText) findViewById(R.id.txtPurchase);

        Bundle bundle = getIntent().getExtras();
        gateway = bundle.getString("Gateway");
        //outletname = bundle.getString("Outlet");


        txtFee = (TextView) findViewById(R.id.txtFee);
        txtFee.setText(bundle.getString("Rate"));
        txtPaymentMethod.setText(gateway);

//        if (gateway.equalsIgnoreCase("onlinebank")) {
//            gateway = "Online Banking";
//        }
//        //gateway + " - " +
//        //txtPaymentMethod.setText(gateway);
//        if (gateway.equalsIgnoreCase("remittance")) txtPaymentMethod.setText(outletname);
//        else txtPaymentMethod.setText(gateway);
//        //region Inline Triggers

        //txtPurchaseAmount.addTextChangedListener(new NumberTextWatcher(txtPurchaseAmount));

//        txtPurchaseAmount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            private String current = "";
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    if (Integer.parseInt(txtPurchaseAmount.getText().toString()) != 0) {
//                        switch (MainActivity.paymentType) {
//                            case "OnlineBanking":
//                                txtFee.setText("10.00");
//                                break;
//                            case "OTCBanking":
//                                txtFee.setText("15.00");
//                                break;
//                            case "OTCNonbank":
//                                txtFee.setText("20.00");
//                                break;
//                            default:
//                                txtFee.setText("20.00");
//                                break;
//                        }
//                        //txtFee.setText("20.00");
//                    }
//                } else {
//                    txtFee.setText("0.00");
//                }
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPurchaseAmount.getText().length() <= 0) {
                    Snackbar.make(view, "Please enter an amount to purchase.", Snackbar.LENGTH_LONG).show();
                } else {
                    if (Integer.parseInt(txtPurchaseAmount.getText().toString()) < 100) {
                        Snackbar.make(view, "The minimum amount you can purchase is 100.00.", Snackbar.LENGTH_LONG).show();
                    } else {

                        if (Integer.parseInt(txtPurchaseAmount.getText().toString()) != 0) {
                            Bundle b = new Bundle();
                            b.putDouble("Amount", Double.parseDouble(txtPurchaseAmount.getText().toString()));
                            b.putDouble("Fee", Double.parseDouble(txtFee.getText().toString()));
                            b.putString("Gateway", gateway);
                            b.putString("Outlet", outletname);
                            Intent intent = new Intent(mcontext, creditsConfirmation.class);
                            intent.putExtras(b);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(view, "Please enter a valid amount to purchase.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        imgrequestcredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                RequestCredits.showRequestDialog(mcontext, creditsAmount.this, PartnerID, MainActivity.user.mobile);
                v.setEnabled(true);
            }
        });

        imgWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallet();
            }
        });

        //endregion


    }


    //region Functions

    public void wallet() {

        progressDialog.showDialog(mcontext, "BudgetLoad", "Fetching Wallet... please wait", false);

        try {
            FetchWallet fetchWallet = new FetchWallet(creditsAmount.this);
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
                        Deactivated.showDeactivatedAccount(mcontext, creditsAmount.this);
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

    //endregion

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

    //region THREADS

    //endregion

}
