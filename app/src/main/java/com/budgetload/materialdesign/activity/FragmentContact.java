package com.budgetload.materialdesign.activity;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.budgetload.materialdesign.ArrayList.MyList;
import com.budgetload.materialdesign.Constant.PreloadedData;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.adapter.ContactListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FragmentContact extends Fragment {

    public FragmentContact() {
    }

    View rootView;
    boolean found = false;
    String text = "Check out BudgetLoad for your Quick, Simple and Secure airtime reloading. - www.budgetload.com       \n \n" +
            "For google play download:https://play.google.com/store/apps/details?id=com.epayvenue.budgetload";
    String phoneNumber = "";
    String contactname = "";
    Button invite;
    Dialog dialog;
    Dialog dialog1;
    Intent intent;
    String contactId;
    ImageView myImageView;
    TextView dialogcontactno;
    ColorGenerator generator;
    EditText searchitem;

    private ArrayList<MyList> myContactList;
    ListView listvew;


    //Add invitation
    String invitationname = "Invite Friends";
    String invitationmsg = "through viber, whatsapp and messenger";
    String invitaionid = "0";

    // and name should be displayed in the text1 textview in item layout
    // private static final String[] FROM = {Contacts.DISPLAY_NAME_PRIMARY,ContactsContract.CommonDataKinds.Phone.NUMBER};
    //  private static final int[] TO = {R.id.txtname,R.id.txtcontactno};
    private ContactListAdapter mAdapter;

    MyList newlist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_contacts, container, false);


        generator = ColorGenerator.MATERIAL;

        myContactList = new ArrayList<MyList>();

        // dialog
        dialog = new Dialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light));
        // dialog.setCancelable(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_invite);


        // dialog
        dialog1 = new Dialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light));

        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.popup_optionsinvite);
        dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                listvew.setEnabled(true);

            }
        });


        ImageView messenger = (ImageView) dialog.findViewById(R.id.imageView2);
        ImageView viber = (ImageView) dialog.findViewById(R.id.imageView3);
        ImageView whatsapp = (ImageView) dialog.findViewById(R.id.imageView4);
        ImageView contacts = (ImageView) dialog.findViewById(R.id.imageView5);
        TableRow topup = (TableRow) dialog1.findViewById(R.id.wrap1);
        TableRow stock = (TableRow) dialog1.findViewById(R.id.wrap2);
        myImageView = (ImageView) dialog1.findViewById(R.id.imageView9);
        dialogcontactno = (TextView) dialog1.findViewById(R.id.textView7);
        TableRow invite = (TableRow) dialog1.findViewById(R.id.wrap3);

        listvew = (ListView) rootView.findViewById(R.id.listView);
        listvew.setClickable(true);
        listvew
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        listvew.setEnabled(false);
                        phoneNumber = ((TextView) view
                                .findViewById(R.id.txtcontactno)).getText()
                                .toString();

                        contactname = ((TextView) view
                                .findViewById(R.id.txtname)).getText()
                                .toString();

                        contactId = ((TextView) view.findViewById(R.id.txtcontactid)).getText().toString();

                        if (contactId.equals("0")) {
                            displaySelectedInvitation();
                        } else {
                            displayOptionInvite();
                        }

                    }

                });

        // displayContacts();

        messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMessenger(v);
            }
        });

        viber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickViber(v);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickWhatsApp(v);
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContact(v);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickWhatsApp(v);
            }
        });

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newmobile = "0" + phoneNumber.substring(Math.max(0, phoneNumber.length() - 10));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.topupload);

                Bundle args = new Bundle();
                args.putString("mobile", newmobile);

                if (fragment != null) {
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                } else {

                    fragment = new TopUpLoad();
                    fragment.setArguments(args);


                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                    // set the toolbar title

                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Top-Up");

                }

                dialog1.dismiss();

            }
        });

        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.stocktransfer);

                phoneNumber.replace(" ", "");
                phoneNumber.replace("+", "");

                String newmobile = "0" + phoneNumber.substring(Math.max(0, phoneNumber.length() - 10));

                Bundle args = new Bundle();
                args.putString("mobile", newmobile);


                if (fragment != null) {


                    fragment.setArguments(args);

                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                } else {

                    fragment = new StockTransfer();
                    fragment.setArguments(args);


                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                    // set the toolbar title

                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Stock Transfer");

                }

                dialog1.dismiss();

            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContact(v);
            }
        });


        searchitem = (EditText) rootView.findViewById(R.id.searchitem);
        searchitem.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchitem.getText().length() == 0) {
                    //displayContacts();
                } else {
                    getPhoneNumber(searchitem.getText().toString());
                }
            }
        });


        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //new displayContacts().execute();
        displayContacts();

    }


    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************
    public void displayContacts() {


        //Record method
        myContactList.clear();
        //for invitation
        MyList newlist1 = new MyList();
        newlist1.setContactname(invitationname);
        newlist1.setMobileNumber(invitationmsg);
        newlist1.setContactId(invitaionid);
        myContactList.add(newlist1);

        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Photo.PHOTO, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");
        if (cursor != null) {
            try {
                final int id = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                final int lblname = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                final int lblnumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                final int lblcontactimage = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
                String prevname = "";
                String prevnum = "";

                while (cursor.moveToNext()) {
                    String contactid = cursor.getString(id);
                    String contactName = cursor.getString(lblname);
                    int contactimage = cursor.getInt(lblcontactimage);
                    String phoneNo = cursor.getString(lblnumber);

                    if ((!prevname.equals(contactName) || !prevnum.equals(phoneNo))) {
                        //you will get all phone numbers
                        if (phoneNo.length() > 10) {
                            newlist = new MyList();
                            newlist.setContactname(contactName);
                            newlist.setMobileNumber(phoneNo.replaceAll("[\\s\\-()]", ""));
                            String rawprefix = "0" + phoneNo.substring(phoneNo.length() - 10);
                            newlist.setCarriername(getCarrierName(rawprefix.substring(0, 4)));
                            newlist.setImage(contactimage);
                            newlist.setContactId(contactid);
                            //for one contact with multiple same contact no
                            myContactList.add(newlist);


                            //for duplication purposes name,number !=
                            prevname = contactName;
                            prevnum = phoneNo;

                        }
                    }
                }
                mAdapter = new ContactListAdapter(getActivity(), R.layout.list_contact, myContactList);
                listvew.setAdapter(mAdapter);

            } finally {
                cursor.close();
            }
        }
    }

    public void getPhoneNumber(String fname) {

        myContactList.clear();

        Cursor c;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like'%" + fname + "%'";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Photo.PHOTO, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        c = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        if (c != null) {
            try {
                final int id = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                final int lblname = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                final int lblnumber = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                final int lblcontactimage = c.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
                String prevname = "";
                String prevnum = "";

                while (c.moveToNext()) {
                    String contactid = c.getString(id);
                    String contactName = c.getString(lblname);
                    int contactimage = c.getInt(lblcontactimage);
                    String phoneNo = c.getString(lblnumber);

                    if ((!prevname.equals(contactName) || !prevnum.equals(phoneNo))) {
                        //you will get all phone numbers
                        if (phoneNo.length() > 10) {
                            newlist = new MyList();
                            newlist.setContactname(contactName);
                            newlist.setMobileNumber(phoneNo.replaceAll("[\\s\\-()]", ""));
                            String rawprefix = "0" + phoneNo.substring(phoneNo.length() - 10);
                            newlist.setCarriername(getCarrierName(rawprefix.substring(0, 4)));
                            newlist.setImage(contactimage);
                            newlist.setContactId(contactid);
                            //for one contact with multiple same contact no
                            myContactList.add(newlist);


                            //for duplication purposes name,number !=
                            prevname = contactName;
                            prevnum = phoneNo;

                        }
                    }
                }
                mAdapter = new ContactListAdapter(getActivity(), R.layout.list_contact, myContactList);
                listvew.setAdapter(mAdapter);
            } finally {
                c.close();
            }
        }
    }

    public String getCarrierName(String mobileprefix) {

        String prefix;
        String network = "";

        try {

            JSONArray array = new JSONArray(PreloadedData.prefix);

            for (int i = 0; i < array.length(); i++) {
                JSONObject currObject = array.getJSONObject(i);
                prefix = currObject.getString("Prefix");
                if (prefix.equals(mobileprefix)) {
                    network = currObject.getString("Brand");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return network;

    }

    public void displaySelectedInvitation() {
        listvew.setEnabled(true);
        //dialog1.dismiss();
        dialog.show();


    }

    public void displayOptionInvite() {
        listvew.setEnabled(true);

        // Bitmap myimage = openDisplayPhoto(Long.parseLong(contactId));

        String firstLetter = String.valueOf(contactname.charAt(0));
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter.toUpperCase(), color); // radius in px
        myImageView.setImageDrawable(drawable);


        dialogcontactno.setText(phoneNumber);

        dialog1.show();
    }

    public Bitmap openDisplayPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);


        try {
            AssetFileDescriptor fd =
                    getActivity().getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");

            // return fd.createInputStream();

            Bitmap bmp = BitmapFactory.decodeStream(fd.createInputStream());

            return bmp;

        } catch (IOException e) {
            return null;
        }
    }

    public void onClickWhatsApp(View view) {
        listvew.setEnabled(true);

        PackageManager pm = getActivity().getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");


            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");


            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Please Install WhatsApp application", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void OnClickViber(View view) {

        listvew.setEnabled(true);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getActivity().getPackageManager()
                .queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase(
                        Locale.getDefault()).contains("com.viber.voip")
                        || info.activityInfo.name.toLowerCase(
                        Locale.getDefault()).contains("com.viber.voip")) {

                    share.putExtra(Intent.EXTRA_TEXT, text);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;

                    startActivity(Intent.createChooser(share, "Share with"));

                    break;
                }
            }
            if (!found) {

                Toast.makeText(getActivity(), "Please Install viber application", Toast.LENGTH_LONG).show();
                // Uri marketUri = Uri.parse("market://details?id="+ "com.viber.voip");
                // Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                // startActivity(marketIntent);
            }

        }
    }

    public void onClickMessenger(View view) {
        listvew.setEnabled(true);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent
                .putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");
        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Please Install Facebook Messenger", Toast.LENGTH_SHORT).show();
        }


    }

    public void onClickContact(View view) {

        listvew.setEnabled(true);

        //Intent sendIntent = new Intent(Intent.ACTION_SEND);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        if (isNumeric(phoneNumber) == true) {
            sendIntent.setData(Uri.parse("smsto:" + phoneNumber));
        } else {
            sendIntent.setData(Uri.parse("smsto:"));
        }
        sendIntent.putExtra("sms_body", text);
        startActivity(sendIntent);
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    //endregion


}
