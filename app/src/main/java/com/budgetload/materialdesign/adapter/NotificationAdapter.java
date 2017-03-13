package com.budgetload.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.budgetload.materialdesign.ArrayList.NotificationList;
import com.budgetload.materialdesign.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by andrewlaurienrsocia on 11/27/15.
 */
public class NotificationAdapter extends ArrayAdapter<NotificationList> {

    private Activity activity;
    Context mcontext;
    DecimalFormat dec;

    public NotificationAdapter(Activity activity, int resource, List<NotificationList> list) {

        super(activity, resource, list);
        this.activity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        dec = new DecimalFormat("###,###,###,###.####");
        dec.setMinimumFractionDigits(2);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_notifications, parent,
                    false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }


        NotificationList mylist = getItem(position);
        String firstLetter = "";
        TextDrawable drawable;

        if (mylist.getSender().length() > 0) {
            firstLetter = String.valueOf(mylist.getSender().charAt(0));
            drawable = TextDrawable.builder()
                    .buildRound(firstLetter, Color.parseColor("#2B60D0")); // radius in px
            holder.sender.setText(mylist.getSender());
        } else {
            drawable = TextDrawable.builder()
                    .buildRound("B", Color.parseColor("#2B60D0"));
            holder.sender.setText(mylist.getMobile());

        }
        holder.imageView.setImageDrawable(drawable);
        holder.title.setText(mylist.getTitle());
        holder.message.setText(mylist.getMessage());

        holder.txtdate.setText(mylist.getDatesent());
        holder.txtid.setText("" + mylist.getID());
        holder.txtmobile.setText(mylist.getMobile());

        if (mylist.getstatus().equalsIgnoreCase("UNREAD")) {
            convertView.setBackgroundColor(Color.parseColor("#dddddd"));
        }

        return convertView;

    }

    private static class ViewHolder {

        private TextView title;
        private TextView message;
        private TextView sender;
        private TextView txtdate;
        private ImageView imageView;
        private TextView txtid;
        private TextView txtmobile;


        public ViewHolder(View v) {

            title = (TextView) v.findViewById(R.id.txttitle);
            message = (TextView) v.findViewById(R.id.txtmessage);
            sender = (TextView) v.findViewById(R.id.sender);
            txtdate = (TextView) v.findViewById(R.id.txtdate);
            imageView = (ImageView) v.findViewById(R.id.image_view);
            txtid = (TextView) v.findViewById(R.id.txtid);
            txtmobile = (TextView) v.findViewById(R.id.txtrmobile);


        }
    }

}


