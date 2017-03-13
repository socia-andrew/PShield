package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetload.materialdesign.ArrayList.NotificationList;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.adapter.NotificationAdapter;

import java.util.ArrayList;

public class Notifications extends Fragment {

    View rootView;
    ListView lvnotifications;
    DataBaseHandler db;
    RelativeLayout Rtlblanknotification;
    RelativeLayout RtlNotifications;
    private ArrayList<NotificationList> notiflist;
    AlertDialog.Builder alertDialog;
    NotificationAdapter notificationAdapter;

    String txtid;

    View.OnClickListener mOnClickListener;
    String mobile = "";
    AlertDialog alert11;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        db = new DataBaseHandler(getActivity());

        notiflist = new ArrayList<NotificationList>();

        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Notification");
        alertDialog.setMessage("Are you sure you want to delete this notification?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DeleteNotification();
                lvnotifications.setEnabled(true);

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                lvnotifications.setEnabled(true);
                dialog.cancel();
            }
        });

        alert11 = alertDialog.create();


        lvnotifications = (ListView) rootView.findViewById(R.id.notiflist);
        Rtlblanknotification = (RelativeLayout) rootView.findViewById(R.id.blanknotif);
        RtlNotifications = (RelativeLayout) rootView.findViewById(R.id.rtlnotification);

        lvnotifications.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressWarnings("rawtypes")
            public boolean onItemLongClick(AdapterView parent,
                                           final View view, final int position, long id) {

                lvnotifications.setEnabled(false);
                txtid = ((TextView) view
                        .findViewById(R.id.txtid)).getText().toString();
                alert11.show();
                Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonbackground.setTextColor(Color.parseColor("#646464"));

                Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonbackground1.setTextColor(Color.parseColor("#19BD4E"));
                return false;
            }
        });


        lvnotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                lvnotifications.setAdapter(null);

                //final int myPosition = position;
                lvnotifications.setEnabled(false);
                String notificationid = ((TextView) view
                        .findViewById(R.id.txtid)).getText().toString();

                Intent intent = new Intent(getActivity(), Notification_Details.class);
                Bundle b = new Bundle();
                db.updateNotification(db, notificationid);
                b.putString("NotificationID", notificationid);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
                lvnotifications.setEnabled(true);
                lvnotifications.setAdapter(notificationAdapter);
            }
        });

//        lvnotifications.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int pos, long id) {
//                // TODO Auto-generated method stub
//                lvnotifications.setAdapter(null);
//                String title = ((TextView) arg1
//                        .findViewById(R.id.txttitle)).getText().toString();
//                mobile = ((TextView) arg1
//                        .findViewById(R.id.txtrmobile)).getText().toString();
//                //Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
//                if (title.equalsIgnoreCase("Request Credits")) {
//                    Snackbar.make(lvnotifications, "Request Credits", Snackbar.LENGTH_LONG)
//                            .setAction("Transfer", mOnClickListener)
//                            .setActionTextColor(Color.WHITE)
//                            .show();
//                }
//                lvnotifications.setAdapter(notificationAdapter);
//                return true;
//            }
//        });

//        mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.stocktransfer);
//
//                Bundle args = new Bundle();
//                args.putString("mobile", mobile);
//
//                if (fragment != null) {
//                    fragment.setArguments(args);
//                    fragmentTransaction.replace(R.id.container_body, fragment);
//                    fragmentTransaction.commit();
//
//                } else {
//
//                    fragment = new StockTransfer();
//                    fragment.setArguments(args);
//
//
//                    fragmentTransaction.replace(R.id.container_body, fragment);
//                    fragmentTransaction.commit();
//
//                    // set the toolbar title
//
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Transfer Credits");
//
//                }
//
//            }
//        };


        displayNotifications();


        return rootView;

    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        notiflist.clear();
        displayNotifications();

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("mobile");


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.stocktransfer);

                Bundle args = new Bundle();
                args.putString("mobile", result);

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

                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Transfer Credits");

                }

            }

        }

    }


    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void DeleteNotification() {
        try {
            db.deleteNotification(db, txtid);
            Toast.makeText(getActivity(), "Notification already deleted", Toast.LENGTH_LONG).show();
            notiflist.clear();
            displayNotifications();
        } catch (Exception e) {
        }

    }

    public void displayNotifications() {
        try {
            Cursor mycursor = db.getNotifications(db);

            if (mycursor.getCount() > 0) {
                RtlNotifications.setVisibility(View.VISIBLE);
                Rtlblanknotification.setVisibility(View.GONE);
                while (mycursor.moveToNext()) {
                    //Toast.makeText(getContext(), mycursor.getString(mycursor.getColumnIndex("Sender")), Toast.LENGTH_SHORT).show();
                    NotificationList newlist = new NotificationList();
                    newlist.setTitle(mycursor.getString(mycursor.getColumnIndex("Title")));
                    newlist.setMessage(mycursor.getString(mycursor.getColumnIndex("Message")));
                    newlist.setSender(mycursor.getString(mycursor.getColumnIndex("Sender")));
                    newlist.setDatesent(mycursor.getString(mycursor.getColumnIndex("DateSent")));
                    newlist.setId(mycursor.getInt(mycursor.getColumnIndex("_id")));
                    newlist.setStatus(mycursor.getString(mycursor.getColumnIndex("Status")));
                    newlist.setMobile(mycursor.getString(mycursor.getColumnIndex("Extra1")));
                    notiflist.add(newlist);
                }
                notificationAdapter = new NotificationAdapter(getActivity(), R.layout.list_notifications, notiflist);
                lvnotifications.setAdapter(notificationAdapter);
            } else {
                RtlNotifications.setVisibility(View.GONE);
                Rtlblanknotification.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }

    }


    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    //endregion


}
