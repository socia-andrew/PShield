package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.budgetload.materialdesign.ArrayList.MyList;
import com.budgetload.materialdesign.Common.HideKeyboard;
import com.budgetload.materialdesign.Constant.PreloadedData;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.adapter.ContactListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity {

    private ArrayList<MyList> myContactList;
    ListView listvew;
    EditText searchitem;
    private ContactListAdapter mAdapter;
    MyList newlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        HideKeyboard.hideKeyboard(this);

        myContactList = new ArrayList<MyList>();

        listvew = (ListView) findViewById(R.id.listView);
        searchitem = (EditText) findViewById(R.id.searchitem);

        listvew.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                listvew.setEnabled(false);
                String contact = ((TextView) view
                        .findViewById(R.id.txtcontactno)).getText().toString();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Contact", contact);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();


            }

        });

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
                    displaycontacts();
                } else {
                    getPhoneNumber(searchitem.getText().toString());
                }
            }
        });


        displaycontacts();


    }

    //region TRIGGERS
    //************************
    //TRIGGERS
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

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void displaycontacts(){
        myContactList.clear();

        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Photo.PHOTO, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");
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
                mAdapter = new ContactListAdapter(this, R.layout.list_contact, myContactList);
                listvew.setAdapter(mAdapter);
            } finally {
                cursor.close();
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

    public void getPhoneNumber(String fname) {

        myContactList.clear();

        Cursor c;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like'%" + fname + "%'";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Photo.PHOTO, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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
                            String rawprefix = "0" + phoneNo.substring(phoneNo.length() - 10);
                            newlist.setCarriername(getCarrierName(rawprefix.substring(0, 4)));
                            newlist.setMobileNumber(phoneNo.replaceAll("[\\s\\-()]", ""));
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
                mAdapter = new ContactListAdapter(this, R.layout.list_contact, myContactList);
                listvew.setAdapter(mAdapter);
            } finally {
                c.close();
            }
        }
    }

    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    //endregion

}



