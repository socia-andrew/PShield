package com.budgetload.materialdesign.activity.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.CheckInternet;
import com.budgetload.materialdesign.Common.CreateSession;
import com.budgetload.materialdesign.Common.Deactivated;
import com.budgetload.materialdesign.Common.DialogError;
import com.budgetload.materialdesign.Common.FetchWallet;
import com.budgetload.materialdesign.Common.GPSTracker;
import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.Common.HideKeyboard;
import com.budgetload.materialdesign.Common.NetworkUtil;
import com.budgetload.materialdesign.Common.RequestCredits;
import com.budgetload.materialdesign.Common.progressDialog;
import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.Constant.Indicators;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.activity.ContactList;
import com.budgetload.materialdesign.Common.GlobalVariables;
import com.budgetload.materialdesign.activity.MainActivity;
import com.budgetload.materialdesign.activity.Product;
import com.budgetload.materialdesign.activity.TopUpError;
import com.google.android.gms.ads.AdView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

//States
// 0 - input number
// 1 - select product
// 2 - topup

public class FragmentTopUp extends Fragment implements View.OnTouchListener, View.OnClickListener, Constant, OnItemSelectedListener {

    //region declaring variables
    View rootView;
    Context mcontext;
    EditText editTargetMobile;
    EditText editRegLoad;
    TextView editNetwork;
    TextView txtProdType;
    TextView txtproductcode;
    TextView txtproductamount;
    TextView txtproductdisount;
    TextView txtMyWallet;
    ImageView mywalleticon;
    ImageView contacticon;
    ImageView imgrequestcredits;
    TextView popmobile;
    TextView popbrand;
    TextView popProdCode;
    TextView popAmount;
    TextView popTopUP;
    TextView popTxn;
    DecimalFormat dec;
    Dialog dialog;
    Dialog dialog1;
    Spinner spinType;
    FloatingActionButton fab;
    String networkprfix;
    String brand;
    String imei;
    String mobile = "";
    String productcode = "";
    String productamount = "";
    String tartgetmobile = "";
    String productdiscount = "";
    String producttype = "";
    String referenceno = "";
    String message = "";
    String SessionID;
    AlertDialog.Builder alertDialog;
    String[] selecttype;
    ArrayAdapter<String> spinTypeAdapter;
    String PartnerID;
    DataBaseHandler db;
    int topupState = 0;
    int globeState = 0;
    int imgClear;
    double longitude;
    double latitude;
    GPSTracker gps;
    RelativeLayout.LayoutParams params;
    int walletoff;
    int walleton;

    EditText inputpass;
    AlertDialog confimation;
    AlertDialog.Builder builder;
    String PasswordStatus = "";
    String confimpass = "";
    String CurrentPassword = "";

    AdView mAdView1;

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        try {
            //inflate fragment
            rootView = inflater.inflate(R.layout.activity_top_up_load, container, false);
            mcontext = getActivity();


//            AdRequest adRequest = new AdRequest.Builder().build();
//            mAdView1 = (AdView) rootView.findViewById(R.id.adView1);
//            mAdView1.loadAd(adRequest);

            //Initialize database
            db = new DataBaseHandler(getActivity());


            //region setting initial value
            dec = new DecimalFormat("#,###,###.####");
            dec.setMinimumFractionDigits(2);
            walletoff = R.drawable.walletsmalloff;
            walleton = R.drawable.wallet;
            selecttype = getResources().getStringArray(R.array.loadtype);
            txtMyWallet = (TextView) rootView.findViewById(R.id.txtMyWallet);
            mywalleticon = (ImageView) rootView.findViewById(R.id.walleticon);
            mywalleticon.setOnClickListener(this);
            contacticon = (ImageView) rootView.findViewById(R.id.imgcontact);
            contacticon.setOnClickListener(this);
            editTargetMobile = (EditText) rootView.findViewById(R.id.txttargetmobile);
            editRegLoad = (EditText) rootView.findViewById(R.id.editRegLoad);
            editNetwork = (TextView) rootView.findViewById(R.id.txtnetwork);
            txtProdType = (TextView) rootView.findViewById(R.id.txtprodtype);
            params = (RelativeLayout.LayoutParams) txtProdType.getLayoutParams();
            txtproductcode = (TextView) rootView.findViewById(R.id.productcode);
            txtproductamount = (TextView) rootView.findViewById(R.id.productamount);
            txtproductdisount = (TextView) rootView.findViewById(R.id.productdiscount);
            spinType = (Spinner) rootView.findViewById(R.id.loadtype);
            spinType.setOnItemSelectedListener(this);
            spinTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, selecttype);
            spinTypeAdapter.setDropDownViewResource(R.layout.spinner_arrow);
            spinType.setAdapter(spinTypeAdapter);
            imgClear = R.drawable.xbutton;
            alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setCancelable(false);
            imgrequestcredits = (ImageView) rootView.findViewById(R.id.requestcredits);
            fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

            dialog = new Dialog(new ContextThemeWrapper(mcontext, android.R.style.Theme_Holo_Light));
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.pop_confirmation);

            dialog1 = new Dialog(new ContextThemeWrapper(mcontext, android.R.style.Theme_Holo_Light));
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.pop_versioncontrol);
            dialog1.setCancelable(false);
            //endregion

            //region COnfirmation for topUp
            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirmation");
            inputpass = new EditText(getActivity());
            inputpass.setHint("Password");
            inputpass.setPadding(60, 10, 60, 10);
            inputpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(inputpass, 10, 100, 10, 20);
            builder.setCancelable(false);

            builder.setPositiveButton("Top-Up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    confimpass = GlobalFunctions.getSha1Hex(inputpass.getText().toString());
                    txtProdType.setEnabled(false);
                    btnconfirm();
                    fab.setEnabled(true);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    fab.setEnabled(true);
                    confimation.dismiss();
                    inputpass.setText("");
                }
            });


            confimation = builder.create();
            //endregion

            //region INITIALIZATION
            popmobile = (TextView) dialog.findViewById(R.id.popnum);
            popbrand = (TextView) dialog.findViewById(R.id.popbrand);
            popProdCode = (TextView) dialog.findViewById(R.id.popProduct);
            popAmount = (TextView) dialog.findViewById(R.id.popAmount);
            popTopUP = (TextView) dialog.findViewById(R.id.popTopUp);
            popTxn = (TextView) dialog.findViewById(R.id.popTxns);
            //endregion


            //get IMEI
            imei = getIMEI();
            GlobalVariables.imei = imei;

            //get partnerID
            PartnerID = db.getPartnerID(db);

            //get other info
            Cursor cursor = db.getIsRegistered(db);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                } while (cursor.moveToNext());
            }

            //getPassword Status
            Cursor c = db.getPassword(db);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    PasswordStatus = c.getString(c.getColumnIndex("PasswordStatus"));
                    CurrentPassword = c.getString(c.getColumnIndex("Password"));
                } while (c.moveToNext());
            }

            //get wallet
            Cursor crWallet = db.getWallet(db);
            if (crWallet.getCount() > 0) {
                crWallet.moveToFirst();
                do {
                    MainActivity.walletvalue = crWallet.getString(crWallet.getColumnIndex("Balance"));
                    txtMyWallet.setText("" + crWallet.getString(crWallet.getColumnIndex("Balance")) + "");
                } while (crWallet.moveToNext());
            } else {
                txtMyWallet.setText("0.00 ");
            }


            //INLINE TRIGGERS

            //get Wallet in the cloud indicator is true (meaning, ether first load, after topup or after fetching topup transaction)
            if (Indicators.WalletFetchIndicator.equals("true")) {
                processGettingWallet();
            }

            //Target Mobile input listener
            editTargetMobile.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editTargetMobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                    if (count > 0) {
                        editTargetMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgClear, 0);
                    } else {
                        editTargetMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 11) {
                        Character num = editTargetMobile.getText().toString().charAt(0);
                        if (!num.toString().equals("0")) {
                            Toast.makeText(getActivity(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                            txtProdType.setVisibility(View.GONE);
                            editNetwork.setVisibility(View.GONE);
                            editRegLoad.setVisibility(View.GONE);
                            spinType.setVisibility(View.GONE);
                            fab.setVisibility(View.GONE);
                            brand = "";
                        } else {
                            tartgetmobile = editTargetMobile.getText().toString();
                            networkprfix = tartgetmobile.substring(1, 4); //editTargetMobile.getText().toString().substring(1, 4);
                            brand = db.getNetworkPrefix(db, networkprfix);

                            editNetwork.setVisibility(View.VISIBLE);
                            HideKeyboard.hideKeyboard(getActivity());


                            if (brand.length() == 0) {
                                txtProdType.setVisibility(View.GONE);
                                editNetwork.setVisibility(View.GONE);
                                editRegLoad.setVisibility(View.GONE);
                                spinType.setVisibility(View.GONE);
                                fab.setVisibility(View.GONE);
                                brand = "";
                                Toast.makeText(getActivity(), "Invalid Mobile Number (Prefix not defined.)", Toast.LENGTH_LONG).show();
                            } else {

                                if (brand.equalsIgnoreCase("TNT")) {
                                    editNetwork.setText("TALK 'N TEXT");
                                } else if (brand.equalsIgnoreCase("BUDDY")) {
                                    editNetwork.setText("SMART BUDDY");
                                } else if (brand.equalsIgnoreCase("BUDDY")) {
                                    editNetwork.setText("SMART BUDDY/BRO");
                                } else {
                                    editNetwork.setText(brand);
                                }


                                String myname = getContactDisplayNameByNumber(editTargetMobile.getText().toString());
                                // editTargetMobile.setText(tartgetmobile + "\n" + Html.fromHtml("<span>" + myname + "</span>"));
                                if (myname != null && !myname.equalsIgnoreCase("?")) {
                                    editTargetMobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                                    editTargetMobile.setText(tartgetmobile + "\n" + Html.fromHtml("<span>" + myname + "</span>"));

                                    if (brand.equalsIgnoreCase("GLOBE")) {
                                        Toast.makeText(mcontext, "Sorry, Globe is temporarily not avaiable.", Toast.LENGTH_SHORT).show();
                                        editTargetMobile.setText("");

//                                    } else if (brand.equalsIgnoreCase("TNT") ||
//                                            brand.equalsIgnoreCase("BUDDY") ||
//                                            brand.equalsIgnoreCase("BUDDYBRO")) {
//
//                                        boolean isAllowed = db.getCheckNumber(db, brand, networkprfix, editTargetMobile.getText().toString());
//
//                                        if (!isAllowed) {
//                                            Toast.makeText(getActivity(), "Sorry, Cannot Top-Up this number", Toast.LENGTH_LONG).show();
//                                            return;
//                                        }
//
//                                        params.setMargins(0, 7, 0, 0);
//                                        txtProdType.setLayoutParams(params);
//                                        txtProdType.setVisibility(View.VISIBLE);
//                                        editRegLoad.setVisibility(View.GONE);
//                                        fab.setVisibility(View.VISIBLE);
//                                        topupState = 1;

//
                                    } else {


                                        params.setMargins(0, 7, 0, 0);
                                        txtProdType.setLayoutParams(params);
                                        txtProdType.setVisibility(View.VISIBLE);
                                        editRegLoad.setVisibility(View.GONE);
                                        fab.setVisibility(View.VISIBLE);
                                        topupState = 1;
                                    }

                                } else {

                                    if (brand.equalsIgnoreCase("GLOBE")) {
                                        Toast.makeText(mcontext, "Sorry, Globe is temporarily not avaiable.", Toast.LENGTH_SHORT).show();
                                        editTargetMobile.setText("");
                                        // spinType.setVisibility(View.VISIBLE);
                                        // spinType.setSelection(0);
                                    } else {
                                        params.setMargins(0, 7, 0, 0);
                                        txtProdType.setLayoutParams(params);
                                        txtProdType.setVisibility(View.VISIBLE);
                                        editRegLoad.setVisibility(View.GONE);
                                        fab.setVisibility(View.VISIBLE);
                                        topupState = 1;
                                    }

                                }
                            }
                        }


                    } else {
                        topupState = 0;
                        fab.setVisibility(View.GONE);

                        txtProdType.setText("");
                        editNetwork.setText("");
                        editRegLoad.setText("");

                        txtProdType.setVisibility(View.GONE);
                        editNetwork.setVisibility(View.GONE);
                        editRegLoad.setVisibility(View.GONE);
                        spinType.setVisibility(View.GONE);
                        spinType.setSelection(0);
                    }

                }
            });

            editTargetMobile.setOnTouchListener(new View.OnTouchListener()

            {
                final int DRAWABLE_RIGHT = 2;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (editTargetMobile.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                            int leftEdgeOfRightDrawable = editTargetMobile.getRight()
                                    - editTargetMobile.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                            leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.EdtPadding);
                            if (event.getRawX() >= leftEdgeOfRightDrawable) {
                                editTargetMobile.setText("");
                                editNetwork.setText("");
                                fab.setVisibility(View.GONE);
                                txtProdType.setVisibility(View.GONE);
                                editRegLoad.setVisibility(View.GONE);
                                editNetwork.setVisibility(View.GONE);
                                spinType.setVisibility(View.GONE);
                                txtProdType.setText("");
                                txtproductcode.setText("");
                                txtproductamount.setText("");
                                txtproductdisount.setText("");
                                topupState = 0;
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });


            //Click on the circle Icon to submit
            fab.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    fab.setEnabled(false);
                    confirmdialog(); //open dialog to confirm
                }
            });

            txtProdType.setOnClickListener(this);

            //For the amount (regular load sa globe) listener
            editRegLoad.addTextChangedListener(new

                                                       TextWatcher() {
                                                           @Override
                                                           public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                           }

                                                           @Override
                                                           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                           }

                                                           @Override
                                                           public void afterTextChanged(Editable s) {
                                                               if (s.length() > 0) {
                                                                   fab.setVisibility(View.VISIBLE);
                                                                   topupState = 2;
                                                               }
                                                           }
                                                       });


            //
            Bundle mybundle = getArguments();
            if (mybundle != null)

            {
                String mobile = mybundle.getString("mobile").toString();
                if (mobile.length() > 0) {
                    HideKeyboard.hideKeyboard(mcontext);
                    editTargetMobile.setText(mobile);
                }
            }

            editRegLoad.setOnEditorActionListener(new TextView.OnEditorActionListener()

            {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    int result = actionId & EditorInfo.IME_MASK_ACTION;
                    switch (result) {
                        case EditorInfo.IME_ACTION_NEXT:
                            HideKeyboard.hideKeyboard(getActivity());
                            break;
                    }
                    return false;
                }
            });


            //Confirming Top-Up (DIALOG)
            popTopUP.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    editTargetMobile.setText("");
                    editNetwork.setText("");
                    fab.setVisibility(View.GONE);
                    txtProdType.setVisibility(View.GONE);
                    editRegLoad.setVisibility(View.GONE);
                    spinType.setVisibility(View.GONE);
                    editNetwork.setVisibility(View.GONE);
                    txtProdType.setText("");
                    txtproductcode.setText("");
                    txtproductamount.setText("");
                    txtproductdisount.setText("");
                    topupState = 0;
                }
            });

            //Choosing Transaction option after top-up (go to transaction)
            popTxn.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    callTransaction.theMethod();
                }
            });


            //click on Update (force update modal)
            Button ok = (Button) dialog1.findViewById(R.id.update);
            ok.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Uri uriUrl = Uri.parse("https://play.google.com/store/apps/details?id=com.epayvenue.budgetload");
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }
            });

            //click cancel on the force quit update
            Button cancel = (Button) dialog1.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    dialog1.cancel();
                    getActivity().finish();
                }

            });

            imgrequestcredits.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    RequestCredits.showRequestDialog(mcontext, getActivity(), PartnerID, mobile);
                    v.setEnabled(true);
                }
            });

            //endregion

        } catch (Exception e) {
        }


        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

        try {

            //by ann,reinitialize variables
            imei = getIMEI();
            GlobalVariables.imei = imei;

            PartnerID = db.getPartnerID(db);

            Cursor cursor = db.getIsRegistered(db);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    try {
                        mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                    } catch (Exception e) {
                    }

                } while (cursor.moveToNext());
            }

            editTargetMobile = (EditText) rootView.findViewById(R.id.txttargetmobile);
            editTargetMobile.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(editTargetMobile, 0);
                }
            }, 200);
        } catch (Exception e) {
        }


    }


    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                HideKeyboard.hideKeyboard(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //open Contacts
            case R.id.imgcontact:
                contacticon.setEnabled(false);
                Intent myintent = new Intent(getActivity(), ContactList.class);
                startActivityForResult(myintent, 5);
                break;

            //Click Wallet Icon
            case R.id.walleticon:
                progressDialog.showDialog(mcontext, "BudgetLoad", "Fetching Wallet... please wait", false);
                processGettingWallet();
                break;

            //Open Product Type
            case R.id.txtprodtype:
                txtProdType.setEnabled(false);
                Intent intent = new Intent(mcontext, Product.class);
                Bundle b = new Bundle();
                b.putString("Brand", brand);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
                break;


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            HideKeyboard.hideKeyboard(getActivity());
            contacticon.setEnabled(true);
            if (requestCode == 1) {

                //Toast.makeText(mcontext, "here" + resultCode, Toast.LENGTH_SHORT).show();

                txtProdType.setEnabled(true);
                if (resultCode == Activity.RESULT_OK) {

                   // Toast.makeText(mcontext, "Here1" + resultCode, Toast.LENGTH_SHORT).show();

                    txtProdType.setText(data.getStringExtra("ProductDescription"));
                    txtproductcode.setText(data.getStringExtra("ProductCode"));
                    txtproductamount.setText(data.getStringExtra("ProductAmount"));
                    txtproductdisount.setText(data.getStringExtra("ProductDiscount"));

                    fab.setVisibility(View.VISIBLE);
                    topupState = 2;
                }
            }

            if (requestCode == 2) {

            }

            if (requestCode == 3) {

                editTargetMobile.setText("");
                editNetwork.setText("");
                fab.setVisibility(View.GONE);
                txtProdType.setVisibility(View.GONE);
                editRegLoad.setVisibility(View.GONE);
                spinType.setVisibility(View.GONE);
                editNetwork.setVisibility(View.GONE);
                txtProdType.setText("");
                txtproductcode.setText("");
                txtproductamount.setText("");
                txtproductdisount.setText("");
                topupState = 0;


                if (resultCode == 4) {
                    callTransaction.theMethod();
                }

            }

            if (requestCode == 5) {

                txtProdType.setText("");
                txtproductcode.setText("");
                txtproductamount.setText("");
                txtproductdisount.setText("");

                if (resultCode == Activity.RESULT_OK) {
                    String mobile = data.getStringExtra("Contact");
                    String newmobile = mobile.substring(Math.max(0, mobile.length() - 10));

                    editTargetMobile.setText("0" + newmobile);
                    topupState = 1;
                    fab.setVisibility(View.GONE);
                    contacticon.setEnabled(true);

                    if (brand.equalsIgnoreCase("Globe")) {
                        spinType.setVisibility(View.VISIBLE);
                    } else {
                        editRegLoad.setVisibility(View.GONE);
                        spinType.setVisibility(View.GONE);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        try {
            String selected = parent.getItemAtPosition(position).toString();

            if (selected.equalsIgnoreCase("Regular")) {
                topupState = 1;
                editRegLoad.setText("");
                txtProdType.setText("");
                editRegLoad.setVisibility(View.VISIBLE);
                txtProdType.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                params.setMargins(0, 5, 0, 0);
                txtProdType.setLayoutParams(params);
                globeState = 0;
            } else if (selected.equalsIgnoreCase("Special")) {
                topupState = 1;
                editRegLoad.setVisibility(View.GONE);
                txtProdType.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                editRegLoad.setText("");
                txtProdType.setText("");
                params.setMargins(0, 5, 0, 0);
                txtProdType.setLayoutParams(params);
                globeState = 1;
            } else {
                editRegLoad.setVisibility(View.GONE);
                txtProdType.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                topupState = 1;
            }
        } catch (Exception e) {
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    private void fetchWallet() {

        try {
            FetchWallet fetchWallet = new FetchWallet(getActivity());

            fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {

                    String[] rawdata = data.split(";");

                    if (rawdata[0].equals("Success")) {
                        if (txtMyWallet != null) {
                            txtMyWallet.setText("" + rawdata[1] + "");
                        }
                    } else if (rawdata[0].equalsIgnoreCase("Deactivated")) {
                        db.DropAllTable(db);
                        Deactivated.showDeactivatedAccount(mcontext, getActivity());
                    } else {
                        Toast.makeText(mcontext, "Failed to fetch Wallet from the server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }


            });

            fetchWallet.execute(imei, SessionID, PartnerID);
        } catch (Exception e) {
        }

    }

    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = "?";

        ContentResolver contentResolver = mcontext.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[]{BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }

    public void walleticonstat(Integer val) {
        if (val.equals(1)) {
            mywalleticon.setBackgroundResource(walletoff);
        } else {
            mywalleticon.setBackgroundResource(walleton);
        }
    }

    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public void confirmdialog() {

        try {
            String mdltext = "";
            if (topupState == 1) {
                if (brand.equalsIgnoreCase("GLOBE")) {
                    switch (globeState) {
                        case 0:
                            if (globeState == 0 && editRegLoad.getText().length() > 0) {
                            } else {
                                Toast.makeText(getActivity(), "Please enter a regular load.", Toast.LENGTH_LONG).show();
                                fab.setEnabled(true);
                            }
                            break;
                        case 1:

                            if (globeState == 1 && txtProdType.getText().length() > 0) {

                            } else {
                                Toast.makeText(getActivity(), "Please select a product type.", Toast.LENGTH_LONG).show();
                                fab.setEnabled(true);
                            }

                            break;
                    }

                } else {
                    if (topupState == 1 && txtProdType.getText().length() == 0) {
                        Toast.makeText(getActivity(), "Please select a product type.", Toast.LENGTH_LONG).show();
                    }
                }
            }
            if (topupState == 2) {


                if (brand.equalsIgnoreCase("GLOBE")) {

                    if (globeState == 0) {
                        mdltext = "Are you sure you want to Top-up " + editRegLoad.getText().toString() + " to " + editTargetMobile.getText().toString() + "?";

                        productcode = "LD";
                        productamount = editRegLoad.getText().toString();
                        producttype = "Regular Load";

                    } else {
                        mdltext = "Are you sure you want to Top-up " + txtProdType.getText().toString() + " to " + editTargetMobile.getText().toString() + "?";

                        productcode = txtproductcode.getText().toString();
                        productamount = txtproductamount.getText().toString();
                        producttype = txtProdType.getText().toString();
                    }


                } else {

                    mdltext = "Are you sure you want to Top-up " + txtProdType.getText().toString() + " to " + editTargetMobile.getText().toString() + "?";

                    productcode = txtproductcode.getText().toString();
                    productamount = txtproductamount.getText().toString();
                    producttype = txtProdType.getText().toString();
                }


                //tartgetmobile = editTargetMobile.getText().toString();
                productdiscount = txtproductdisount.getText().toString();

                // alertDialog.setMessage("" + mdltext + "");
                // alertDialog.show();

                if (PasswordStatus.equals("on")) {
                    inputpass.setVisibility(View.VISIBLE);
                } else {
                    inputpass.setVisibility(View.GONE);
                }
                confimation.setMessage(mdltext);
                confimation.show();
                confimation.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#646464"));
                confimation.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#19BD4E"));
                HideKeyboard.hideKeyboard(getActivity());

            }

        } catch (Exception e) {
        }


    }

    public void btnconfirm() {

        try {
            if ((PasswordStatus.equals("on") && confimpass.equals(CurrentPassword)) || PasswordStatus.equals("off") || PasswordStatus.equals("")) {
                int status = NetworkUtil.getConnectivityStatusString(getActivity());
                if (status == 0) {
                    CheckInternet.showConnectionDialog(mcontext);
                    progressDialog.hideDialog();
                } else {

                    progressDialog.showDialog(mcontext, "TopUp", "Processing Top-Up. Please wait...", false);
                    CreateSession newsession = new CreateSession(getActivity());
                    newsession.setQueryListener(new CreateSession.QueryListener() {
                        @SuppressWarnings("unchecked")
                        public void QuerySuccessFul(String data) {
                            String[] rawdata = data.split(";");
                            if (rawdata[0].equals("Success")) {
                                SessionID = rawdata[1];
                                new processTopUp().execute();
                            } else {
                                Toast.makeText(mcontext, "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                progressDialog.hideDialog();
                            }
                        }

                    });
                    newsession.execute(PartnerID);
                }
            } else {
                fab.setEnabled(true);
                Toast.makeText(getActivity(), "Password is incorrect.", Toast.LENGTH_SHORT).show();


            }


        } catch (Exception e) {
            progressDialog.hideDialog();
        }


        //      }
    }

    public void processGettingWallet() {

        try {
            int constatus = NetworkUtil.getConnectivityStatusString(getActivity());
            if (constatus == 0) {
                CheckInternet.showConnectionDialog(getActivity());
                progressDialog.hideDialog();
            } else {

                mywalleticon.setEnabled(false);
                walleticonstat(1);

                CreateSession newsession = new CreateSession(getActivity());
                newsession.setQueryListener(new CreateSession.QueryListener() {
                    @SuppressWarnings("unchecked")
                    public void QuerySuccessFul(String data) {
                        String[] rawdata = data.split(";");
                        if (rawdata[0].equalsIgnoreCase("Success")) {
                            SessionID = rawdata[1];
                            wallet();
                        } else if (rawdata[0].equalsIgnoreCase("timeout")) {
                            Toast.makeText(mcontext, "Please check you internet connection.", Toast.LENGTH_LONG).show();
                            progressDialog.hideDialog();
                            walleticonstat(0);
                        } else {
                            Toast.makeText(mcontext, "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                            progressDialog.hideDialog();
                            walleticonstat(0);
                        }
                    }

                });
                newsession.execute(PartnerID);
            }
        } catch (Exception e) {
        }


    }

    public void wallet() {

        try {
            FetchWallet fetchWallet = new FetchWallet(getActivity());
            fetchWallet.setWalletListener(new FetchWallet.WalletListener() {

                @SuppressWarnings("unchecked")
                public void QuerySuccessFul(String data) {

                    String[] rawdata = data.split(";");

                    if (rawdata[0].equals("Success")) {
                        if (txtMyWallet != null) {
                            try {
                                txtMyWallet.setText("" + rawdata[1] + "");
                                mywalleticon.setEnabled(true);
                                walleticonstat(0);
                                progressDialog.hideDialog();
                                Indicators.WalletFetchIndicator = "false";
                                if (!versionCode.equals(rawdata[3])) {
                                    dialog1.show();
                                }
                            } catch (Exception e) {
                                progressDialog.hideDialog();
                            }

                        }
                    } else if (rawdata[0].equalsIgnoreCase("Deactivated")) {
                        db.DropAllTable(db);
                        Deactivated.showDeactivatedAccount(mcontext, getActivity());
                    } else {
                        mywalleticon.setEnabled(true);
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

    private GoToTransactions callTransaction;

    public void onAttach(Activity activity) {
        callTransaction = (GoToTransactions) activity;
        super.onAttach(activity);
    }

    public interface GoToTransactions {
        void theMethod();
    }


    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************

    class processTopUp extends AsyncTask<Void, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity)
                throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0)
                    out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            String text;
            try {
                String authcode = GlobalFunctions.getSha1Hex(imei
                        .toLowerCase()
                        + mobile
                        + Constant.topup
                        + SessionID.toLowerCase());
                String apiURL = LOADURL
                        + "&IMEI="
                        + imei + "&SourceMobTel="
                        + mobile + "&SessionNo="
                        + SessionID + "&AuthCode=" + authcode
                        + "&TargetMobTel=" + tartgetmobile + "&ProductCode="
                        + productcode + "&Amount="
                        + productamount + "&Longitude="
                        + longitude + "&Latitude="
                        + latitude + "&PartnerID="
                        + PartnerID + "&Password=" + confimpass + "&PasswordStatus=" + PasswordStatus + "";
                Log.d("URI", apiURL);

                HttpGet httpGet = new HttpGet(apiURL);
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 60000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 60000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                text = null;
            }
            return text;

        }

        @Override
        protected void onPostExecute(String s) {

            try {
                if (s == null) {
                    progressDialog.hideDialog();
                    Toast.makeText(mcontext, " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                } else {

                    try {

                        JSONObject json = new JSONObject(s);
                        JSONObject articles = json.getJSONObject("TopUp");
                        String sessionresult = articles.getString("Result");
                        String resultcode = articles.getString("ResultCode");

                        referenceno = articles.getString("ReferenceNumber");
                        message = articles.getString("Message");


                        if (sessionresult.equals("OK")) {
                            // new commitTopup().execute();
                            CreateSession newsession = new CreateSession(getActivity());
                            newsession.setQueryListener(new CreateSession.QueryListener() {
                                @SuppressWarnings("unchecked")
                                public void QuerySuccessFul(String data) {
                                    String[] rawdata = data.split(";");
                                    if (rawdata[0].equals("Success")) {
                                        SessionID = rawdata[1];
                                        new commitTopup().execute();
                                    } else {
                                        Toast.makeText(mcontext, "Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                                        progressDialog.hideDialog();
                                    }
                                }

                            });
                            newsession.execute(PartnerID);

                        } else {
                            txtProdType.setEnabled(true);
                            progressDialog.hideDialog();
                            if (resultcode.equals("1009")) {
                                db.DropAllTable(db);
                                Deactivated.showDeactivatedAccount(mcontext, getActivity());
                            }
                            //Error TopUP
                            else if (resultcode.equals("1011")) {
                                Bundle b = new Bundle();
                                b.putString("Brand", brand);
                                Intent intent = new Intent(getActivity(), TopUpError.class);
                                intent.putExtras(b);
                                startActivityForResult(intent, 2);
                            } else {

                                progressDialog.hideDialog();
                                if (message.equals("Network is offline")) {
                                    Bundle b = new Bundle();
                                    b.putString("Brand", brand);

                                    Intent intent = new Intent(getActivity(), TopUpError.class);
                                    intent.putExtras(b);
                                    startActivityForResult(intent, 2);
                                } else {
                                    DialogError.alertDialogShow(mcontext, "BudgetLoad", message);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        progressDialog.hideDialog();
                        Toast.makeText(mcontext, " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
            }


        }
    }

    class commitTopup extends AsyncTask<Void, Void, String> {


        protected String getASCIIContentFromEntity(HttpEntity entity)
                throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0)
                    out.append(new String(b, 0, n));
            }
            return out.toString();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            String text;
            try {

                String authcode = GlobalFunctions.getSha1Hex(imei
                        .toLowerCase()
                        + mobile
                        + Constant.commitopup
                        + SessionID.toLowerCase());
                String apiURL = COMMITURL
                        + "&IMEI="
                        + imei + "&SourceMobTel="
                        + mobile + "&SessionNo="
                        + SessionID + "&AuthCode=" + authcode
                        + "&TargetMobTel=" + tartgetmobile + "&ReferenceNo="
                        + referenceno + "&PartnerID="
                        + PartnerID + "";

                Log.d("URI", apiURL);

                HttpGet httpGet = new HttpGet(apiURL);
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 60000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 60000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                text = null;
            }
            return text;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                txtProdType.setEnabled(true);
                //btnconfirm.setEnabled(true);

                if (s == null) {
                    Toast.makeText(mcontext, " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    progressDialog.hideDialog();
                } else {
                    try {

                        JSONObject json = new JSONObject(s);
                        JSONObject articles = json.getJSONObject("CommitTopUp");
                        String sessionresult = articles.getString("Result");
                        String datetimein = articles.getString("DateTimeIN");
                        String BudgetLoadRef = articles.getString("ReferenceNo");
                        String TxnNo = articles.getString("TxnNo");
                        String resultmessage = articles.getString("Message");


                        if (sessionresult.equals("OK")) {

                            Bundle b = new Bundle();
                            b.putString("Mobile", mobile);
                            b.putString("Brand", brand);
                            b.putString("TargetMobile", tartgetmobile);
                            b.putString("ProductType", producttype);
                            b.putString("ProductAmount", productamount);


                            //commented by ann
                            // db.saveTopUpPending(db, brand, TxnNo, datetimein, mobile, tartgetmobile, productcode, productamount, producttype, "PENDING", BudgetLoadRef);

                            Indicators.TopUpIndicator = "true";

                            popmobile.setText(tartgetmobile);
                            popbrand.setText(brand);
                            popProdCode.setText(producttype);
                            popAmount.setText("P " + dec.format(Double.parseDouble(productamount)) + "");
                            dialog.show();

                            //fetch wallet
                            fetchWallet();

                            GlobalFunctions.fbLogger(getActivity(), "TopUp", b, 1);

                            progressDialog.hideDialog();

                        } else {
                            progressDialog.hideDialog();
                            if (resultmessage.equals("Network is offline")) {
                                Bundle b = new Bundle();
                                b.putString("Brand", brand);

                                Intent intent = new Intent(getActivity(), TopUpError.class);
                                intent.putExtras(b);
                                startActivityForResult(intent, 2);
                            } else {
                                Toast.makeText(mcontext,
                                        resultmessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        progressDialog.hideDialog();
                        Toast.makeText(mcontext, " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                progressDialog.hideDialog();
                Toast.makeText(mcontext, " Failed to Connect Server. Please try again.", Toast.LENGTH_LONG).show();
            }


        }
    }

    //endregion

}
